package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThongKeActivity extends AppCompatActivity {

    private PieChart pieChart;
    private RecyclerView recyclerView;
    private GiaoDichAdapter giaoDichAdapter;
    private List<GiaoDich> danhSachGiaoDich;
    private ImageView ivBack;
    private MaterialButtonToggleGroup toggleButtonGroup;
    private TextView tvTongThu, tvTongChi, tvConLai;

    private int currentUserId;
    private String currentFilter = "thang"; // Default filter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        // Get current user ID
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy người dùng.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        setupListeners();

        // Load initial data
        loadTransactionData(currentFilter);
    }

    private void initViews() {
        pieChart = findViewById(R.id.piechart);
        recyclerView = findViewById(R.id.recycler_view_giao_dich);
        ivBack = findViewById(R.id.iv_back);
        toggleButtonGroup = findViewById(R.id.toggle_button_group);
        tvTongThu = findViewById(R.id.tv_tong_thu);
        tvTongChi = findViewById(R.id.tv_tong_chi);
        tvConLai = findViewById(R.id.tv_con_lai);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        danhSachGiaoDich = new ArrayList<>();
        giaoDichAdapter = new GiaoDichAdapter(this, danhSachGiaoDich);
        recyclerView.setAdapter(giaoDichAdapter);
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        toggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_ngay) {
                    currentFilter = "ngay";
                } else if (checkedId == R.id.btn_thang) {
                    currentFilter = "thang";
                } else if (checkedId == R.id.btn_nam) {
                    currentFilter = "nam";
                }
                loadTransactionData(currentFilter);
            }
        });
    }

    private void loadTransactionData(String filter) {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) {
                runOnUiThread(() -> Toast.makeText(ThongKeActivity.this, "Không thể kết nối database", Toast.LENGTH_SHORT).show());
                return;
            }

            List<GiaoDich> transactions = new ArrayList<>();
            double totalIncome = 0;
            double totalExpense = 0;

            try {
                String dateFilterClause = "";
                switch (filter) {
                    case "ngay":
                        dateFilterClause = " AND CONVERT(date, g.NgayGiaoDich) = CONVERT(date, GETDATE())";
                        break;
                    case "thang":
                        dateFilterClause = " AND MONTH(g.NgayGiaoDich) = MONTH(GETDATE()) AND YEAR(g.NgayGiaoDich) = YEAR(GETDATE())";
                        break;
                    case "nam":
                        dateFilterClause = " AND YEAR(g.NgayGiaoDich) = YEAR(GETDATE())";
                        break;
                }

                String query = "SELECT g.TenGiaoDich, g.SoTien, g.NgayGiaoDich, d.TenDanhMuc, d.LoaiDanhMuc, d.BieuTuong " +
                             "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                             "WHERE g.MaNguoiDung = ?" + dateFilterClause +
                             " ORDER BY g.NgayGiaoDich DESC";

                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, currentUserId);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String tenGiaoDich = rs.getString("TenGiaoDich");
                    double soTien = rs.getDouble("SoTien");
                    Date ngayGiaoDich = rs.getDate("NgayGiaoDich");
                    String tenDanhMuc = rs.getString("TenDanhMuc");
                    String loaiDanhMuc = rs.getString("LoaiDanhMuc");
                    String bieuTuong = rs.getString("BieuTuong");

                    transactions.add(new GiaoDich(tenGiaoDich, soTien, ngayGiaoDich, tenDanhMuc, loaiDanhMuc, bieuTuong));

                    if ("Thu nhập".equals(loaiDanhMuc)) {
                        totalIncome += soTien;
                    } else {
                        totalExpense += soTien;
                    }
                }
                rs.close();
                stmt.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ThongKeActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show());
            } finally {
                 try {
                    if (connection != null) connection.close();
                 } catch (SQLException e) {e.printStackTrace();}
            }

            double finalTotalIncome = totalIncome;
            double finalTotalExpense = totalExpense;
            double balance = totalIncome - totalExpense;

            runOnUiThread(() -> {
                danhSachGiaoDich.clear();
                danhSachGiaoDich.addAll(transactions);
                giaoDichAdapter.notifyDataSetChanged();

                updateSummary(finalTotalIncome, finalTotalExpense, balance);
                updatePieChart(finalTotalIncome, finalTotalExpense);
            });

        }).start();
    }

    private void updateSummary(double income, double expense, double balance) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTongThu.setText("TỔNG THU: " + formatter.format(income));
        tvTongChi.setText("TỔNG CHI: " + formatter.format(expense));
        tvConLai.setText("CÒN LẠI: " + formatter.format(balance));
    }

    private void updatePieChart(double income, double expense) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) expense, "Tổng chi"));
        entries.add(new PieEntry((float) income, "Tổng thu"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{Color.parseColor("#E53935"), Color.parseColor("#43A047")}); // Red, Green
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setCenterText("Báo cáo");
        pieChart.setCenterTextSize(18f);
        pieChart.invalidate(); // refresh
    }
}
