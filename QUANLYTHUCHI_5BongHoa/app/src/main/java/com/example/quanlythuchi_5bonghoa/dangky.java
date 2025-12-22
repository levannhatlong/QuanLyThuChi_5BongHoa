package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class dangky extends AppCompatActivity {

    private TextInputLayout tilFullName, tilUsername, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText edtFullName, edtUsername, edtEmail, edtPassword, edtConfirmPassword;

    private MaterialButton btnSignUp;
    private TextView tvAlreadyHave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);

        // TextInputLayout
        tilFullName = findViewById(R.id.tilFullName);
        tilUsername = findViewById(R.id.tilUsername);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        // EditText
        edtFullName = findViewById(R.id.edtFullName);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        tvAlreadyHave = findViewById(R.id.tvAlreadyHave);

        btnSignUp.setOnClickListener(v -> handleSignUp());

        tvAlreadyHave.setOnClickListener(v -> gotoLogin());
    }

    private void gotoLogin() {
        Intent intent = new Intent(dangky.this, dangnhap.class);
        startActivity(intent);
        finish();
    }

    private void handleSignUp() {
        // Clear errors
        tilFullName.setError(null);
        tilUsername.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        String fullName = edtFullName.getText() != null ? edtFullName.getText().toString().trim() : "";
        String username = edtUsername.getText() != null ? edtUsername.getText().toString().trim() : "";
        String emailOrPhone = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";
        String password = edtPassword.getText() != null ? edtPassword.getText().toString().trim() : "";
        String confirmPassword = edtConfirmPassword.getText() != null ? edtConfirmPassword.getText().toString().trim() : "";

        boolean isValid = true;

        // HoTen trong DB có thể NULL, nhưng UI bạn có field Full Name -> mình vẫn khuyến nghị bắt buộc
        if (TextUtils.isEmpty(fullName)) {
            tilFullName.setError("Vui lòng nhập họ tên");
            isValid = false;
        }

        if (TextUtils.isEmpty(username)) {
            tilUsername.setError("Vui lòng nhập username");
            isValid = false;
        }

        if (TextUtils.isEmpty(emailOrPhone)) {
            tilEmail.setError("Vui lòng nhập email hoặc số điện thoại");
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Vui lòng nhập mật khẩu");
            isValid = false;
        } else if (password.length() < 6) {
            tilPassword.setError("Mật khẩu tối thiểu 6 ký tự");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Vui lòng nhập lại mật khẩu");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Mật khẩu nhập lại không khớp");
            isValid = false;
        }

        if (!isValid) return;

        btnSignUp.setEnabled(false);

        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) {
                runOnUiThread(() -> {
                    btnSignUp.setEnabled(true);
                    Toast.makeText(dangky.this, "Không thể kết nối database", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            PreparedStatement psCheck = null;
            ResultSet rs = null;
            PreparedStatement psInsert = null;

            try {
                // 1) Check trùng username
                String checkSql = "SELECT 1 FROM NguoiDung WHERE TenDangNhap = ?";
                psCheck = connection.prepareStatement(checkSql);
                psCheck.setString(1, username);
                rs = psCheck.executeQuery();

                if (rs.next()) {
                    runOnUiThread(() -> {
                        btnSignUp.setEnabled(true);
                        tilUsername.setError("Username đã tồn tại, vui lòng chọn tên khác");
                    });
                    return;
                }

                // 2) Insert user
                String insertSql = "INSERT INTO NguoiDung (TenDangNhap, MatKhau, HoTen, EmailSoDienThoai) VALUES (?, ?, ?, ?)";
                psInsert = connection.prepareStatement(insertSql);
                psInsert.setString(1, username);
                psInsert.setString(2, password); // DB hiện đang lưu plaintext như dữ liệu mẫu
                psInsert.setString(3, fullName);
                psInsert.setString(4, emailOrPhone);

                int rows = psInsert.executeUpdate();

                if (rows > 0) {
                    runOnUiThread(() -> {
                        Toast.makeText(dangky.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
                        // Có thể truyền sẵn username qua màn đăng nhập nếu bạn muốn
                        Intent intent = new Intent(dangky.this, dangnhap.class);
                        intent.putExtra("prefill_username", username);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> {
                        btnSignUp.setEnabled(true);
                        Toast.makeText(dangky.this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    btnSignUp.setEnabled(true);
                    Toast.makeText(dangky.this, "Lỗi đăng ký: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } finally {
                try { if (rs != null) rs.close(); } catch (Exception ignored) {}
                try { if (psCheck != null) psCheck.close(); } catch (Exception ignored) {}
                try { if (psInsert != null) psInsert.close(); } catch (Exception ignored) {}
                try { connection.close(); } catch (Exception ignored) {}
            }
        }).start();
    }
}
