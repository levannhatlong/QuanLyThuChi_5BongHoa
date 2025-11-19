package com.example.quanlythuchi_5bonghoa;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class TaiKhoanActivity extends AppCompatActivity {

    private ImageView btnBack, imgProfile;
    private EditText edtName, edtEmail, edtBirthDate;
    private TextView btnDoiMatKhau, btnXoaBoNhoCache, btnXoaTaiLieu, btnXoaTaiKhoan;

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
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtBirthDate = findViewById(R.id.edtBirthDate);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);
        btnXoaBoNhoCache = findViewById(R.id.btnXoaBoNhoCache);
        btnXoaTaiLieu = findViewById(R.id.btnXoaTaiLieu);
        btnXoaTaiKhoan = findViewById(R.id.btnXoaTaiKhoan);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            saveUserData();
            finish();
        });

        // Profile image click
        imgProfile.setOnClickListener(v -> {
            // Mở gallery để chọn ảnh
            Toast.makeText(this, "Chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
            // TODO: Implement image picker
        });

        // Birth date picker
        edtBirthDate.setOnClickListener(v -> showDatePicker());

        // Đổi mật khẩu
        btnDoiMatKhau.setOnClickListener(v -> {
            // Mở dialog hoặc activity đổi mật khẩu
            showChangePasswordDialog();
        });

        // Xóa bộ nhớ cache
        btnXoaBoNhoCache.setOnClickListener(v -> {
            showConfirmDialog(
                    "Xóa bộ nhớ cache",
                    "Bạn có chắc chắn muốn xóa bộ nhớ cache không?",
                    () -> {
                        clearCache();
                        Toast.makeText(this, "Đã xóa bộ nhớ cache", Toast.LENGTH_SHORT).show();
                    }
            );
        });

        // Xóa dữ liệu ứng dụng
        btnXoaTaiLieu.setOnClickListener(v -> {
            showConfirmDialog(
                    "Xóa dữ liệu ứng dụng",
                    "CẢNH BÁO: Hành động này sẽ xóa toàn bộ dữ liệu của bạn và không thể khôi phục. Bạn có chắc chắn muốn tiếp tục?",
                    () -> {
                        clearAppData();
                        Toast.makeText(this, "Đã xóa dữ liệu ứng dụng", Toast.LENGTH_SHORT).show();
                    }
            );
        });

        // Xóa tài khoản
        btnXoaTaiKhoan.setOnClickListener(v -> {
            showConfirmDialog(
                    "Xóa tài khoản",
                    "CẢNH BÁO: Hành động này sẽ xóa vĩnh viễn tài khoản của bạn và không thể khôi phục. Bạn có chắc chắn muốn tiếp tục?",
                    () -> {
                        deleteAccount();
                    }
            );
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    edtBirthDate.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void showChangePasswordDialog() {
        // TODO: Implement change password dialog with old password, new password, confirm password fields
        Toast.makeText(this, "Chức năng đổi mật khẩu", Toast.LENGTH_SHORT).show();
    }

    private void showConfirmDialog(String title, String message, Runnable onConfirm) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    onConfirm.run();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadUserData() {
        // TODO: Load user data from SharedPreferences or database
        // edtName.setText(userData.getName());
        // edtEmail.setText(userData.getEmail());
        // edtBirthDate.setText(userData.getBirthDate());
    }

    private void saveUserData() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String birthDate = edtBirthDate.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Save to SharedPreferences or database
        // SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        // SharedPreferences.Editor editor = prefs.edit();
        // editor.putString("name", name);
        // editor.putString("email", email);
        // editor.putString("birth_date", birthDate);
        // editor.apply();

        Toast.makeText(this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
    }

    private void clearCache() {
        // TODO: Implement cache clearing logic
        try {
            deleteDir(getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAppData() {
        // TODO: Implement app data clearing logic
        // Clear all SharedPreferences, database, files, etc.
    }

    private void deleteAccount() {
        // TODO: Implement account deletion logic
        // 1. Delete user data from server
        // 2. Clear local data
        // 3. Logout and redirect to login screen
        Toast.makeText(this, "Tài khoản đã được xóa", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean deleteDir(java.io.File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new java.io.File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        }
        return false;
    }
}