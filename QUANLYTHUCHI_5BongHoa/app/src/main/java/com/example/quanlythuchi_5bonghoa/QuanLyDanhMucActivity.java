package com.example.quanlythuchi_5bonghoa;

<<<<<<< HEAD
import android.content.Intent;
import android.graphics.Color;
=======
import android.content.SharedPreferences;
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
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
<<<<<<< HEAD

import com.google.android.material.appbar.AppBarLayout;
=======
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class QuanLyDanhMucActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tabChiTieu, tabThuNhap;
    private EditText edtSearch;
    private RecyclerView recyclerDanhMuc;
    private ExtendedFloatingActionButton fabThemDanhMuc;
    private LinearLayout emptyView;

    private DanhMucAdapter adapter;
    private List<DanhMuc> danhMucList = new ArrayList<>();
    private int userId;
    private boolean isChiTieu = true;
<<<<<<< HEAD
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
=======
    private String selectedIcon = "food.png";
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

<<<<<<< HEAD
        dbHelper = new DatabaseHelper(this);
=======
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
        initViews();
        setupListeners();
<<<<<<< HEAD
        setupRecyclerView();
        updateCategoryCount();
=======
        loadDanhMuc();
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabChiTieu = findViewById(R.id.tabChiTieu);
        tabThuNhap = findViewById(R.id.tabThuNhap);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerDanhMuc = findViewById(R.id.recyclerDanhMuc);
        fabThemDanhMuc = findViewById(R.id.fabThemDanhMuc);
        emptyView = findViewById(R.id.emptyView);

<<<<<<< HEAD
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
=======
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
        recyclerDanhMuc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DanhMucAdapter(this, danhMucList, new DanhMucAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(DanhMuc danhMuc, int position) {
<<<<<<< HEAD
                Intent intent = new Intent(QuanLyDanhMucActivity.this, ThemSuaDanhMucActivity.class);
                intent.putExtra("EDIT_MODE", true);
                intent.putExtra("CATEGORY_ID", danhMuc.getId());
                intent.putExtra("CATEGORY_NAME", danhMuc.getTen());
                intent.putExtra("CATEGORY_DESC", danhMuc.getMoTa());
                intent.putExtra("CATEGORY_ICON", danhMuc.getIconResId());
                intent.putExtra("CATEGORY_COLOR", danhMuc.getMauSac());
                intent.putExtra("IS_EXPENSE", danhMuc.isChiTieu());
                startActivityForResult(intent, 101);
=======
                showDialogThemSua(danhMuc);
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
            }

            @Override
            public void onDeleteClick(DanhMuc danhMuc, int position) {
                showDeleteConfirmDialog(danhMuc);
            }
        });
        recyclerDanhMuc.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tabChiTieu.setOnClickListener(v -> {
            isChiTieu = true;
            updateTabUI();
            loadDanhMuc();
        });

        tabThuNhap.setOnClickListener(v -> {
            isChiTieu = false;
            updateTabUI();
            loadDanhMuc();
        });

<<<<<<< HEAD
        fabThemDanhMuc.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyDanhMucActivity.this, ThemSuaDanhMucActivity.class);
            intent.putExtra("EDIT_MODE", false);
            startActivityForResult(intent, 100);
        });
