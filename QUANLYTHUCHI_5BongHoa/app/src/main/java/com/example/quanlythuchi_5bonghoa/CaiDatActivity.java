package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class CaiDatActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout layoutNgonNgu;
    private LinearLayout layoutQuanLyTheLoai;
    private LinearLayout layoutLienKetNganHang;
    private LinearLayout layoutTaiKhoan;
    private LinearLayout layoutXuatDuLieu;
    private LinearLayout layoutDatCanhBao;
    private SwitchCompat switchCanhBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat);

        initViews();
        loadSettings();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        layoutNgonNgu = findViewById(R.id.layoutNgonNgu);
        layoutQuanLyTheLoai = findViewById(R.id.layoutQuanLyTheLoai);
        layoutLienKetNganHang = findViewById(R.id.layoutLienKetNganHang);
        layoutTaiKhoan = findViewById(R.id.layoutTaiKhoan);
        layoutXuatDuLieu = findViewById(R.id.layoutXuatDuLieu);
        layoutDatCanhBao = findViewById(R.id.layoutDatCanhBao);
        switchCanhBao = findViewById(R.id.switchCanhBao);
    }

    private void loadSettings() {
        boolean canhBaoEnabled = getSharedPreferences("AppSettings", MODE_PRIVATE)
                .getBoolean("canh_bao_chi_tieu", true);
        switchCanhBao.setChecked(canhBaoEnabled);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Mở trang Ngôn ngữ
        layoutNgonNgu.setOnClickListener(v -> {
            Intent intent = new Intent(this, NgonNguActivity.class);
            startActivity(intent);
        });

        // Mở trang Quản lý danh mục
        layoutQuanLyTheLoai.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuanLyDanhMucActivity.class);
            startActivity(intent);
        });

        // Tài khoản của tôi
        layoutTaiKhoan.setOnClickListener(v -> {
            Intent intent = new Intent(this, TaiKhoanActivity.class);
            startActivity(intent);
        });

        // Liên kết ngân hàng
        layoutLienKetNganHang.setOnClickListener(v -> {
            Intent intent = new Intent(this, LienKetNganHangActivity.class);
            startActivity(intent);
        });

        // Xuất dữ liệu
        layoutXuatDuLieu.setOnClickListener(v -> {
            Intent intent = new Intent(this, XuatDuLieuActivity.class);
            startActivity(intent);
        });

        // Đặt cảnh báo chi tiêu
        layoutDatCanhBao.setOnClickListener(v -> {
            Intent intent = new Intent(this, CaiDatCanhBao.class);
            startActivity(intent);
        });


        // Switch cảnh báo chi tiêu
        switchCanhBao.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences("AppSettings", MODE_PRIVATE)
                    .edit()
                    .putBoolean("canh_bao_chi_tieu", isChecked)
                    .apply();
            
            String msg = isChecked ? "Đã bật cảnh báo chi tiêu" : "Đã tắt cảnh báo chi tiêu";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });
    }
}
