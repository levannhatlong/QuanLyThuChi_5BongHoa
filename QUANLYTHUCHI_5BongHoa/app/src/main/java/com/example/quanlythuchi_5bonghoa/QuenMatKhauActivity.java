package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

    private LinearLayout layoutStep1, layoutStep2, layoutStep3;
    private TextInputEditText edtEmailForgot, edtOTP, edtMatKhauMoi, edtXacNhanMatKhau;
    private MaterialButton btnGuiOTP, btnXacNhanOTP, btnDatLaiMatKhau;
    private TextView tvBackToLogin, tvEmailDaGui, tvCountdown, tvGuiLai, btnQuayLaiStep1;

    private int foundUserId = -1;
    private String userEmail = "";
    private String generatedOTP = "";
    private CountDownTimer countDownTimer;

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
        layoutStep3 = findViewById(R.id.layoutStep3);

        edtEmailForgot = findViewById(R.id.edtEmailForgot);
        edtOTP = findViewById(R.id.edtOTP);
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtXacNhanMatKhau = findViewById(R.id.edtXacNhanMatKhau);

        btnGuiOTP = findViewById(R.id.btnGuiOTP);
        btnXacNhanOTP = findViewById(R.id.btnXacNhanOTP);
        btnDatLaiMatKhau = findViewById(R.id.btnDatLaiMatKhau);

        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        tvEmailDaGui = findViewById(R.id.tvEmailDaGui);
        tvCountdown = findViewById(R.id.tvCountdown);
        tvGuiLai = findViewById(R.id.tvGuiLai);
        btnQuayLaiStep1 = findViewById(R.id.btnQuayLaiStep1);
    }

    private void setupListeners() {
        tvBackToLogin.setOnClickListener(v -> finish());

        btnQuayLaiStep1.setOnClickListener(v -> {
            if (countDownTimer != null) countDownTimer.cancel();
            layoutStep2.setVisibility(View.GONE);
            layoutStep1.setVisibility(View.VISIBLE);
        });

        btnGuiOTP.setOnClickListener(v -> guiOTP());
        btnXacNhanOTP.setOnClickListener(v -> xacNhanOTP());
        btnDatLaiMatKhau.setOnClickListener(v -> datLaiMatKhau());

        tvGuiLai.setOnClickListener(v -> guiLaiOTP());
    }

    private void guiOTP() {
        String email = edtEmailForgot.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmailForgot.setError("Vui lòng nhập email");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailForgot.setError("Email không hợp lệ");
            return;
        }

        btnGuiOTP.setEnabled(false);
        btnGuiOTP.setText("Đang kiểm tra...");

        new Thread(() -> {
            try {
                Connection conn = DatabaseConnector.getConnection();
                if (conn != null) {
                    String sql = "SELECT MaNguoiDung, EmailSoDienThoai FROM NguoiDung WHERE EmailSoDienThoai = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, email);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        foundUserId = rs.getInt("MaNguoiDung");
                        userEmail = rs.getString("EmailSoDienThoai");

                        rs.close();
                        stmt.close();
                        conn.close();

                        // Gửi OTP
                        runOnUiThread(() -> {
                            btnGuiOTP.setText("Đang gửi OTP...");
                            sendOTPEmail();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Email chưa được đăng ký trong hệ thống",
                                    Toast.LENGTH_SHORT).show();
                            btnGuiOTP.setEnabled(true);
                            btnGuiOTP.setText("📧 Gửi mã OTP");
                        });
                        rs.close();
                        stmt.close();
                        conn.close();
                    }
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Không thể kết nối đến máy chủ",
                                Toast.LENGTH_SHORT).show();
                        btnGuiOTP.setEnabled(true);
                        btnGuiOTP.setText("📧 Gửi mã OTP");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnGuiOTP.setEnabled(true);
                    btnGuiOTP.setText("📧 Gửi mã OTP");
                });
            }
        }).start();
    }

    private void sendOTPEmail() {
        generatedOTP = EmailSender.generateOTP();

        EmailSender.sendOTP(userEmail, generatedOTP, new EmailSender.EmailCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(QuenMatKhauActivity.this,
                            "Đã gửi mã OTP đến " + userEmail, Toast.LENGTH_LONG).show();

                    layoutStep1.setVisibility(View.GONE);
                    layoutStep2.setVisibility(View.VISIBLE);

                    String maskedEmail = maskEmail(userEmail);
                    tvEmailDaGui.setText("📧 Mã OTP đã gửi đến: " + maskedEmail);

                    btnGuiOTP.setEnabled(true);
                    btnGuiOTP.setText("📧 Gửi mã OTP");

                    startCountdown();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(QuenMatKhauActivity.this,
                            "Không thể gửi email: " + error, Toast.LENGTH_LONG).show();
                    btnGuiOTP.setEnabled(true);
                    btnGuiOTP.setText("📧 Gửi mã OTP");
                });
            }
        });
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex > 2) {
            return email.substring(0, 2) + "***" + email.substring(atIndex);
        }
        return email;
    }

    private void startCountdown() {
        tvGuiLai.setVisibility(View.GONE);
        tvCountdown.setVisibility(View.VISIBLE);

        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(300000, 1000) { // 5 phút
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                tvCountdown.setText(String.format("⏰ Mã có hiệu lực trong %d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("⏰ Mã OTP đã hết hạn");
                tvCountdown.setTextColor(0xFFF44336);
                tvGuiLai.setVisibility(View.VISIBLE);
                generatedOTP = ""; // Hủy OTP
            }
        }.start();
    }

    private void guiLaiOTP() {
        tvGuiLai.setEnabled(false);
        tvGuiLai.setText("Đang gửi...");
        sendOTPEmail();
        tvGuiLai.setEnabled(true);
        tvGuiLai.setText("Gửi lại mã OTP");
    }

    private void xacNhanOTP() {
        String inputOTP = edtOTP.getText().toString().trim();

        if (inputOTP.isEmpty()) {
            edtOTP.setError("Vui lòng nhập mã OTP");
            return;
        }

        if (inputOTP.length() != 6) {
            edtOTP.setError("Mã OTP phải có 6 số");
            return;
        }

        if (generatedOTP.isEmpty()) {
            Toast.makeText(this, "Mã OTP đã hết hạn, vui lòng gửi lại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputOTP.equals(generatedOTP)) {
            // OTP đúng
            if (countDownTimer != null) countDownTimer.cancel();

            Toast.makeText(this, "Xác minh OTP thành công!", Toast.LENGTH_SHORT).show();

            layoutStep2.setVisibility(View.GONE);
            layoutStep3.setVisibility(View.VISIBLE);
        } else {
            edtOTP.setError("Mã OTP không đúng");
            Toast.makeText(this, "Mã OTP không chính xác", Toast.LENGTH_SHORT).show();
        }
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
                            Toast.makeText(this, "🎉 Đặt lại mật khẩu thành công!",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(this, dangnhap.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại",
                                    Toast.LENGTH_SHORT).show();
                            btnDatLaiMatKhau.setEnabled(true);
                            btnDatLaiMatKhau.setText("🔒 Đặt lại mật khẩu");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnDatLaiMatKhau.setEnabled(true);
                    btnDatLaiMatKhau.setText("🔒 Đặt lại mật khẩu");
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
