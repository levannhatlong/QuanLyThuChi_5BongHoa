package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class TrangChuActivity extends AppCompatActivity {

    private TextView tvTienChi, tvTienThu;
    private LineChart lineChart;
    private LinearLayout btnViTien, btnThemGiaoDich, btnThongKe;
    private RecyclerView recyclerViewGiaoDich;
    private ImageView ivNotification, ivSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        // Khởi tạo views
        initViews();

        // Setup biểu đồ
        setupLineChart();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup click listeners
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
    }

    private void setupLineChart() {
        // (Giữ nguyên code setup biểu đồ của bạn)
    }

    private void setupRecyclerView() {
        recyclerViewGiaoDich.setLayoutManager(new LinearLayoutManager(this));
        // (Giữ nguyên code setup RecyclerView của bạn)
    }

    private void setupClickListeners() {
        // Nút Ví tiền
        btnViTien.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, ViTienActivity.class);
            startActivity(intent);
        });

        // Nút Thêm giao dịch
        btnThemGiaoDich.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, ThemGiaoDichActivity.class);
            startActivity(intent);
        });

        // Nút Thống kê
        btnThongKe.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, ThongKeActivity.class);
            startActivity(intent);
        });

        // Nút thông báo
        ivNotification.setOnClickListener(v -> {
            // TODO: Xử lý thông báo
        });

        // Nút cài đặt
        ivSettings.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, CaiDatActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật dữ liệu khi quay lại màn hình
        loadData();
    }

    private void loadData() {
        // TODO: Load dữ liệu từ database
        tvTienChi.setText("5,300,000 VND");
        tvTienThu.setText("30,000,000 VND");
    }
}
