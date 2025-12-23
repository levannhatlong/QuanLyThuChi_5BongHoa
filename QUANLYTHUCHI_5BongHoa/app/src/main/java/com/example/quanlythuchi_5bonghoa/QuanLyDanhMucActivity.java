package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_danh_muc);

        dbHelper = new DatabaseHelper(this);
        
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        setupListeners();
        setupRecyclerView();
        
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
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tabChiTieu = findViewById(R.id.tabChiTieu);
        tabThuNhap = findViewById(R.id.tabThuNhap);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerDanhMuc = findViewById(R.id.recyclerDanhMuc);
        fabThemDanhMuc = findViewById(R.id.fabThemDanhMuc);
        emptyView = findViewById(R.id.emptyView);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tabChiTieu.setOnClickListener(v -> {
            isChiTieu = true;
            updateTabSelection();
            loadDanhMuc();
        });

        tabThuNhap.setOnClickListener(v -> {
            isChiTieu = false;
            updateTabSelection();
            loadDanhMuc();
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
        }
    }

    private void filterByKeyword(String keyword) {
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
    }

    private void showDeleteConfirmDialog(DanhMuc danhMuc, int position) {
        new AlertDialog.Builder(this)
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
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == 100 || requestCode == 101)) {
            loadDanhMuc(); // Reload data after adding/editing
        }
    }
}