=======
        fabThemDanhMuc.setOnClickListener(v -> showDialogThemSua(null));
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchDanhMuc(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateTabUI() {
        if (isChiTieu) {
            tabChiTieu.setBackgroundResource(R.drawable.bg_tab_selected);
            tabChiTieu.setTextColor(getResources().getColor(android.R.color.white));
            tabThuNhap.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabThuNhap.setTextColor(getResources().getColor(R.color.mau_chu_dao));
        } else {
            tabThuNhap.setBackgroundResource(R.drawable.bg_tab_selected);
            tabThuNhap.setTextColor(getResources().getColor(android.R.color.white));
            tabChiTieu.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabChiTieu.setTextColor(getResources().getColor(R.color.mau_chu_dao));
        }
    }

<<<<<<< HEAD
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
=======
    private void loadDanhMuc() {
        String loai = isChiTieu ? "Chi tiêu" : "Thu nhập";
        new Thread(() -> {
            List<DanhMuc> list = DanhMucRepository.getDanhMucByLoai(userId, loai);
            runOnUiThread(() -> {
                danhMucList.clear();
                danhMucList.addAll(list);
                adapter.updateData(danhMucList);
                updateEmptyView();
            });
        }).start();
    }

    private void searchDanhMuc(String keyword) {
        if (keyword.isEmpty()) {
            loadDanhMuc();
            return;
        }
        String loai = isChiTieu ? "Chi tiêu" : "Thu nhập";
        new Thread(() -> {
            List<DanhMuc> list = DanhMucRepository.searchDanhMuc(userId, loai, keyword);
            runOnUiThread(() -> {
                danhMucList.clear();
                danhMucList.addAll(list);
                adapter.updateData(danhMucList);
                updateEmptyView();
            });
        }).start();
    }

    private void updateEmptyView() {
        if (emptyView != null) {
            if (danhMucList.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerDanhMuc.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recyclerDanhMuc.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showDialogThemSua(DanhMuc danhMuc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_them_sua_danh_muc, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupLoai);
        RadioButton radioChiTieu = view.findViewById(R.id.radioChiTieu);
        RadioButton radioThuNhap = view.findViewById(R.id.radioThuNhap);
        EditText edtTen = view.findViewById(R.id.edtTenDanhMuc);
        EditText edtMoTa = view.findViewById(R.id.edtMoTa);
        LinearLayout layoutIcons = view.findViewById(R.id.layoutIcons);
        MaterialButton btnHuy = view.findViewById(R.id.btnHuy);
        MaterialButton btnLuu = view.findViewById(R.id.btnLuu);

        // Ẩn layout màu nếu có
        View layoutColors = view.findViewById(R.id.layoutColors);
        if (layoutColors != null) layoutColors.setVisibility(View.GONE);

        if (danhMuc != null) {
            tvTitle.setText("Sửa danh mục");
            edtTen.setText(danhMuc.getTenDanhMuc());
            edtMoTa.setText(danhMuc.getMoTa());
            selectedIcon = danhMuc.getBieuTuong();
            if (danhMuc.isChiTieu()) {
                radioChiTieu.setChecked(true);
            } else {
                radioThuNhap.setChecked(true);
            }
        } else {
            tvTitle.setText("Thêm danh mục mới");
            selectedIcon = "food.png";
            if (isChiTieu) {
                radioChiTieu.setChecked(true);
            } else {
                radioThuNhap.setChecked(true);
            }
        }

        setupIconSelection(layoutIcons);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLuu.setOnClickListener(v -> {
            String ten = edtTen.getText().toString().trim();
            String moTa = edtMoTa.getText().toString().trim();
            String loai = radioChiTieu.isChecked() ? "Chi tiêu" : "Thu nhập";

            if (ten.isEmpty()) {
                edtTen.setError("Vui lòng nhập tên danh mục");
                return;
            }

            new Thread(() -> {
                boolean success;
                if (danhMuc != null) {
                    success = DanhMucRepository.updateDanhMuc(danhMuc.getMaDanhMuc(), ten, loai, moTa, selectedIcon);
                } else {
                    success = DanhMucRepository.addDanhMuc(userId, ten, loai, moTa, selectedIcon);
                }

                runOnUiThread(() -> {
                    if (success) {
                        String msg = danhMuc != null ? "Đã cập nhật danh mục" : "Đã thêm danh mục mới";
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        loadDanhMuc();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "Lỗi khi lưu danh mục", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        dialog.show();
    }

    private void setupIconSelection(LinearLayout layoutIcons) {
        layoutIcons.removeAllViews();

        String[] icons = {"food.png", "car.png", "salary.png", "diet.png", "fashion.png", 
                          "pet.png", "edu.png", "bonus.png", "invest.png"};
        int[] iconRes = {R.drawable.ic_thuc_pham, R.drawable.ic_di_chuyen, R.drawable.ic_salary, 
                         R.drawable.ic_che_do_an, R.drawable.ic_thoi_trang, R.drawable.ic_thu_y,
                         R.drawable.ic_giao_duc, R.drawable.ic_tien_thuong, R.drawable.ic_dau_tu};

        for (int i = 0; i < icons.length; i++) {
            final String iconName = icons[i];
            final int iconDrawable = iconRes[i];

            ImageView iv = new ImageView(this);
            int size = (int) (56 * getResources().getDisplayMetrics().density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(6, 6, 6, 6);
            iv.setLayoutParams(params);
            iv.setImageResource(iconDrawable);
            iv.setPadding(12, 12, 12, 12);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setImageTintList(null); // Không áp dụng tint, giữ màu gốc

            // Highlight icon được chọn bằng viền
            if (iconName.equals(selectedIcon)) {
                iv.setBackgroundResource(R.drawable.category_item_bg);
            } else {
                iv.setBackgroundResource(R.drawable.bg_edit_text);
            }

            iv.setOnClickListener(v -> {
                selectedIcon = iconName;
                setupIconSelection(layoutIcons);
            });

            layoutIcons.addView(iv);
        }
    }

    private void showDeleteConfirmDialog(DanhMuc danhMuc) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc muốn xóa danh mục \"" + danhMuc.getTenDanhMuc() + "\"?\n\nLưu ý: Các giao dịch liên quan có thể bị ảnh hưởng.")
                .setPositiveButton("Xóa", (d, w) -> {
                    new Thread(() -> {
                        boolean success = DanhMucRepository.deleteDanhMuc(danhMuc.getMaDanhMuc());
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Đã xóa danh mục", Toast.LENGTH_SHORT).show();
                                loadDanhMuc();
                            } else {
                                Toast.makeText(this, "Không thể xóa danh mục này", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
<<<<<<< HEAD

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
=======
}
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
