package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.DecimalFormat;
import java.util.Locale;

public class ViTienActivity extends AppCompatActivity {

    private TextView tvSoDu, tvThuNhap, tvChiTieu;
    private ProgressBar progressBalance;
    private ImageView ivToggleBalance;
    private FloatingActionButton fabHome;

    private double tongThu = 0, tongChi = 0, soDu = 0;
    private boolean isBalanceVisible = true;
    private int currentUserId;
    private GiaoDichRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_tien);

        currentUserId = getIntent().getIntExtra("USER_ID", 1);
        repository = new GiaoDichRepository();

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        tvSoDu = findViewById(R.id.tv_so_du);
        tvThuNhap = findViewById(R.id.tv_thu_nhap);
        tvChiTieu = findViewById(R.id.tv_chi_tieu);
        progressBalance = findViewById(R.id.progress_balance);
        ivToggleBalance = findViewById(R.id.iv_toggle_balance);
        fabHome = findViewById(R.id.fab_home);
    }

    private void loadDataFromDatabase() {
        new Thread(() -> {
            double[] data = repository.layTongThuChi(currentUserId);
            tongThu = data[0];
            tongChi = data[1];
            soDu = tongThu - tongChi;

            runOnUiThread(() -> {
                displayData();
                updateProgressBar();
            });
        }).start();
    }

    private void displayData() {
        DecimalFormat df = new DecimalFormat("#,### VND");

        if (isBalanceVisible) {
            tvSoDu.setText(df.format(soDu));
        } else {
            tvSoDu.setText("*** *** VND");
        }

        tvThuNhap.setText("+" + df.format(tongThu));
        tvChiTieu.setText("-" + df.format(tongChi));
    }

    private void updateProgressBar() {
        double tongGiaoDich = tongThu + tongChi;
        if (tongGiaoDich > 0) {
            int percentThu = (int) ((tongThu * 100) / tongGiaoDich);
            progressBalance.setProgress(percentThu);
        } else {
            progressBalance.setProgress(50);
        }
    }

    private void setupClickListeners() {
        fabHome.setOnClickListener(v -> finish()); // Quay về Trang chủ

        ivToggleBalance.setOnClickListener(v -> {
            isBalanceVisible = !isBalanceVisible;
            displayData();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDatabase();
    }
}