package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
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
    private String selectedIcon = "food.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        setupListeners();
        loadDanhMuc();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabChiTieu = findViewById(R.id.tabChiTieu);
        tabThuNhap = findViewById(R.id.tabThuNhap);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerDanhMuc = findViewById(R.id.recyclerDanhMuc);
        fabThemDanhMuc = findViewById(R.id.fabThemDanhMuc);
        emptyView = findViewById(R.id.emptyView);

        recyclerDanhMuc.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DanhMucAdapter(this, danhMucList, new DanhMucAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(DanhMuc danhMuc, int position) {
                showDialogThemSua(danhMuc);
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

        fabThemDanhMuc.setOnClickListener(v -> showDialogThemSua(null));

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
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
