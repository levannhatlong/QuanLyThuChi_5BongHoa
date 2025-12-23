package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TaiKhoanActivity extends AppCompatActivity {

    private ImageView btnBack, imgProfile;
    private TextView tvUserName, btnEdit, btnSave;
    private TextView tvTenDangNhap, tvHoTen, tvEmail, tvNgaySinh;
    private EditText edtHoTen, edtEmail, edtNgaySinh;
    private LinearLayout layoutChangePassword;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);

        initViews();
        setupListeners();
        loadUserData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);
        tvUserName = findViewById(R.id.tvUserName);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        
        tvTenDangNhap = findViewById(R.id.tvTenDangNhap);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvEmail = findViewById(R.id.tvEmail);
        tvNgaySinh = findViewById(R.id.tvNgaySinh);
        
        edtHoTen = findViewById(R.id.edtHoTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        
        layoutChangePassword = findViewById(R.id.layoutChangePassword);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnEdit.setOnClickListener(v -> toggleEditMode(true));
        
        btnSave.setOnClickListener(v -> {
            saveUserData();
            toggleEditMode(false);
        });
        
        layoutChangePassword.setOnClickListener(v -> showChangePasswordDialog());

        // Handle back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void toggleEditMode(boolean editMode) {
        isEditMode = editMode;
        
        if (editMode) {
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
            
            tvHoTen.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
            tvNgaySinh.setVisibility(View.GONE);
            
            edtHoTen.setVisibility(View.VISIBLE);
            edtEmail.setVisibility(View.VISIBLE);
            edtNgaySinh.setVisibility(View.VISIBLE);
            
            // Copy current values to edit fields
            edtHoTen.setText(tvHoTen.getText().toString());
            edtEmail.setText(tvEmail.getText().toString());
            edtNgaySinh.setText(tvNgaySinh.getText().toString());
        } else {
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            
            tvHoTen.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            tvNgaySinh.setVisibility(View.VISIBLE);
            
            edtHoTen.setVisibility(View.GONE);
            edtEmail.setVisibility(View.GONE);
            edtNgaySinh.setVisibility(View.GONE);
        }
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);
        
        if (userId == -1) {
            // Fallback to SharedPreferences data
            loadUserDataFromPrefs();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.getUserInfo(userId, new DatabaseHelper.DataCallback<NguoiDung>() {
            @Override
            public void onSuccess(NguoiDung user) {
                runOnUiThread(() -> {
                    tvUserName.setText(user.getHoTen());
                    tvTenDangNhap.setText(user.getEmail());
                    tvHoTen.setText(user.getHoTen());
                    tvEmail.setText(user.getEmail());
                    tvNgaySinh.setText("Chưa cập nhật");
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(TaiKhoanActivity.this, "Lỗi khi tải thông tin: " + error, Toast.LENGTH_SHORT).show();
                    // Fallback to SharedPreferences data
                    loadUserDataFromPrefs();
                });
            }
        });
    }

    private void loadUserDataFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String tenDangNhap = prefs.getString("username", "user@example.com");
        String hoTen = prefs.getString("full_name", "Người dùng");
        String email = prefs.getString("email", "user@example.com");
        String ngaySinh = prefs.getString("birth_date", "01/01/1990");

        tvUserName.setText(hoTen);
        tvTenDangNhap.setText(tenDangNhap);
        tvHoTen.setText(hoTen);
        tvEmail.setText(email);
        tvNgaySinh.setText(ngaySinh);
    }

    private void saveUserData() {
        String hoTen = edtHoTen.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String ngaySinh = edtNgaySinh.getText().toString().trim();

        if (hoTen.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("full_name", hoTen);
        editor.putString("email", email);
        editor.putString("birth_date", ngaySinh);
        editor.apply();

        // Update display
        tvUserName.setText(hoTen);
        tvHoTen.setText(hoTen);
        tvEmail.setText(email);
        tvNgaySinh.setText(ngaySinh);

        Toast.makeText(this, "Đã lưu thông tin thành công", Toast.LENGTH_SHORT).show();
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi mật khẩu");
        builder.setMessage("Tính năng đổi mật khẩu sẽ được cập nhật trong phiên bản tiếp theo.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}