package com.example.quanlythuchi_5bonghoa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class TaiKhoanActivity extends AppCompatActivity {

    private ImageView btnBack, imgProfile;
    private TextView btnEdit, btnSave;
    private TextView tvUserName;
    
    // TextView hiển thị (chế độ xem)
    private TextView tvTenDangNhap, tvHoTen, tvEmailSoDienThoai, tvNgaySinh;
    
    // EditText chỉnh sửa (chế độ sửa)
    private EditText edtHoTen, edtEmailSoDienThoai;
    private LinearLayout layoutNgaySinhEdit;
    private TextView tvNgaySinhEdit;
    
    private LinearLayout layoutDoiMatKhau, layoutXoaCache;
    private TextView btnDangXuat, btnXoaTaiKhoan;

    private int userId;
    private String selectedDate = "";
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        setupListeners();
        loadUserData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        imgProfile = findViewById(R.id.imgProfile);
        tvUserName = findViewById(R.id.tvUserName);
        
        // TextView hiển thị
        tvTenDangNhap = findViewById(R.id.tvTenDangNhap);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvEmailSoDienThoai = findViewById(R.id.tvEmailSoDienThoai);
        tvNgaySinh = findViewById(R.id.tvNgaySinh);
        
        // EditText chỉnh sửa
        edtHoTen = findViewById(R.id.edtHoTen);
        edtEmailSoDienThoai = findViewById(R.id.edtEmailSoDienThoai);
        layoutNgaySinhEdit = findViewById(R.id.layoutNgaySinhEdit);
        tvNgaySinhEdit = findViewById(R.id.tvNgaySinhEdit);
        
        layoutDoiMatKhau = findViewById(R.id.layoutDoiMatKhau);
        layoutXoaCache = findViewById(R.id.layoutXoaCache);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnXoaTaiKhoan = findViewById(R.id.btnXoaTaiKhoan);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            if (isEditMode) {
                toggleEditMode(false);
                loadUserData();
            } else {
                finish();
            }
        });

        btnEdit.setOnClickListener(v -> toggleEditMode(true));

        btnSave.setOnClickListener(v -> saveUserData());

        layoutNgaySinhEdit.setOnClickListener(v -> {
            if (isEditMode) showDatePicker();
        });

        layoutDoiMatKhau.setOnClickListener(v -> showChangePasswordDialog());

        layoutXoaCache.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa bộ nhớ cache")
                    .setMessage("Bạn có chắc muốn xóa bộ nhớ cache?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        clearCache();
                        Toast.makeText(this, "Đã xóa bộ nhớ cache", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        btnDangXuat.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> logout())
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        btnXoaTaiKhoan.setOnClickListener(v -> showDeleteAccountDialog());
    }

    private void toggleEditMode(boolean editMode) {
        isEditMode = editMode;
        
        // Hiển thị/ẩn nút Sửa/Lưu
        btnEdit.setVisibility(editMode ? View.GONE : View.VISIBLE);
        btnSave.setVisibility(editMode ? View.VISIBLE : View.GONE);
        
        // Hiển thị/ẩn TextView và EditText
        tvHoTen.setVisibility(editMode ? View.GONE : View.VISIBLE);
        edtHoTen.setVisibility(editMode ? View.VISIBLE : View.GONE);
        
        tvEmailSoDienThoai.setVisibility(editMode ? View.GONE : View.VISIBLE);
        edtEmailSoDienThoai.setVisibility(editMode ? View.VISIBLE : View.GONE);
        
        tvNgaySinh.setVisibility(editMode ? View.GONE : View.VISIBLE);
        layoutNgaySinhEdit.setVisibility(editMode ? View.VISIBLE : View.GONE);
        
        if (editMode) {
            // Copy dữ liệu từ TextView sang EditText
            String hoTen = tvHoTen.getText().toString();
            edtHoTen.setText(hoTen.equals("--") ? "" : hoTen);
            
            String emailSdt = tvEmailSoDienThoai.getText().toString();
            edtEmailSoDienThoai.setText(emailSdt.equals("--") ? "" : emailSdt);
            
            String ngaySinh = tvNgaySinh.getText().toString();
            if (!ngaySinh.equals("--")) {
                selectedDate = ngaySinh;
                tvNgaySinhEdit.setText(ngaySinh);
                tvNgaySinhEdit.setTextColor(getResources().getColor(android.R.color.black));
            } else {
                tvNgaySinhEdit.setText("Chọn ngày");
                tvNgaySinhEdit.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
        }
    }

    private void loadUserData() {
        new Thread(() -> {
            NguoiDungRepository.NguoiDung nd = NguoiDungRepository.getNguoiDung(userId);
            runOnUiThread(() -> {
                if (nd != null) {
                    // Tên hiển thị dưới avatar
                    tvUserName.setText(nd.hoTen != null && !nd.hoTen.isEmpty() ? nd.hoTen : "Chưa cập nhật");

                    // Thông tin cá nhân từ database
                    tvTenDangNhap.setText(nd.tenDangNhap != null ? nd.tenDangNhap : "--");
                    tvHoTen.setText(nd.hoTen != null && !nd.hoTen.isEmpty() ? nd.hoTen : "--");
                    tvEmailSoDienThoai.setText(nd.emailSoDienThoai != null && !nd.emailSoDienThoai.isEmpty() ? nd.emailSoDienThoai : "--");
                    tvNgaySinh.setText(nd.ngaySinh != null && !nd.ngaySinh.isEmpty() ? nd.ngaySinh : "--");

                    // Lưu ngày sinh để edit
                    if (nd.ngaySinh != null && !nd.ngaySinh.isEmpty()) {
                        selectedDate = nd.ngaySinh;
                    }
                } else {
                    tvUserName.setText("Lỗi kết nối");
                    Toast.makeText(this, "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void saveUserData() {
        String hoTen = edtHoTen.getText().toString().trim();
        String emailSoDienThoai = edtEmailSoDienThoai.getText().toString().trim();

        if (hoTen.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            boolean success = NguoiDungRepository.updateNguoiDung(userId, hoTen, emailSoDienThoai, selectedDate);
            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "Đã lưu thông tin", Toast.LENGTH_SHORT).show();
                    toggleEditMode(false);
                    loadUserData();
                } else {
                    Toast.makeText(this, "Lỗi khi lưu thông tin", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        if (!selectedDate.isEmpty()) {
            try {
                String[] parts = selectedDate.split("/");
                if (parts.length == 3) {
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(parts[1]) - 1);
                    calendar.set(Calendar.YEAR, Integer.parseInt(parts[2]));
                }
            } catch (Exception ignored) {}
        }

        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                    tvNgaySinhEdit.setText(selectedDate);
                    tvNgaySinhEdit.setTextColor(getResources().getColor(android.R.color.black));
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void showChangePasswordDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_doi_mat_khau, null);
        EditText edtMatKhauCu = dialogView.findViewById(R.id.edtMatKhauCu);
        EditText edtMatKhauMoi = dialogView.findViewById(R.id.edtMatKhauMoi);
        EditText edtXacNhanMatKhau = dialogView.findViewById(R.id.edtXacNhanMatKhau);

        new AlertDialog.Builder(this)
                .setTitle("Đổi mật khẩu")
                .setView(dialogView)
                .setPositiveButton("Đổi", (dialog, which) -> {
                    String matKhauCu = edtMatKhauCu.getText().toString();
                    String matKhauMoi = edtMatKhauMoi.getText().toString();
                    String xacNhan = edtXacNhanMatKhau.getText().toString();

                    if (matKhauCu.isEmpty() || matKhauMoi.isEmpty() || xacNhan.isEmpty()) {
                        Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!matKhauMoi.equals(xacNhan)) {
                        Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (matKhauMoi.length() < 6) {
                        Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    new Thread(() -> {
                        boolean success = NguoiDungRepository.doiMatKhau(userId, matKhauCu, matKhauMoi);
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void logout() {
        getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();
        Intent intent = new Intent(this, dangnhap.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa tài khoản")
                .setMessage("CẢNH BÁO: Hành động này sẽ xóa vĩnh viễn tài khoản và toàn bộ dữ liệu của bạn!")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Xác nhận")
                            .setMessage("Bạn thực sự muốn xóa tài khoản?")
                            .setPositiveButton("Có, xóa ngay", (d, w) -> deleteAccount())
                            .setNegativeButton("Không", null)
                            .show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteAccount() {
        new Thread(() -> {
            boolean success = NguoiDungRepository.xoaTaiKhoan(userId);
            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "Tài khoản đã được xóa", Toast.LENGTH_SHORT).show();
                    logout();
                } else {
                    Toast.makeText(this, "Lỗi khi xóa tài khoản", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void clearCache() {
        try {
            deleteDir(getCacheDir());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean deleteDir(java.io.File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new java.io.File(dir, child));
                    if (!success) return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        }
        return false;
    }
}
