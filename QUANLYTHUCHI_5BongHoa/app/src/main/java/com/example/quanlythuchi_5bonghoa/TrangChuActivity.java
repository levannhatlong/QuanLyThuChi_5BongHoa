package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class TrangChuActivity extends AppCompatActivity {

    private TextView tvTienChi, tvTienThu;
    private LineChart lineChart;
    private LinearLayout btnViTien, btnThemGiaoDich, btnThongKe;
    private RecyclerView recyclerViewGiaoDich;
    private ImageView ivNotification, ivSettings, ivGhichu;
    private GiaoDichAdapter giaoDichAdapter;
    private List<GiaoDich> danhSachGiaoDich;

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
        // Dữ liệu mẫu cho Thu
        ArrayList<Entry> incomeEntries = new ArrayList<>();
        incomeEntries.add(new Entry(0, 10));
        incomeEntries.add(new Entry(1, 12));
        incomeEntries.add(new Entry(2, 8));
        incomeEntries.add(new Entry(3, 15));
        incomeEntries.add(new Entry(4, 11));

        LineDataSet incomeDataSet = new LineDataSet(incomeEntries, "Thu");
        incomeDataSet.setColor(Color.parseColor("#43A047")); // Màu xanh lá
        incomeDataSet.setLineWidth(2f);
        incomeDataSet.setCircleColor(Color.parseColor("#43A047"));
        incomeDataSet.setDrawValues(false);

        // Dữ liệu mẫu cho Chi
        ArrayList<Entry> expenseEntries = new ArrayList<>();
        expenseEntries.add(new Entry(0, 5));
        expenseEntries.add(new Entry(1, 7));
        expenseEntries.add(new Entry(2, 6));
        expenseEntries.add(new Entry(3, 9));
        expenseEntries.add(new Entry(4, 4));

        LineDataSet expenseDataSet = new LineDataSet(expenseEntries, "Chi");
        expenseDataSet.setColor(Color.parseColor("#E53935")); // Màu đỏ
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

        // Tùy chỉnh trục X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        // Tùy chỉnh trục Y
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.invalidate(); // refresh
    }

    private void setupRecyclerView() {
        recyclerViewGiaoDich.setLayoutManager(new LinearLayoutManager(this));
        danhSachGiaoDich = new ArrayList<>();
        // Thêm dữ liệu mẫu
        danhSachGiaoDich.add(new GiaoDich("Ăn uống", 200000, false));
        danhSachGiaoDich.add(new GiaoDich("Lương", 30000000, true));
        danhSachGiaoDich.add(new GiaoDich("Mua sắm", 500000, false));
        giaoDichAdapter = new GiaoDichAdapter(this, danhSachGiaoDich);
        recyclerViewGiaoDich.setAdapter(giaoDichAdapter);
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
        // Dữ liệu sẽ được tải từ database ở đây
        // Tạm thời tính toán từ dữ liệu mẫu
        double tongThu = 0;
        double tongChi = 0;
        for (GiaoDich gd : danhSachGiaoDich) {
            if (gd.isTienVao()) {
                tongThu += gd.getSoTien();
            } else {
                tongChi += gd.getSoTien();
            }
        }
        tvTienThu.setText(String.format("%,.0f VND", tongThu));
        tvTienChi.setText(String.format("%,.0f VND", tongChi));
    }
}
