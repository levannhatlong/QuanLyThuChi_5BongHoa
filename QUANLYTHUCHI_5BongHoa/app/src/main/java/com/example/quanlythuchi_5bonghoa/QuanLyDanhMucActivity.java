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
            List<DanhMuc> list = dbHelper.getDanhMucByLoai(loaiDanhMuc);
            
            danhMucList.clear();
            danhMucList.addAll(list);
            adapter.notifyDataSetChanged();
            
            updateEmptyView();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tải danh mục: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void filterByKeyword(String keyword) {
        try {
            String loaiDanhMuc = isChiTieu ? "Chi tiêu" : "Thu nhập";
            List<DanhMuc> filteredList;
            
            if (keyword.trim().isEmpty()) {
                filteredList = dbHelper.getDanhMucByLoai(loaiDanhMuc);
            } else {
                filteredList = dbHelper.searchDanhMuc(keyword, loaiDanhMuc);
            }
            
            danhMucList.clear();
            danhMucList.addAll(filteredList);
            adapter.notifyDataSetChanged();
            
            updateEmptyView();
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
                    try {
                        boolean success = dbHelper.deleteDanhMuc(danhMuc.getMaDanhMuc());
                        if (success) {
                            danhMucList.remove(position);
                            adapter.notifyItemRemoved(position);
                            updateEmptyView();
                            Toast.makeText(this, "Đã xóa danh mục thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Không thể xóa danh mục", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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