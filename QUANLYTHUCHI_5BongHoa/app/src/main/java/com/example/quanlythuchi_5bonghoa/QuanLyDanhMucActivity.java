package com.example.quanlythuchi_5bonghoa;

<<<<<<< HEAD
import android.graphics.Color;
=======

import android.content.SharedPreferences;

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
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
    // Biến lưu lựa chọn icon và màu trong dialog
    private int selectedIconResId = R.drawable.ic_food;
    private String selectedColor = "#1976D2";

    // Danh sách icon và màu sắc
    private final int[] iconList = {
            R.drawable.ic_food,
            R.drawable.ic_travel,
            R.drawable.ic_salary,
            R.drawable.ic_wallet,
            R.drawable.ic_chart,
            R.drawable.ic_settings
    };

    private final String[] colorList = {
            "#1976D2",  // Xanh dương
            "#E53935",  // Đỏ
            "#43A047",  // Xanh lá
            "#FB8C00",  // Cam
            "#8E24AA",  // Tím
            "#EC407A"   // Hồng
    };
=======
    private String selectedIcon = "food.png";

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

<<<<<<< HEAD
=======

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
        initViews();
        setupListeners();
<<<<<<< HEAD
        setupRecyclerView();
=======

        loadDanhMuc();

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabChiTieu = findViewById(R.id.tabChiTieu);
        tabThuNhap = findViewById(R.id.tabThuNhap);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerDanhMuc = findViewById(R.id.recyclerDanhMuc);
        fabThemDanhMuc = findViewById(R.id.fabThemDanhMuc);

    private void loadDanhMuc() {
        danhSachGoc = new ArrayList<>();
        // Danh mục chi tiêu mẫu
        danhSachGoc.add(new DanhMuc(1, "Ăn uống", "Chi phí ăn uống hàng ngày", R.drawable.ic_food, "#E53935", true));
        danhSachGoc.add(new DanhMuc(2, "Di chuyển", "Chi phí đi lại, xăng xe", R.drawable.ic_travel, "#FB8C00", true));
        danhSachGoc.add(new DanhMuc(3, "Mua sắm", "Chi phí mua sắm", R.drawable.ic_wallet, "#8E24AA", true));
        danhSachGoc.add(new DanhMuc(4, "Giải trí", "Chi phí giải trí", R.drawable.ic_chart, "#1976D2", true));
        danhSachGoc.add(new DanhMuc(5, "Hóa đơn", "Điện, nước, internet", R.drawable.ic_settings, "#43A047", true));
        // Danh mục thu nhập mẫu
        danhSachGoc.add(new DanhMuc(6, "Tiền lương", "Lương hàng tháng", R.drawable.ic_salary, "#43A047", false));
        danhSachGoc.add(new DanhMuc(7, "Tiền thưởng", "Thưởng, bonus", R.drawable.ic_salary, "#1976D2", false));
        danhSachGoc.add(new DanhMuc(8, "Đầu tư", "Lợi nhuận đầu tư", R.drawable.ic_chart, "#FB8C00", false));
<<<<<<< HEAD

        filterByType();
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

        filterByType();
    }

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
        recyclerDanhMuc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DanhMucAdapter(this, danhMucList, new DanhMucAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(DanhMuc danhMuc, int position) {
<<<<<<< HEAD
                showDialogThemSua(danhMuc, position);
=======

                showDialogThemSua(danhMuc);

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
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
        fabThemDanhMuc.setOnClickListener(v -> showDialogThemSua(null, -1));
=======

        fabThemDanhMuc.setOnClickListener(v -> showDialogThemSua(null));

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f

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
        List<DanhMuc> filtered = new ArrayList<>();
        for (DanhMuc dm : danhSachGoc) {
            if (dm.isChiTieu() == isChiTieu &&
                    dm.getTen().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(dm);
            }
        }
        adapter.updateData(filtered);
    }

    private void showDialogThemSua(DanhMuc danhMuc, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_them_sua_danh_muc, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroupLoai);
        RadioButton radioChiTieu = view.findViewById(R.id.radioChiTieu);
        RadioButton radioThuNhap = view.findViewById(R.id.radioThuNhap);
        EditText edtTen = view.findViewById(R.id.edtTenDanhMuc);
        EditText edtMoTa = view.findViewById(R.id.edtMoTa);
        LinearLayout layoutIcons = view.findViewById(R.id.layoutIcons);
        LinearLayout layoutColors = view.findViewById(R.id.layoutColors);
        MaterialButton btnHuy = view.findViewById(R.id.btnHuy);
        MaterialButton btnLuu = view.findViewById(R.id.btnLuu);

        // Khởi tạo giá trị mặc định
        if (danhMuc != null) {
            tvTitle.setText("Sửa danh mục");
            edtTen.setText(danhMuc.getTen());
            edtMoTa.setText(danhMuc.getMoTa());
            selectedIconResId = danhMuc.getIconResId();
            selectedColor = danhMuc.getMauSac();
            if (danhMuc.isChiTieu()) {
                radioChiTieu.setChecked(true);
            } else {
                radioThuNhap.setChecked(true);
            }
        } else {
            tvTitle.setText("Thêm danh mục mới");
            selectedIconResId = R.drawable.ic_food;
            selectedColor = "#1976D2";
            if (isChiTieu) {
                radioChiTieu.setChecked(true);
            } else {
                radioThuNhap.setChecked(true);
            }
        }

        // Setup icon selection
        setupIconSelection(layoutIcons);

        // Setup color selection
        setupColorSelection(layoutColors);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        btnLuu.setOnClickListener(v -> {
            String ten = edtTen.getText().toString().trim();
            String moTa = edtMoTa.getText().toString().trim();
            boolean loaiChiTieu = radioChiTieu.isChecked();

            if (ten.isEmpty()) {
                edtTen.setError("Vui lòng nhập tên danh mục");
                return;
            }

            if (danhMuc != null) {
                // Cập nhật
                danhMuc.setTen(ten);
                danhMuc.setMoTa(moTa);
                danhMuc.setChiTieu(loaiChiTieu);
                danhMuc.setIconResId(selectedIconResId);
                danhMuc.setMauSac(selectedColor);

                // Cập nhật lại list
                filterByType();
                Toast.makeText(this, "Đã cập nhật danh mục", Toast.LENGTH_SHORT).show();
            } else {
                // Thêm mới
                int newId = danhSachGoc.size() + 1;
                DanhMuc newDM = new DanhMuc(newId, ten, moTa, selectedIconResId, selectedColor, loaiChiTieu);
                danhSachGoc.add(newDM);
                filterByType();
                Toast.makeText(this, "Đã thêm danh mục mới", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setupIconSelection(LinearLayout layoutIcons) {
        layoutIcons.removeAllViews();

        for (int i = 0; i < iconList.length; i++) {
            final int iconRes = iconList[i];

            View iconContainer = LayoutInflater.from(this).inflate(R.layout.item_icon_selector, layoutIcons, false);
            MaterialCardView cardIcon = iconContainer.findViewById(R.id.cardIconSelector);
            ImageView ivIcon = iconContainer.findViewById(R.id.ivIconSelector);

            ivIcon.setImageResource(iconRes);

            // Đánh dấu icon đã chọn
            if (iconRes == selectedIconResId) {
                cardIcon.setCardBackgroundColor(Color.parseColor("#E3F2FD"));
                cardIcon.setStrokeColor(Color.parseColor("#1976D2"));
                cardIcon.setStrokeWidth(4);
                ivIcon.setColorFilter(Color.parseColor("#1976D2"));
            } else {
                cardIcon.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                cardIcon.setStrokeWidth(0);
                ivIcon.setColorFilter(Color.parseColor("#546E7A"));
            }

            // Click listener
            cardIcon.setOnClickListener(v -> {
                selectedIconResId = iconRes;
                setupIconSelection(layoutIcons); // Refresh UI
            });

            layoutIcons.addView(iconContainer);
        }
    }

    private void setupColorSelection(LinearLayout layoutColors) {
        layoutColors.removeAllViews();

        for (int i = 0; i < colorList.length; i++) {
            final String color = colorList[i];

            View colorContainer = LayoutInflater.from(this).inflate(R.layout.item_color_selector, layoutColors, false);
            MaterialCardView cardColor = colorContainer.findViewById(R.id.cardColorSelector);

            cardColor.setCardBackgroundColor(Color.parseColor(color));

            // Đánh dấu màu đã chọn
            if (color.equals(selectedColor)) {
                cardColor.setStrokeColor(Color.parseColor("#263238"));
                cardColor.setStrokeWidth(6);
            } else {
                cardColor.setStrokeWidth(0);
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
>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
            }

            // Click listener
            cardColor.setOnClickListener(v -> {
                selectedColor = color;
                setupColorSelection(layoutColors); // Refresh UI
            });

            layoutColors.addView(colorContainer);
        }
        adapter.updateData(filtered);
    }

<<<<<<< HEAD
    private void showDeleteConfirmDialog(DanhMuc danhMuc, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa danh mục")
                .setMessage("Bạn có chắc chắn muốn xóa danh mục \"" + danhMuc.getTen() + "\" không?")
                .setPositiveButton("Xóa", (d, w) -> {
                    danhSachGoc.remove(danhMuc);
                    danhSachDanhMuc.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Đã xóa danh mục", Toast.LENGTH_SHORT).show();
=======
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

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

<<<<<<< HEAD
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

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
