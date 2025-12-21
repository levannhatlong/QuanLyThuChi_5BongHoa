package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViTienActivity extends AppCompatActivity {

    private TextView tvSoDu, tvThuNhap, tvChiTieu;
    private ProgressBar progressBalance;
    private RecyclerView recyclerViewDanhMuc;
    private FloatingActionButton fabHome;
    private ImageView ivToggleBalance;

    private long tongThuNhap = 30000000;
    private long tongChiTieu = 5100000;
    private long soDu;
    private boolean isBalanceVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_tien);

        // Khởi tạo views
        initViews();

        // Tính toán số dư
        calculateBalance();

        // Hiển thị dữ liệu
        displayData();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup click listeners
        setupClickListeners();
    }

    private void initViews() {
        tvSoDu = findViewById(R.id.tv_so_du);
        tvThuNhap = findViewById(R.id.tv_thu_nhap);
        tvChiTieu = findViewById(R.id.tv_chi_tieu);
        progressBalance = findViewById(R.id.progress_balance);
        ivToggleBalance = findViewById(R.id.iv_toggle_balance);
        // recyclerViewDanhMuc = findViewById(R.id.recycler_view_danh_muc); // Tạm thời comment
        fabHome = findViewById(R.id.fab_home);
    }

    private void calculateBalance() {
        soDu = tongThuNhap - tongChiTieu;

        // Tính phần trăm cho progress bar
        long tongGiaoDich = tongThuNhap + tongChiTieu;
        if (tongGiaoDich > 0) {
            int percentThuNhap = (int) ((tongThuNhap * 100) / tongGiaoDich);
            progressBalance.setProgress(percentThuNhap);
        }
    }

    

    private List<DanhMuc> createSampleData() {
        List<DanhMuc> danhSach = new ArrayList<>();

        danhSach.add(new DanhMuc("Ăn uống", -100000, R.drawable.ic_food, false));
        danhSach.add(new DanhMuc("Du lịch", -5000000, R.drawable.ic_travel, false));
        danhSach.add(new DanhMuc("Tiền lương", 30000000, R.drawable.ic_salary, true));
        danhSach.add(new DanhMuc("Đi chuyển", -20000, R.drawable.ic_transport, false));
        danhSach.add(new DanhMuc("Hóa đơn nước", -300000, R.drawable.ic_water, false));

        return danhSach;
    }

    private void setupClickListeners() {
        fabHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViTienActivity.this, TrangChuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        ivToggleBalance.setOnClickListener(v -> {
            isBalanceVisible = !isBalanceVisible;
            displayData();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật dữ liệu khi quay lại màn hình
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        // TODO: Load dữ liệu từ database
        // Tạm thời sử dụng dữ liệu mẫu
        calculateBalance();
        displayData();
    }

    // Inner class để lưu thông tin danh mục
    public static class DanhMuc {
        private String ten;
        private long soTien;
        private int iconResId;
        private boolean laThuNhap;

        public DanhMuc(String ten, long soTien, int iconResId, boolean laThuNhap) {
            this.ten = ten;
            this.soTien = soTien;
            this.iconResId = iconResId;
            this.laThuNhap = laThuNhap;
        }

        public String getTen() {
            return ten;
        }

        public long getSoTien() {
            return soTien;
        }

        public int getIconResId() {
            return iconResId;
        }

        public boolean isLaThuNhap() {
            return laThuNhap;
        }
    }
}