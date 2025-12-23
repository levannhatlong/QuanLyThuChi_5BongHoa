package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LienKetNganHangActivity extends AppCompatActivity {

    private ImageView btnBack;
    private MaterialCardView cardNganHang, cardEmpty;
    private TextView tvTenNganHang, tvSoTaiKhoan, tvChuTaiKhoan;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lien_ket_ngan_hang);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        setupListeners();
        loadData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        cardNganHang = findViewById(R.id.cardNganHang);
        cardEmpty = findViewById(R.id.cardEmpty);
        tvTenNganHang = findViewById(R.id.tvTenNganHang);
        tvSoTaiKhoan = findViewById(R.id.tvSoTaiKhoan);
        tvChuTaiKhoan = findViewById(R.id.tvChuTaiKhoan);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadData() {
        new Thread(() -> {
            try {
                Connection conn = DatabaseConnector.getConnection();
                if (conn != null) {
                    String sql = "SELECT TOP 1 TenNganHang, SoTaiKhoan, ChuTaiKhoan " +
                            "FROM LienKetNganHang WHERE MaNguoiDung = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String tenNganHang = rs.getString("TenNganHang");
                        String soTaiKhoan = rs.getString("SoTaiKhoan");
                        String chuTaiKhoan = rs.getString("ChuTaiKhoan");

                        runOnUiThread(() -> {
                            tvTenNganHang.setText(tenNganHang != null ? tenNganHang : "");
                            tvSoTaiKhoan.setText(soTaiKhoan != null ? soTaiKhoan : "");
                            tvChuTaiKhoan.setText(chuTaiKhoan != null ? chuTaiKhoan : "");

                            cardNganHang.setVisibility(View.VISIBLE);
                            cardEmpty.setVisibility(View.GONE);
                        });
                    } else {
                        runOnUiThread(() -> {
                            cardNganHang.setVisibility(View.GONE);
                            cardEmpty.setVisibility(View.VISIBLE);
                        });
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    cardNganHang.setVisibility(View.GONE);
                    cardEmpty.setVisibility(View.VISIBLE);
                });
            }
        }).start();
    }
}
