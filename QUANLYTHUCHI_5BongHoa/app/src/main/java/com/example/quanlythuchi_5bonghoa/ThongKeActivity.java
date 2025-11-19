package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    private PieChart pieChart;
    private RecyclerView recyclerView;
    private FloatingActionButton fabHome;
    private GiaoDichAdapter giaoDichAdapter;
    private List<GiaoDich> danhSachGiaoDich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        pieChart = findViewById(R.id.piechart);
        recyclerView = findViewById(R.id.recycler_view_giao_dich);
        fabHome = findViewById(R.id.fab_home);

        setupPieChart();
        setupRecyclerView();

        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongKeActivity.this, TrangChuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); 
            }
        });
    }

    private void setupPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(5000000, "Tổng chi"));
        entries.add(new PieEntry(30000000, "Tổng thu"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{Color.RED, Color.GREEN});
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate(); // refresh
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        danhSachGiaoDich = new ArrayList<>();
        // Thêm dữ liệu mẫu
        danhSachGiaoDich.add(new GiaoDich("Ăn uống", 200000, false));
        danhSachGiaoDich.add(new GiaoDich("Lương", 30000000, true));
        danhSachGiaoDich.add(new GiaoDich("Mua sắm", 500000, false));
        danhSachGiaoDich.add(new GiaoDich("Đi lại", 150000, false));
        danhSachGiaoDich.add(new GiaoDich("Bán đồ cũ", 500000, true));

        giaoDichAdapter = new GiaoDichAdapter(this, danhSachGiaoDich);
        recyclerView.setAdapter(giaoDichAdapter);
    }
}
