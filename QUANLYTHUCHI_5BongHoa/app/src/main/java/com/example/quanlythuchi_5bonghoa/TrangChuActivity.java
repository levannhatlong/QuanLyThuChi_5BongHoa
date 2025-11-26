package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

public class TrangChuActivity extends AppCompatActivity {

    private TextView tvTienChi, tvTienThu;
    private LineChart lineChart;
    private LinearLayout btnViTien, btnThemGiaoDich, btnThongKe;
    private RecyclerView recyclerViewGiaoDich;
    private ImageView ivNotification, ivSettings, ivGhichu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        initViews();
        setupLineChart();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initViews() {
        tvTienChi = findViewById(R.id.tv_tien_chi);
        tvTienThu = findViewById(R.id.tv_tien_thu);
        lineChart = findViewById(R.id.line_chart);
        btnViTien = findViewById(R.id.btn_vi_tien);
        btnThemGiaoDich = findViewById(R.id.btn_them_giao_dich);
        btnThongKe = findViewById(R.id.btn_thong_ke);
        recyclerViewGiaoDich = findViewById(R.id.recycler_view_giao_dich);
        ivNotification = findViewById(R.id.iv_notification);
        ivSettings = findViewById(R.id.iv_settings);
        ivGhichu = findViewById(R.id.iv_ghichu);
    }

    private void setupLineChart() {
        // Giữ nguyên code
    }

    private void setupRecyclerView() {
        recyclerViewGiaoDich.setLayoutManager(new LinearLayoutManager(this));
        // Giữ nguyên code
    }

    private void setupClickListeners() {
        btnViTien.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, ViTienActivity.class);
            startActivity(intent);
        });

        btnThemGiaoDich.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, ThemGiaoDichActivity.class);
            startActivity(intent);
        });

        btnThongKe.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, ThongKeActivity.class);
            startActivity(intent);
        });

        ivNotification.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, ThongBaoActivity.class);
            startActivity(intent);
        });

        ivGhichu.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, GhiChuActivity.class);
            startActivity(intent);
        });

        ivSettings.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, CaiDatActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        // Giữ nguyên code
        tvTienChi.setText("5,300,000 VND");
        tvTienThu.setText("30,000,000 VND");
    }
}
