package com.example.quanlythuchi_5bonghoa;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class TaiKhoanActivity extends AppCompatActivity {

    private ImageView btnBack, imgProfile;
    private TextView tvUserName, tvUserEmail;
    private TextInputEditText edtName, edtEmail, edtPhone, edtBirthDate;
    private LinearLayout layoutChangePassword, layoutTwoFactor, layoutBackupData, 
                        layoutClearCache, layoutClearAppData, layoutDeleteAccount;
    private SwitchCompat switchTwoFactor;
    private ExtendedFloatingActionButton fabSave;
    
    private SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);

        userPrefs = getSharedPreferences("UserData", MODE_PRIVATE);
        
        initViews();
        setupListeners();
        loadUserData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtBirthDate = findViewById(R.id.edtBirthDate);
        
        layoutChangePassword = findViewById(R.id.layoutChangePassword);
        layoutTwoFactor = findViewById(R.id.layoutTwoFactor);
        layoutBackupData = findViewById(R.id.layoutBackupData);
        layoutClearCache = findViewById(R.id.layoutClearCache);
        layoutClearAppData = findViewById(R.id.layoutClearAppData);
        layoutDeleteAccount = findViewById(R.id.layoutDeleteAccount);
        
        switchTwoFactor = findViewById(R.id.switchTwoFactor);
        fabSave = findViewById(R.id.fabSave);
    }


    private void setupListeners() {
        // Back button click
        btnBack.setOnClickListener(v -> {
            saveUserData();
            finish();
        });

        // Profile image click
        imgProfile.setOnClickListener(v -> {
            Toast.makeText(this, "Chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
            // TODO: Implement image picker
        });

        // Birth date picker
        edtBirthDate.setOnClickListener(v -> showDatePicker());

        // Change Password
        layoutChangePassword.setOnClickListener(v -> showChangePasswordDialog());

        // Two Factor Authentication
        switchTwoFactor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userPrefs.edit().putBoolean("two_factor_enabled", isChecked).apply();
            String message = isChecked ? "Đã bật xác thực 2 bước" : "Đã tắt xác thực 2 bước";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        // Backup Data
        layoutBackupData.setOnClickListener(v -> {
            showConfirmDialog(
                "Sao lưu dữ liệu",
                "Bạn có muốn xuất dữ liệu để sao lưu không?",
                () -> {
                    // TODO: Implement data backup
                    Toast.makeText(this, "Đang xuất dữ liệu...", Toast.LENGTH_SHORT).show();
                }
            );
        });

        // Clear Cache
        layoutClearCache.setOnClickListener(v -> {
            showConfirmDialog(
                "Xóa bộ nhớ cache",
                "Bạn có chắc chắn muốn xóa bộ nhớ cache không? Điều này sẽ giải phóng dung lượng lưu trữ.",
                () -> {
                    clearCache();
                    Toast.makeText(this, "Đã xóa bộ nhớ cache", Toast.LENGTH_SHORT).show();
                }
            );
        });

        // Clear App Data
        layoutClearAppData.setOnClickListener(v -> {
            showDangerDialog(
                "Xóa dữ liệu ứng dụng",
                "⚠️ CẢNH BÁO: Hành động này sẽ xóa toàn bộ dữ liệu của bạn và đặt lại ứng dụng về trạng thái ban đầu. Dữ liệu không thể khôi phục!\n\nBạn có chắc chắn muốn tiếp tục?",
                () -> {
                    clearAppData();
                    Toast.makeText(this, "Đã xóa dữ liệu ứng dụng", Toast.LENGTH_SHORT).show();
                }
            );
        });

        // Delete Account
        layoutDeleteAccount.setOnClickListener(v -> {
            showDangerDialog(
                "Xóa tài khoản",
                "⚠️ CẢNH BÁO NGHIÊM TRỌNG: Hành động này sẽ xóa vĩnh viễn tài khoản của bạn và toàn bộ dữ liệu liên quan. Bạn sẽ không thể đăng nhập lại và dữ liệu không thể khôi phục!\n\nĐây là hành động không thể hoàn tác. Bạn có thực sự chắc chắn?",
                () -> deleteAccount()
            );
        });

        // Save FAB
        fabSave.setOnClickListener(v -> {
            saveUserData();
            Toast.makeText(this, "Đã lưu thông tin thành công", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi mật khẩu");
        builder.setMessage("Tính năng đổi mật khẩu sẽ được cập nhật trong phiên bản tiếp theo.");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void showConfirmDialog(String title, String message, Runnable onConfirm) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Xác nhận", (dialog, which) -> onConfirm.run())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showDangerDialog(String title, String message, Runnable onConfirm) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("XÓA", (dialog, which) -> {
                    // Double confirmation for dangerous actions
                    new AlertDialog.Builder(this)
                            .setTitle("Xác nhận lần cuối")
                            .setMessage("Bạn có thực sự chắc chắn muốn thực hiện hành động này?")
                            .setPositiveButton("CHẮC CHẮN", (d, w) -> onConfirm.run())
                            .setNegativeButton("HỦY", null)
                            .show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void loadUserData() {
        String name = userPrefs.getString("name", "S Bông Hoa");
        String email = userPrefs.getString("email", "5bonghoa@gmail.com");
        String phone = userPrefs.getString("phone", "");
        String birthDate = userPrefs.getString("birth_date", "30/08/2001");
        boolean twoFactorEnabled = userPrefs.getBoolean("two_factor_enabled", false);

        edtName.setText(name);
        edtEmail.setText(email);
        edtPhone.setText(phone);
        edtBirthDate.setText(birthDate);
        
        // Update header info
        tvUserName.setText(name);
        tvUserEmail.setText(email);
        
        switchTwoFactor.setChecked(twoFactorEnabled);
    }

    private void saveUserData() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String birthDate = edtBirthDate.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("birth_date", birthDate);
        editor.apply();

        // Update header info
        tvUserName.setText(name);
        tvUserEmail.setText(email);
    }

    private void clearCache() {
        try {
            deleteDir(getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAppData() {
        // Clear SharedPreferences
        userPrefs.edit().clear().apply();
        getSharedPreferences("AppSettings", MODE_PRIVATE).edit().clear().apply();
        
        // Clear cache
        clearCache();
        
        // TODO: Clear database data
        // DatabaseHelper dbHelper = new DatabaseHelper(this);
        // dbHelper.clearAllData();
        
        Toast.makeText(this, "Đã xóa toàn bộ dữ liệu ứng dụng", Toast.LENGTH_LONG).show();
        
        // Restart app or go to login
        finishAffinity();
    }

    private void deleteAccount() {
        // Clear all data
        clearAppData();
        
        Toast.makeText(this, "Tài khoản đã được xóa vĩnh viễn", Toast.LENGTH_LONG).show();
        
        // TODO: Send delete request to server if using online account
        
        // Close app completely
        finishAffinity();
        System.exit(0);
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

    @Override
    public void onBackPressed() {
        saveUserData();
        super.onBackPressed();
    }
}