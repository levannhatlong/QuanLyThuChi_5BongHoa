package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrangChuActivity extends AppCompatActivity {

    private TextView tvGreeting, tvTienChi, tvTienThu, tvSoDu, tvChiTiet;
    private LineChart lineChart;
    private LinearLayout btnViTien, btnThemGiaoDich, btnThongKe;
    private RecyclerView recyclerViewGiaoDich;
    private ImageView ivNotification, ivSettings, ivGhichu;

    private GiaoDichAdapter giaoDichAdapter;
    private List<GiaoDich> danhSachGiaoDich;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi: không tìm thấy người dùng.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDatabase();
    }

    private void initViews() {
        tvGreeting = findViewById(R.id.tv_greeting);
        tvTienChi = findViewById(R.id.tv_tien_chi);
        tvTienThu = findViewById(R.id.tv_tien_thu);
        tvSoDu = findViewById(R.id.tv_so_du);
        tvChiTiet = findViewById(R.id.tv_chi_tiet);

        lineChart = findViewById(R.id.line_chart);

        btnViTien = findViewById(R.id.btn_vi_tien);
        btnThemGiaoDich = findViewById(R.id.btn_them_giao_dich);
        btnThongKe = findViewById(R.id.btn_thong_ke);

        recyclerViewGiaoDich = findViewById(R.id.recycler_view_giao_dich);

        ivNotification = findViewById(R.id.iv_notification);
        ivSettings = findViewById(R.id.iv_settings);
        ivGhichu = findViewById(R.id.iv_ghichu);
    }

    private void setupRecyclerView() {
        recyclerViewGiaoDich.setLayoutManager(new LinearLayoutManager(this));
        danhSachGiaoDich = new ArrayList<>();
        giaoDichAdapter = new GiaoDichAdapter(this, danhSachGiaoDich);
        recyclerViewGiaoDich.setAdapter(giaoDichAdapter);
    }

    private void setupClickListeners() {
        btnViTien.setOnClickListener(v -> startActivity(new Intent(this, ViTienActivity.class)));
        btnThemGiaoDich.setOnClickListener(v -> startActivity(new Intent(this, ThemGiaoDichActivity.class)));
        btnThongKe.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));

        ivNotification.setOnClickListener(v -> startActivity(new Intent(this, ThongBaoActivity.class)));
        ivGhichu.setOnClickListener(v -> startActivity(new Intent(this, GhiChuActivity.class)));
        ivSettings.setOnClickListener(v -> startActivity(new Intent(this, CaiDatActivity.class)));

        // "Xem tất cả" -> qua Thống kê (hoặc màn danh sách giao dịch nếu bạn có)
        tvChiTiet.setOnClickListener(v -> startActivity(new Intent(this, ThongKeActivity.class)));
    }

    private void loadDataFromDatabase() {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) {
                runOnUiThread(() ->
                        Toast.makeText(TrangChuActivity.this, "Không thể kết nối database", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            String userName = "";
            double totalIncome = 0;
            double totalExpense = 0;
            List<GiaoDich> transactions = new ArrayList<>();
            ArrayList<Entry> incomeEntries = new ArrayList<>();
            ArrayList<Entry> expenseEntries = new ArrayList<>();

            try {
                // 1) Lấy tên user
                PreparedStatement userStmt = connection.prepareStatement(
                        "SELECT HoTen FROM NguoiDung WHERE MaNguoiDung = ?"
                );
                userStmt.setInt(1, currentUserId);
                ResultSet userRs = userStmt.executeQuery();
                if (userRs.next()) userName = userRs.getString("HoTen");
                userRs.close();
                userStmt.close();

                // 2) Lấy giao dịch + join danh mục để biết Thu/Chi
                String transQuery =
                        "SELECT g.TenGiaoDich, g.SoTien, g.NgayGiaoDich, d.TenDanhMuc, d.LoaiDanhMuc, d.BieuTuong " +
                                "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                                "WHERE g.MaNguoiDung = ? ORDER BY g.NgayGiaoDich DESC";

                PreparedStatement transStmt = connection.prepareStatement(transQuery);
                transStmt.setInt(1, currentUserId);
                ResultSet transRs = transStmt.executeQuery();

                int incomeIndex = 0;
                int expenseIndex = 0;

                while (transRs.next()) {
                    String tenGiaoDich = transRs.getString("TenGiaoDich");
                    double soTien = transRs.getDouble("SoTien");
                    Date ngayGiaoDich = transRs.getTimestamp("NgayGiaoDich");
                    String tenDanhMuc = transRs.getString("TenDanhMuc");
                    String loaiDanhMuc = transRs.getString("LoaiDanhMuc");
                    String bieuTuong = transRs.getString("BieuTuong");

                    // List 5 giao dịch gần nhất
                    if (transactions.size() < 5) {
                        transactions.add(new GiaoDich(tenGiaoDich, soTien, ngayGiaoDich, tenDanhMuc, loaiDanhMuc, bieuTuong));
                    }

                    if ("Thu nhập".equalsIgnoreCase(loaiDanhMuc)) {
                        totalIncome += soTien;
                        incomeEntries.add(new Entry(incomeIndex++, (float) soTien));
                    } else {
                        totalExpense += soTien;
                        expenseEntries.add(new Entry(expenseIndex++, (float) soTien));
                    }
                }

                transRs.close();
                transStmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(TrangChuActivity.this, "Lỗi khi tải dữ liệu.", Toast.LENGTH_SHORT).show()
                );
            } finally {
                try { connection.close(); } catch (SQLException ignored) {}
            }

            String finalUserName = userName;
            double finalTotalIncome = totalIncome;
            double finalTotalExpense = totalExpense;

            runOnUiThread(() -> {
                updateUI(finalUserName, finalTotalIncome, finalTotalExpense, transactions);
                setupLineChart(incomeEntries, expenseEntries);
            });
        }).start();
    }

    private void updateUI(String userName, double income, double expense, List<GiaoDich> transactions) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        tvGreeting.setText(userName != null && !userName.isEmpty() ? userName : "Người dùng");
        tvTienThu.setText(formatter.format(income));
        tvTienChi.setText(formatter.format(expense));
        tvSoDu.setText(formatter.format(income - expense));

        danhSachGiaoDich.clear();
        danhSachGiaoDich.addAll(transactions);
        giaoDichAdapter.notifyDataSetChanged();
    }

    private void setupLineChart(ArrayList<Entry> incomeEntries, ArrayList<Entry> expenseEntries) {
        LineDataSet incomeDataSet = new LineDataSet(incomeEntries, "Thu");
        incomeDataSet.setColor(Color.parseColor("#43A047"));
        incomeDataSet.setLineWidth(2f);
        incomeDataSet.setCircleColor(Color.parseColor("#43A047"));
        incomeDataSet.setDrawValues(false);

        LineDataSet expenseDataSet = new LineDataSet(expenseEntries, "Chi");
        expenseDataSet.setColor(Color.parseColor("#E53935"));
        expenseDataSet.setLineWidth(2f);
        expenseDataSet.setCircleColor(Color.parseColor("#E53935"));
        expenseDataSet.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(incomeDataSet);
        dataSets.add(expenseDataSet);

        LineData lineData = new LineData(dataSets);

        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(true);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.invalidate();
    }
}
