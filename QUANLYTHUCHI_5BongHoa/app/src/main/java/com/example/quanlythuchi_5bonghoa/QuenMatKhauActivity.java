package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QuenMatKhauActivity extends AppCompatActivity {

    private LinearLayout layoutStep1, layoutStep2;
    private TextInputEditText edtEmailForgot, edtMatKhauMoi, edtXacNhanMatKhau;
    private MaterialButton btnXacMinh, btnDatLaiMatKhau;
    private TextView tvBackToLogin, tvThongTinTaiKhoan, btnQuayLai;

    private int foundUserId = -1;
    private String foundUserInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quenmatkhau);

        initViews();
        setupListeners();
    }

    private void initViews() {
        layoutStep1 = findViewById(R.id.layoutStep1);
        layoutStep2 = findViewById(R.id.layoutStep2);
        edtEmailForgot = findViewById(R.id.edtEmailForgot);
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtXacNhanMatKhau = findViewById(R.id.edtXacNhanMatKhau);
        btnXacMinh = findViewById(R.id.btnXacMinh);
        btnDatLaiMatKhau = findViewById(R.id.btnDatLaiMatKhau);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvThongTinTaiKhoan = findViewById(R.id.tvThongTinTaiKhoan);
        btnQuayLai = findViewById(R.id.btnQuayLai);
    }

    private void setupListeners() {
        tvBackToLogin.setOnClickListener(v -> finish());

        btnQuayLai.setOnClickListener(v -> {
            layoutStep2.setVisibility(View.GONE);
            layoutStep1.setVisibility(View.VISIBLE);
            foundUserId = -1;
        });

        btnXacMinh.setOnClickListener(v -> xacMinhTaiKhoan());

        btnDatLaiMatKhau.setOnClickListener(v -> datLaiMatKhau());
    }

    private void xacMinhTaiKhoan() {
        String input = edtEmailForgot.getText().toString().trim();

        if (input.isEmpty()) {
            edtEmailForgot.setError("Vui lòng nhập email hoặc tên đăng nhập");
            return;
        }

        btnXacMinh.setEnabled(false);
        btnXacMinh.setText("Đang xác minh...");

        new Thread(() -> {
            try {
                Connection conn = DatabaseConnector.getConnection();
                if (conn != null) {
                    // Tìm user theo email hoặc tên đăng nhập
                    String sql = "SELECT MaNguoiDung, HoTen, EmailSoDienThoai, TenDangNhap " +
                            "FROM NguoiDung WHERE EmailSoDienThoai = ? OR TenDangNhap = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, input);
                    stmt.setString(2, input);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        foundUserId = rs.getInt("MaNguoiDung");
                        String hoTen = rs.getString("HoTen");
                        String email = rs.getString("EmailSoDienThoai");
                        foundUserInfo = hoTen + " (" + (email != null ? email : "N/A") + ")";

                        runOnUiThread(() -> {
                            tvThongTinTaiKhoan.setText("✓ Tài khoản: " + foundUserInfo);
                            layoutStep1.setVisibility(View.GONE);
                            layoutStep2.setVisibility(View.VISIBLE);
                            btnXacMinh.setEnabled(true);
                            btnXacMinh.setText("Xác minh");
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Không tìm thấy tài khoản với thông tin này", 
                                    Toast.LENGTH_SHORT).show();
                            btnXacMinh.setEnabled(true);
                            btnXacMinh.setText("Xác minh");
                        });
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Không thể kết nối đến máy chủ", 
                                Toast.LENGTH_SHORT).show();
                        btnXacMinh.setEnabled(true);
                        btnXacMinh.setText("Xác minh");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnXacMinh.setEnabled(true);
                    btnXacMinh.setText("Xác minh");
                });
            }
        }).start();
    }

    private void datLaiMatKhau() {
        String matKhauMoi = edtMatKhauMoi.getText().toString().trim();
        String xacNhan = edtXacNhanMatKhau.getText().toString().trim();

        if (matKhauMoi.isEmpty()) {
            edtMatKhauMoi.setError("Vui lòng nhập mật khẩu mới");
            return;
        }

        if (matKhauMoi.length() < 6) {
            edtMatKhauMoi.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }

        if (!matKhauMoi.equals(xacNhan)) {
            edtXacNhanMatKhau.setError("Mật khẩu xác nhận không khớp");
            return;
        }

        if (foundUserId == -1) {
            Toast.makeText(this, "Lỗi: Không xác định được tài khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        btnDatLaiMatKhau.setEnabled(false);
        btnDatLaiMatKhau.setText("Đang xử lý...");

        new Thread(() -> {
            try {
                Connection conn = DatabaseConnector.getConnection();
                if (conn != null) {
                    String sql = "UPDATE NguoiDung SET MatKhau = ? WHERE MaNguoiDung = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, matKhauMoi);
                    stmt.setInt(2, foundUserId);

                    int result = stmt.executeUpdate();
                    stmt.close();
                    conn.close();

                    runOnUiThread(() -> {
                        if (result > 0) {
                            Toast.makeText(this, "Đặt lại mật khẩu thành công!", 
                                    Toast.LENGTH_LONG).show();
                            
                            // Chuyển về màn hình đăng nhập
                            Intent intent = new Intent(this, dangnhap.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", 
                                    Toast.LENGTH_SHORT).show();
                            btnDatLaiMatKhau.setEnabled(true);
                            btnDatLaiMatKhau.setText("Đặt lại mật khẩu");
                        }
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Không thể kết nối đến máy chủ", 
                                Toast.LENGTH_SHORT).show();
                        btnDatLaiMatKhau.setEnabled(true);
                        btnDatLaiMatKhau.setText("Đặt lại mật khẩu");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnDatLaiMatKhau.setEnabled(true);
                    btnDatLaiMatKhau.setText("Đặt lại mật khẩu");
                });
            }
        }).start();
    }
}
