package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class CaiDatActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout layoutNgonNgu;
    private LinearLayout layoutQuanLyTheLoai;
    private LinearLayout layoutLienKetNganHang;
    private LinearLayout layoutTaiKhoan;
    private LinearLayout layoutXuatDuLieu;
    private SwitchCompat switchCanhBao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        initViews();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        layoutNgonNgu = findViewById(R.id.layoutNgonNgu);
        layoutQuanLyTheLoai = findViewById(R.id.layoutQuanLyTheLoai);
        layoutLienKetNganHang = findViewById(R.id.layoutLienKetNganHang);
        layoutTaiKhoan = findViewById(R.id.layoutTaiKhoan);
        layoutXuatDuLieu = findViewById(R.id.layoutXuatDuLieu);
        switchCanhBao = findViewById(R.id.switchCanhBao);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Mở trang Ngôn ngữ
        layoutNgonNgu.setOnClickListener(v -> {
            Intent intent = new Intent(CaiDatActivity.this, NgonNguActivity.class);
            startActivity(intent);
        });
//
        // Mở trang Quản lý danh mục
        layoutQuanLyTheLoai.setOnClickListener(v -> {
            Intent intent = new Intent(CaiDatActivity.this, QuanLyDanhMucActivity.class);
            startActivity(intent);
        });
//
//        // Liên kết ngân hàng
//        layoutLienKetNganHang.setOnClickListener(v -> {
//            // TODO: Implement bank linking feature
//        });
//
//        // Tài khoản của tôi
//        layoutTaiKhoan.setOnClickListener(v -> {
//            Intent intent = new Intent(CaiDatActivity.this, TaiKhoanActivity.class);
//            startActivity(intent);
//        });
//
//        // Xuất dữ liệu
//        layoutXuatDuLieu.setOnClickListener(v -> {
//            // TODO: Implement data export feature
//        });
//
//        // Switch cảnh báo chi tiêu
//        switchCanhBao.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            // TODO: Save setting to SharedPreferences
//            getSharedPreferences("AppSettings", MODE_PRIVATE)
//                    .edit()
//                    .putBoolean("canh_bao_chi_tieu", isChecked)
//                    .apply();
//        });
    }
}
