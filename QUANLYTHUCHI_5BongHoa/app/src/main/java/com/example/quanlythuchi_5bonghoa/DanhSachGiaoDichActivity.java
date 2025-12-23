package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DanhSachGiaoDichActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTongThu, tvTongChi, tvSoDu, tvSoLuong;
    private RecyclerView rvGiaoDich;
    private LinearLayout layoutEmpty;

    private GiaoDichAdapter adapter;
    private List<GiaoDich> listGiaoDich = new ArrayList<>();
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_giao_dich);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "Lỗi: không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        setupListeners();
        loadData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTongThu = findViewById(R.id.tvTongThu);
        tvTongChi = findViewById(R.id.tvTongChi);
        tvSoDu = findViewById(R.id.tvSoDu);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        rvGiaoDich = findViewById(R.id.rvGiaoDich);
        layoutEmpty = findViewById(R.id.layoutEmpty);
    }

    private void setupRecyclerView() {
        adapter = new GiaoDichAdapter(this, listGiaoDich);
        rvGiaoDich.setLayoutManager(new LinearLayoutManager(this));
        rvGiaoDich.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadData() {
        new Thread(() -> {
            Connection conn = DatabaseConnector.getConnection();
            if (conn == null) {
                runOnUiThread(() -> Toast.makeText(this, "Không thể kết nối database", Toast.LENGTH_SHORT).show());
                return;
            }

            List<GiaoDich> transactions = new ArrayList<>();
            double totalIncome = 0;
            double totalExpense = 0;

            try {
                String query = "SELECT g.TenGiaoDich, g.SoTien, g.NgayGiaoDich, " +
                        "d.TenDanhMuc, d.LoaiDanhMuc, d.BieuTuong " +
                        "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                        "WHERE g.MaNguoiDung = ? ORDER BY g.NgayGiaoDich DESC";

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String tenGiaoDich = rs.getString("TenGiaoDich");
                    double soTien = rs.getDouble("SoTien");
                    Date ngayGiaoDich = rs.getTimestamp("NgayGiaoDich");
                    String tenDanhMuc = rs.getString("TenDanhMuc");
                    String loaiDanhMuc = rs.getString("LoaiDanhMuc");
                    String bieuTuong = rs.getString("BieuTuong");

                    transactions.add(new GiaoDich(tenGiaoDich, soTien, ngayGiaoDich, 
                            tenDanhMuc, loaiDanhMuc, bieuTuong));

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
                runOnUiThread(() -> Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show());
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            double finalTotalIncome = totalIncome;
            double finalTotalExpense = totalExpense;

            runOnUiThread(() -> {
                updateUI(transactions, finalTotalIncome, finalTotalExpense);
            });
        }).start();
    }

    private void updateUI(List<GiaoDich> transactions, double income, double expense) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        tvTongThu.setText(formatter.format(income));
        tvTongChi.setText(formatter.format(expense));
        tvSoDu.setText(formatter.format(income - expense));
        tvSoLuong.setText("Tổng: " + transactions.size() + " giao dịch");

        listGiaoDich.clear();
        listGiaoDich.addAll(transactions);
        adapter.notifyDataSetChanged();

        if (transactions.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvGiaoDich.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvGiaoDich.setVisibility(View.VISIBLE);
        }
    }
}
