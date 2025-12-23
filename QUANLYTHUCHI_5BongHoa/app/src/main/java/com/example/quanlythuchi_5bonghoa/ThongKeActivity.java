package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThongKeActivity extends AppCompatActivity {

    private PieChart pieChart;
    private RecyclerView recyclerView;
    private GiaoDichAdapter giaoDichAdapter;
    private ImageView ivBack;
    private MaterialButtonToggleGroup toggleButtonGroup;

    private TextView tvTongThu, tvTongChi, tvConLai;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private int userId = -1;
    private String currentFilter = "thang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        pieChart = findViewById(R.id.piechart);
        recyclerView = findViewById(R.id.recycler_view_giao_dich);
        ivBack = findViewById(R.id.iv_back);
        toggleButtonGroup = findViewById(R.id.toggle_button_group);

        tvTongThu = findViewById(R.id.tvTongThu);
        tvTongChi = findViewById(R.id.tvTongChi);
        tvConLai = findViewById(R.id.tvConLai);

        setupRecyclerView();
        ivBack.setOnClickListener(v -> finish());

        userId = getUserId();
        Log.d("ThongKeActivity", "userId=" + userId);

        if (userId <= 0) {
            toast("Chưa xác định người dùng. Vui lòng đăng nhập lại.");
            // return;
        }

        // Listener đổi filter
        toggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;

            if (checkedId == R.id.btn_ngay) currentFilter = "ngay";
            else if (checkedId == R.id.btn_thang) currentFilter = "thang";
            else if (checkedId == R.id.btn_nam) currentFilter = "nam";

            loadData(currentFilter);
        });

        // IMPORTANT: luôn gọi loadData() lần đầu, KHÔNG phụ thuộc listener
        // (vì btn_thang có thể đã checked sẵn trong XML)
        loadData(currentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

    private int getUserId() {
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sp.getInt("user_id", -1);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        giaoDichAdapter = new GiaoDichAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(giaoDichAdapter);
    }

    private void loadData(String filter) {
        if (userId <= 0) return;

        executor.execute(() -> {
            int totalAll = countAllTransactions(userId);
            Log.d("ThongKeActivity", "totalAllTransactions=" + totalAll + " | filter=" + filter);

            List<GiaoDich> list = fetchGiaoDichFromDb(filter);

            runOnUiThread(() -> {
                if (list == null) {
                    toast("Lỗi kết nối hoặc truy vấn dữ liệu.");
                    return;
                }

                Log.d("ThongKeActivity", "rows=" + list.size());

                giaoDichAdapter.setGiaoDichs(list);
                updateTongSo(list);
                setupPieChart(list);

                if (list.isEmpty()) {
                    toast("Không có giao dịch theo bộ lọc: " + filter);
                }
            });
        });
    }

    private int countAllTransactions(int uid) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return -1;

            ps = conn.prepareStatement("SELECT COUNT(*) AS Cnt FROM GiaoDich WHERE MaNguoiDung = ?");
            ps.setInt(1, uid);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("Cnt");
            return 0;

        } catch (Exception e) {
            Log.e("ThongKeActivity", "countAllTransactions error", e);
            return -1;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    /**
     * Lấy giao dịch + danh mục + ngày để adapter hiển thị đầy đủ.
     * Lọc theo ngày/tháng/năm dựa theo máy Android (ổn định hơn GETDATE và Timestamp với jTDS).
     */
    private List<GiaoDich> fetchGiaoDichFromDb(String filter) {
        List<GiaoDich> giaoDichs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return null;

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;

            String sql;
            if ("ngay".equals(filter)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String today = sdf.format(cal.getTime());

                sql = "SELECT gd.TenGiaoDich, gd.SoTien, gd.NgayGiaoDich, dm.TenDanhMuc, dm.LoaiDanhMuc, dm.BieuTuong " +
                        "FROM GiaoDich gd " +
                        "JOIN DanhMuc dm ON gd.MaDanhMuc = dm.MaDanhMuc " +
                        "WHERE gd.MaNguoiDung = ? AND CONVERT(date, gd.NgayGiaoDich) = ? " +
                        "ORDER BY gd.NgayGiaoDich DESC";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, userId);
                ps.setString(2, today);

                Log.d("ThongKeActivity", "SQL(day): today=" + today);

            } else if ("nam".equals(filter)) {

                sql = "SELECT gd.TenGiaoDich, gd.SoTien, gd.NgayGiaoDich, dm.TenDanhMuc, dm.LoaiDanhMuc, dm.BieuTuong " +
                        "FROM GiaoDich gd " +
                        "JOIN DanhMuc dm ON gd.MaDanhMuc = dm.MaDanhMuc " +
                        "WHERE gd.MaNguoiDung = ? AND YEAR(gd.NgayGiaoDich) = ? " +
                        "ORDER BY gd.NgayGiaoDich DESC";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, userId);
                ps.setInt(2, year);

                Log.d("ThongKeActivity", "SQL(year): year=" + year);

            } else { // thang

                sql = "SELECT gd.TenGiaoDich, gd.SoTien, gd.NgayGiaoDich, dm.TenDanhMuc, dm.LoaiDanhMuc, dm.BieuTuong " +
                        "FROM GiaoDich gd " +
                        "JOIN DanhMuc dm ON gd.MaDanhMuc = dm.MaDanhMuc " +
                        "WHERE gd.MaNguoiDung = ? AND YEAR(gd.NgayGiaoDich) = ? AND MONTH(gd.NgayGiaoDich) = ? " +
                        "ORDER BY gd.NgayGiaoDich DESC";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, userId);
                ps.setInt(2, year);
                ps.setInt(3, month);

                Log.d("ThongKeActivity", "SQL(month): year=" + year + ", month=" + month);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                String ten = rs.getString("TenGiaoDich");
                double soTien = rs.getDouble("SoTien");
                java.util.Date ngay = rs.getTimestamp("NgayGiaoDich");

                String tenDanhMuc = rs.getString("TenDanhMuc");
                String loaiDanhMuc = rs.getString("LoaiDanhMuc");
                String bieuTuong = rs.getString("BieuTuong");

                giaoDichs.add(new GiaoDich(ten, soTien, ngay, tenDanhMuc, loaiDanhMuc, bieuTuong));
            }

            return giaoDichs;

        } catch (Exception e) {
            Log.e("ThongKeActivity", "fetchGiaoDichFromDb error", e);
            return null;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    private void updateTongSo(List<GiaoDich> list) {
        double tongThu = 0;
        double tongChi = 0;

        for (GiaoDich gd : list) {
            if (gd.isTienVao()) tongThu += gd.getSoTien();
            else tongChi += gd.getSoTien();
        }

        double conLai = tongThu - tongChi;

        tvTongThu.setText("TỔNG THU: " + formatVnd(tongThu));
        tvTongChi.setText("TỔNG CHI: " + formatVnd(tongChi));
        tvConLai.setText("CÒN LẠI: " + formatVnd(conLai));
    }

    private void setupPieChart(List<GiaoDich> list) {
        float tongThu = 0f;
        float tongChi = 0f;

        for (GiaoDich gd : list) {
            if (gd.isTienVao()) tongThu += gd.getSoTien();
            else tongChi += gd.getSoTien();
        }

        if (tongThu == 0f && tongChi == 0f) {
            pieChart.clear();
            pieChart.setNoDataText("Không có dữ liệu để thống kê");
            pieChart.invalidate();
            return;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(tongChi, "Tổng chi"));
        entries.add(new PieEntry(tongThu, "Tổng thu"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{Color.RED, Color.GREEN});
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();
    }

    private String formatVnd(double value) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return nf.format(value) + " VND";
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
