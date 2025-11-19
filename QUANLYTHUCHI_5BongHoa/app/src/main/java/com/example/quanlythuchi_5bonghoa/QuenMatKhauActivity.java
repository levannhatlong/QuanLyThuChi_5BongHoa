package com.example.quanlythuchi_5bonghoa;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResetPassword();
            }
        });

        tvBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // quay lại màn hình đăng nhập
            }
        });
    }

    private void handleResetPassword() {
        String email = edtEmailForgot.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmailForgot.setError("Không được để trống");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailForgot.setError("Email không hợp lệ");
            return;
        }

        // TODO: Kiểm tra email trong database SQLite của bạn
        boolean exists = true; // bạn sẽ tự viết code kiểm tra email sau

        if (!exists) {
            edtEmailForgot.setError("Email không tồn tại trong hệ thống");
            return;
        }

        Toast.makeText(this, "Đã gửi yêu cầu đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();

        // TODO: Điều hướng đến màn hình đặt mật khẩu mới
        // startActivity(new Intent(this, ResetPasswordActivity.class));
    }
}
