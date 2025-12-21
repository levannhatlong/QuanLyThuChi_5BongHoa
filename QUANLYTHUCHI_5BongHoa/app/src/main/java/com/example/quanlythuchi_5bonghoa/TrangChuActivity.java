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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TrangChuActivity extends AppCompatActivity {

    private TextView tvTienChi, tvTienThu;
    private RecyclerView recyclerViewGiaoDich;
    private GiaoDichAdapter giaoDichAdapter;
    private List<GiaoDich> danhSachGiaoDich;
    private GiaoDichRepository repository;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        // Giả sử nhận ID từ màn hình đăng nhập, mặc định là 1 để test
        currentUserId = getIntent().getIntExtra("USER_ID", 1);
        repository = new GiaoDichRepository();

        initViews();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initViews() {
        tvTienChi = findViewById(R.id.tv_tien_chi);
        tvTienThu = findViewById(R.id.tv_tien_thu);
        recyclerViewGiaoDich = findViewById(R.id.recycler_view_giao_dich);
    }

    private void setupRecyclerView() {
        recyclerViewGiaoDich.setLayoutManager(new LinearLayoutManager(this));
        danhSachGiaoDich = new ArrayList<>();
        giaoDichAdapter = new GiaoDichAdapter(this, danhSachGiaoDich);
        recyclerViewGiaoDich.setAdapter(giaoDichAdapter);
    }

    private void loadDataFromSQL() {
        new Thread(() -> {
            // Lấy tổng thu chi
            double[] totals = repository.layTongThuChi(currentUserId);
            // Lấy list giao dịch
            List<GiaoDich> list = repository.layGiaoDichGanDay(currentUserId);

            runOnUiThread(() -> {
                DecimalFormat df = new DecimalFormat("#,### VND");
                tvTienThu.setText(df.format(totals[0]));
                tvTienChi.setText(df.format(totals[1]));

                danhSachGiaoDich.clear();
                danhSachGiaoDich.addAll(list);
                giaoDichAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void setupClickListeners() {
        findViewById(R.id.btn_vi_tien).setOnClickListener(v -> {
            Intent intent = new Intent(this, ViTienActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });
        // Thêm các click listener khác cho btnThemGiaoDich, btnThongKe...
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromSQL();
    }
}