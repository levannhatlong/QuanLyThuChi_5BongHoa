package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class QuanLyDanhMucActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabChiTieu, tabThuNhap;
    private EditText edtSearch;
    private RecyclerView recyclerDanhMuc;
    private ExtendedFloatingActionButton fabThemDanhMuc;

    private DanhMucAdapter adapter;
    private List<DanhMuc> danhSachDanhMuc;
    private List<DanhMuc> danhSachGoc;
    private boolean isChiTieu = true;
    private DatabaseHelper dbHelper;

    // Biến lưu lựa chọn icon và màu trong dialog
    private int selectedIconResId = R.drawable.ic_food_modern;
    private String selectedColor = "#0EA5E9";

    // Danh sách icon và màu sắc hiện đại với màu cũ
    private final int[] iconList = {
            R.drawable.ic_food_modern,
            R.drawable.ic_transport_modern,
            R.drawable.ic_shopping_modern,
            R.drawable.ic_entertainment_modern,
            R.drawable.ic_bills_modern,
            R.drawable.ic_salary_modern,
            R.drawable.ic_investment_modern,
            R.drawable.ic_health_modern,
            R.drawable.ic_education_modern,
            R.drawable.ic_travel_modern
    };

    private final String[] colorList = {
            "#0EA5E9",  // Màu chủ đạo cũ
            "#E53935",  // Đỏ
            "#43A047",  // Xanh lá
            "#FB8C00",  // Cam
            "#8E24AA",  // Tím
            "#EC407A",  // Hồng
            "#1976D2",  // Xanh dương
            "#795548",  // Nâu
            "#607D8B",  // Xanh xám
            "#FF5722"   // Cam đậm
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadDanhMuc();
        setupListeners();
        setupRecyclerView();
        updateCategoryCount();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabChiTieu = findViewById(R.id.tabChiTieu);
        tabThuNhap = findViewById(R.id.tabThuNhap);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerDanhMuc = findViewById(R.id.recyclerDanhMuc);
        fabThemDanhMuc = findViewById(R.id.fabThemDanhMuc);
    }

    private void loadDanhMuc() {
        try {
            // Load từ database thay vì dữ liệu mẫu
            danhSachGoc = dbHelper.getAllCategories();
            filterByType();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải dữ liệu danh mục", Toast.LENGTH_SHORT).show();
            danhSachGoc = new ArrayList<>();
            filterByType();
        }
    }

    private void refreshData() {
        loadDanhMuc();
        updateCategoryCount();
    }

    private void filterByType() {
        danhSachDanhMuc = new ArrayList<>();
        for (DanhMuc dm : danhSachGoc) {
            if (dm.isChiTieu() == isChiTieu) {
                danhSachDanhMuc.add(dm);
            }
        }
        if (adapter != null) {
            adapter.updateData(danhSachDanhMuc);
        }
    }

    private void setupRecyclerView() {
        recyclerDanhMuc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DanhMucAdapter(this, danhSachDanhMuc, new DanhMucAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(DanhMuc danhMuc, int position) {
                Intent intent = new Intent(QuanLyDanhMucActivity.this, ThemSuaDanhMucActivity.class);
                intent.putExtra("EDIT_MODE", true);
                intent.putExtra("CATEGORY_ID", danhMuc.getId());
                intent.putExtra("CATEGORY_NAME", danhMuc.getTen());
                intent.putExtra("CATEGORY_DESC", danhMuc.getMoTa());
                intent.putExtra("CATEGORY_ICON", danhMuc.getIconResId());
                intent.putExtra("CATEGORY_COLOR", danhMuc.getMauSac());
                intent.putExtra("IS_EXPENSE", danhMuc.isChiTieu());
                startActivityForResult(intent, 101);
            }

            @Override
            public void onDeleteClick(DanhMuc danhMuc, int position) {
                showDeleteConfirmDialog(danhMuc, position);
            }
        });
        recyclerDanhMuc.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tabChiTieu.setOnClickListener(v -> {
            isChiTieu = true;
            updateTabUI();
            filterByType();
        });

        tabThuNhap.setOnClickListener(v -> {
            isChiTieu = false;
            updateTabUI();
            filterByType();
        });

        fabThemDanhMuc.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyDanhMucActivity.this, ThemSuaDanhMucActivity.class);
            intent.putExtra("EDIT_MODE", false);
            startActivityForResult(intent, 100);
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterByKeyword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateTabUI() {
        if (isChiTieu) {
            tabChiTieu.setBackgroundResource(R.drawable.bg_tab_active);
            tabChiTieu.setTextColor(getResources().getColor(android.R.color.white));
            tabThuNhap.setBackgroundResource(0);
            tabThuNhap.setTextColor(getResources().getColor(R.color.mau_chu_phu));
        } else {
            tabThuNhap.setBackgroundResource(R.drawable.bg_tab_active);
            tabThuNhap.setTextColor(getResources().getColor(android.R.color.white));
            tabChiTieu.setBackgroundResource(0);
            tabChiTieu.setTextColor(getResources().getColor(R.color.mau_chu_phu));
        }
    }

    private void filterByKeyword(String keyword) {
        try {
            if (keyword.isEmpty()) {
                filterByType();
            } else {
                danhSachDanhMuc = dbHelper.searchCategories(keyword, isChiTieu);
                adapter.updateData(danhSachDanhMuc);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tìm kiếm", Toast.LENGTH_SHORT).show();
            filterByType(); // Fallback to showing all categories of current type
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("CATEGORY_NAME");
            String desc = data.getStringExtra("CATEGORY_DESC");
            int iconRes = data.getIntExtra("CATEGORY_ICON", R.drawable.ic_food_modern);
            String color = data.getStringExtra("CATEGORY_COLOR");
            boolean isExpense = data.getBooleanExtra("IS_EXPENSE", true);
            
            if (requestCode == 100) {
                // Add new category to database
                long newId = dbHelper.addCategory(name, desc, iconRes, color, isExpense);
                if (newId > 0) {
                    Toast.makeText(this, "Đã thêm danh mục mới", Toast.LENGTH_SHORT).show();
                    refreshData();
                } else {
                    Toast.makeText(this, "Lỗi khi thêm danh mục", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 101) {
                // Edit existing category in database
                int categoryId = data.getIntExtra("CATEGORY_ID", -1);
                int rowsAffected = dbHelper.updateCategory(categoryId, name, desc, iconRes, color, isExpense);
                if (rowsAffected > 0) {
                    Toast.makeText(this, "Đã cập nhật danh mục", Toast.LENGTH_SHORT).show();
                    refreshData();
                } else {
                    Toast.makeText(this, "Lỗi khi cập nhật danh mục", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showDeleteConfirmDialog(DanhMuc danhMuc, int position) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa danh mục \"" + danhMuc.getTen() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    int rowsAffected = dbHelper.deleteCategory(danhMuc.getId());
                    if (rowsAffected > 0) {
                        Toast.makeText(this, "Đã xóa danh mục", Toast.LENGTH_SHORT).show();
                        refreshData();
                    } else {
                        Toast.makeText(this, "Lỗi khi xóa danh mục", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updateCategoryCount() {
        try {
            TextView tvExpenseCount = findViewById(R.id.tvExpenseCount);
            TextView tvIncomeCount = findViewById(R.id.tvIncomeCount);
            
            int expenseCount = dbHelper.getCategoryCount(true);
            int incomeCount = dbHelper.getCategoryCount(false);
            
            tvExpenseCount.setText(String.valueOf(expenseCount));
            tvIncomeCount.setText(String.valueOf(incomeCount));
        } catch (Exception e) {
            // Nếu có lỗi, hiển thị 0
            TextView tvExpenseCount = findViewById(R.id.tvExpenseCount);
            TextView tvIncomeCount = findViewById(R.id.tvIncomeCount);
            tvExpenseCount.setText("0");
            tvIncomeCount.setText("0");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        refreshData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // Inner class DanhMuc
    public static class DanhMuc {
        private int id;
        private String ten;
        private String moTa;
        private int iconResId;
        private String mauSac;
        private boolean isChiTieu;

        public DanhMuc(int id, String ten, String moTa, int iconResId, String mauSac, boolean isChiTieu) {
            this.id = id;
            this.ten = ten;
            this.moTa = moTa;
            this.iconResId = iconResId;
            this.mauSac = mauSac;
            this.isChiTieu = isChiTieu;
        }

        // Getters and Setters
        public int getId() { return id; }
        public String getTen() { return ten; }
        public void setTen(String ten) { this.ten = ten; }
        public String getMoTa() { return moTa; }
        public void setMoTa(String moTa) { this.moTa = moTa; }
        public int getIconResId() { return iconResId; }
        public void setIconResId(int iconResId) { this.iconResId = iconResId; }
        public String getMauSac() { return mauSac; }
        public void setMauSac(String mauSac) { this.mauSac = mauSac; }
        public boolean isChiTieu() { return isChiTieu; }
        public void setChiTieu(boolean chiTieu) { isChiTieu = chiTieu; }
    }
}