package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThongKeActivity extends AppCompatActivity {

    private PieChart pieChart;
    private RecyclerView recyclerView;
    private GiaoDichAdapter giaoDichAdapter;
    private ImageView ivBack;
    private MaterialButtonToggleGroup toggleButtonGroup;
    private TextView tvTongThu, tvTongChi, tvSoDu, tvTitleGiaoDich, tvEmpty;

    private int currentUserId;
    private String currentFilter = "thang"; // ngay, thang, nam

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi: không tìm thấy người dùng.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDatabase();
    }

    private void initViews() {
        pieChart = findViewById(R.id.piechart);
        recyclerView = findViewById(R.id.recycler_view_giao_dich);
        ivBack = findViewById(R.id.iv_back);
        toggleButtonGroup = findViewById(R.id.toggle_button_group);
        tvTongThu = findViewById(R.id.tv_tong_thu);
        tvTongChi = findViewById(R.id.tv_tong_chi);
        tvSoDu = findViewById(R.id.tv_so_du);
        tvTitleGiaoDich = findViewById(R.id.tv_title_giao_dich);
        tvEmpty = findViewById(R.id.tv_empty);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        giaoDichAdapter = new GiaoDichAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(giaoDichAdapter);
    }

    private void setupListeners() {
        ivBack.setOnClickListener(v -> finish());

        toggleButtonGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_ngay) {
                    currentFilter = "ngay";
                    tvTitleGiaoDich.setText("Giao dịch hôm nay");
                } else if (checkedId == R.id.btn_thang) {
                    currentFilter = "thang";
                    tvTitleGiaoDich.setText("Giao dịch tháng này");
                } else if (checkedId == R.id.btn_nam) {
                    currentFilter = "nam";
                    tvTitleGiaoDich.setText("Giao dịch năm nay");
                }
                loadDataFromDatabase();
            }
        });

        // Mặc định chọn tháng
        toggleButtonGroup.check(R.id.btn_thang);
    }

    private void loadDataFromDatabase() {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) {
                runOnUiThread(() -> Toast.makeText(this, "Không thể kết nối database", Toast.LENGTH_SHORT).show());
                return;
            }

            double totalIncome = 0;
            double totalExpense = 0;
            List<GiaoDich> transactions = new ArrayList<>();

            try {
                String dateCondition = getDateCondition();

                String query = "SELECT g.TenGiaoDich, g.SoTien, g.NgayGiaoDich, d.TenDanhMuc, d.LoaiDanhMuc, d.BieuTuong " +
                        "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                        "WHERE g.MaNguoiDung = ? " + dateCondition +
                        " ORDER BY g.NgayGiaoDich DESC, g.MaGiaoDich DESC";

                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, currentUserId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String tenGiaoDich = rs.getString("TenGiaoDich");
                    double soTien = rs.getDouble("SoTien");
                    Date ngayGiaoDich = rs.getTimestamp("NgayGiaoDich");
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

            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            double finalTotalIncome = totalIncome;
            double finalTotalExpense = totalExpense;
            List<GiaoDich> finalTransactions = transactions;

            runOnUiThread(() -> {
                updateUI(finalTotalIncome, finalTotalExpense, finalTransactions);
            });
        }).start();
    }

    private String getDateCondition() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        switch (currentFilter) {
            case "ngay":
                return String.format("AND CAST(g.NgayGiaoDich AS DATE) = '%d-%02d-%02d'", year, month, day);
            case "thang":
                return String.format("AND YEAR(g.NgayGiaoDich) = %d AND MONTH(g.NgayGiaoDich) = %d", year, month);
            case "nam":
                return String.format("AND YEAR(g.NgayGiaoDich) = %d", year);
            default:
                return "";
        }
    }

    private void updateUI(double income, double expense, List<GiaoDich> transactions) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        tvTongThu.setText("+" + formatter.format(income));
        tvTongChi.setText("-" + formatter.format(expense));
        tvSoDu.setText(formatter.format(income - expense));

        setupPieChart((float) income, (float) expense);

        giaoDichAdapter.setGiaoDichs(transactions);

        if (transactions.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setupPieChart(float income, float expense) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        if (income > 0 || expense > 0) {
            if (expense > 0) entries.add(new PieEntry(expense, "Chi tiêu"));
            if (income > 0) entries.add(new PieEntry(income, "Thu nhập"));
        } else {
            entries.add(new PieEntry(1, "Chưa có dữ liệu"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        if (income > 0 || expense > 0) {
            ArrayList<Integer> colors = new ArrayList<>();
            if (expense > 0) colors.add(Color.parseColor("#E53935"));
            if (income > 0) colors.add(Color.parseColor("#43A047"));
            dataSet.setColors(colors);
        } else {
            dataSet.setColors(Color.parseColor("#E0E0E0"));
        }

        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.getLegend().setEnabled(true);
        pieChart.setCenterText("Thu Chi");
        pieChart.setCenterTextSize(14f);
        pieChart.setCenterTextColor(Color.parseColor("#263238"));
        pieChart.animateY(800);
        pieChart.invalidate();
    }
}