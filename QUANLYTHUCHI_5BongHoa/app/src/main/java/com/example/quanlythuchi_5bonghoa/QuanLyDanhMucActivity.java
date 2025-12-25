package com.example.quanlythuchi_5bonghoa;

<<<<<<< HEAD
import android.content.Intent;
import android.content.SharedPreferences;
=======
<<<<<<< HEAD
import android.graphics.Color;
=======

import android.content.SharedPreferences;

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
<<<<<<< HEAD
=======

import com.google.android.material.button.MaterialButton;
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
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
=======
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

>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

<<<<<<< HEAD
        dbHelper = new DatabaseHelper(this);
        
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

=======
<<<<<<< HEAD
=======

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
        initViews();
        setupListeners();
<<<<<<< HEAD
        setupRecyclerView();
<<<<<<< HEAD
        
        // Test kết nối database trước khi load dữ liệu
        testDatabaseConnection();
    }

    private void testDatabaseConnection() {
        dbHelper.testConnection(new DatabaseHelper.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(() -> {
                    Toast.makeText(QuanLyDanhMucActivity.this, result, Toast.LENGTH_SHORT).show();
                    loadDanhMuc(); // Load dữ liệu sau khi test thành công
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(QuanLyDanhMucActivity.this, "Lỗi kết nối: " + error, Toast.LENGTH_LONG).show();
                    updateEmptyView(); // Hiển thị empty view nếu không kết nối được
                });
            }
        });
=======
=======

        loadDanhMuc();

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabChiTieu = findViewById(R.id.tabChiTieu);
        tabThuNhap = findViewById(R.id.tabThuNhap);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerDanhMuc = findViewById(R.id.recyclerDanhMuc);
        fabThemDanhMuc = findViewById(R.id.fabThemDanhMuc);
<<<<<<< HEAD
        emptyView = findViewById(R.id.emptyView);
=======

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
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tabChiTieu.setOnClickListener(v -> {
            isChiTieu = true;
<<<<<<< HEAD
            updateTabSelection();
=======
            updateTabUI();
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
            loadDanhMuc();
        });

        tabThuNhap.setOnClickListener(v -> {
            isChiTieu = false;
<<<<<<< HEAD
            updateTabSelection();
            loadDanhMuc();
        });
=======
            updateTabUI();
            loadDanhMuc();
        });

<<<<<<< HEAD
        fabThemDanhMuc.setOnClickListener(v -> showDialogThemSua(null, -1));
=======

        fabThemDanhMuc.setOnClickListener(v -> showDialogThemSua(null));

>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3

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

        fabThemDanhMuc.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyDanhMucActivity.this, ThemSuaDanhMucActivity.class);
            intent.putExtra("IS_CHI_TIEU", isChiTieu);
            startActivityForResult(intent, 100);
        });
    }

    private void setupRecyclerView() {
        adapter = new DanhMucAdapter(this, danhMucList, new DanhMucAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(DanhMuc danhMuc, int position) {
                Intent intent = new Intent(QuanLyDanhMucActivity.this, ThemSuaDanhMucActivity.class);
                intent.putExtra("EDIT_MODE", true);
                intent.putExtra("DANH_MUC_ID", danhMuc.getMaDanhMuc());
                intent.putExtra("TEN_DANH_MUC", danhMuc.getTenDanhMuc());
                intent.putExtra("MO_TA", danhMuc.getMoTa());
                intent.putExtra("BIEU_TUONG", danhMuc.getBieuTuong());
                intent.putExtra("MAU_SAC", danhMuc.getMauSac());
                intent.putExtra("IS_CHI_TIEU", danhMuc.isChiTieu());
                startActivityForResult(intent, 101);
            }

            @Override
            public void onDeleteClick(DanhMuc danhMuc, int position) {
                showDeleteConfirmDialog(danhMuc, position);
            }
        });

        recyclerDanhMuc.setLayoutManager(new LinearLayoutManager(this));
        recyclerDanhMuc.setAdapter(adapter);
    }

    private void updateTabSelection() {
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
<<<<<<< HEAD
        }
    }

    private void loadDanhMuc() {
        try {
            String loaiDanhMuc = isChiTieu ? "Chi tiêu" : "Thu nhập";
            
            dbHelper.getDanhMucByLoai(loaiDanhMuc, new DatabaseHelper.DataCallback<List<DanhMuc>>() {
                @Override
                public void onSuccess(List<DanhMuc> result) {
                    runOnUiThread(() -> {
                        danhMucList.clear();
                        danhMucList.addAll(result);
                        adapter.notifyDataSetChanged();
                        updateEmptyView();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        Toast.makeText(QuanLyDanhMucActivity.this, error, Toast.LENGTH_SHORT).show();
                        updateEmptyView();
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải danh mục: " + e.getMessage(), Toast.LENGTH_SHORT).show();
=======
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
        }
    }

<<<<<<< HEAD
    private void filterByKeyword(String keyword) {
<<<<<<< HEAD
        try {
            String loaiDanhMuc = isChiTieu ? "Chi tiêu" : "Thu nhập";
            
            if (keyword.trim().isEmpty()) {
                loadDanhMuc(); // Reload all data
            } else {
                dbHelper.searchDanhMuc(keyword, loaiDanhMuc, new DatabaseHelper.DataCallback<List<DanhMuc>>() {
                    @Override
                    public void onSuccess(List<DanhMuc> result) {
                        runOnUiThread(() -> {
                            danhMucList.clear();
                            danhMucList.addAll(result);
                            adapter.notifyDataSetChanged();
                            updateEmptyView();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            Toast.makeText(QuanLyDanhMucActivity.this, error, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tìm kiếm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmptyView() {
        if (danhMucList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerDanhMuc.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerDanhMuc.setVisibility(View.VISIBLE);
        }
=======
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
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
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
<<<<<<< HEAD
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa danh mục \"" + danhMuc.getTenDanhMuc() + "\"?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteDanhMuc(danhMuc.getMaDanhMuc(), new DatabaseHelper.DataCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean result) {
                            runOnUiThread(() -> {
                                if (result) {
                                    danhMucList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    updateEmptyView();
                                    Toast.makeText(QuanLyDanhMucActivity.this, "Đã xóa danh mục thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(QuanLyDanhMucActivity.this, "Không thể xóa danh mục", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Toast.makeText(QuanLyDanhMucActivity.this, error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
=======
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
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

<<<<<<< HEAD
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == 100 || requestCode == 101)) {
            loadDanhMuc(); // Reload data after adding/editing
        }
    }
}
=======
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
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
