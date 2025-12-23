package com.example.quanlythuchi_5bonghoa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Random;

public class QuenMatKhauActivity extends Activity {

    private EditText edtEmailForgot;
    private Button btnResetPassword;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quenmatkhau);

        edtEmailForgot = findViewById(R.id.edtEmailForgot);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        tvBackToLogin.setOnClickListener(v -> finish());

        btnResetPassword.setOnClickListener(v -> handleSendOtp());
    }

    private void handleSendOtp() {
        String emailOrPhone = edtEmailForgot.getText() != null
                ? edtEmailForgot.getText().toString().trim()
                : "";

        if (TextUtils.isEmpty(emailOrPhone)) {
            toast("Vui lòng nhập Email/SĐT");
            return;
        }

        new Thread(() -> {
            boolean exists = checkUserExists(emailOrPhone);

            runOnUiThread(() -> {
                if (!exists) {
                    toast("Email/SĐT không tồn tại trong hệ thống!");
                    return;
                }

                String otp = generateOtp6();
                saveOtpTemp(emailOrPhone, otp);

                // Demo: hiện OTP để test (KHI NỘP BÀI nên bỏ)
                toast("OTP của bạn (demo): " + otp);

                Intent intent = new Intent(QuenMatKhauActivity.this, NhapOTPActivity.class);
                intent.putExtra("email_or_phone", emailOrPhone);
                startActivity(intent);
            });
        }).start();
    }

    private boolean checkUserExists(String emailOrPhone) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnector.getConnection();
            if (conn == null) return false;

            String sql = "SELECT TOP 1 MaNguoiDung FROM NguoiDung WHERE EmailSoDienThoai = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, emailOrPhone);

            rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    private String generateOtp6() {
        int otp = 100000 + new Random().nextInt(900000);
        return String.format(Locale.US, "%06d", otp);
    }

    private void saveOtpTemp(String emailOrPhone, String otp) {
        SharedPreferences sp = getSharedPreferences("otp_prefs", MODE_PRIVATE);
        sp.edit()
                .putString("otp_target", emailOrPhone)
                .putString("otp_code", otp)
                .putLong("otp_time", System.currentTimeMillis())
                .apply();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
