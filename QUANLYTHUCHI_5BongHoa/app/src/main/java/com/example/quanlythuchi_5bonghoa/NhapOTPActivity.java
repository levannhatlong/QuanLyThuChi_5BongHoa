package com.example.quanlythuchi_5bonghoa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NhapOTPActivity extends Activity {

    private EditText edtOTP;
    private Button btnXacNhanOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_otp);

        edtOTP = findViewById(R.id.edtOTP);
        btnXacNhanOTP = findViewById(R.id.btnXacNhanOTP);

        btnXacNhanOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edtOTP.getText() != null ? edtOTP.getText().toString().trim() : "";

                if (TextUtils.isEmpty(otp)) {
                    edtOTP.setError("Vui lòng nhập OTP");
                    edtOTP.requestFocus();
                    return;
                }

                // Demo: chưa kiểm tra OTP thật
                Toast.makeText(NhapOTPActivity.this, "Xác nhận OTP thành công!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NhapOTPActivity.this, DatLaiMatKhauActivity.class);

                // Nếu sau này bạn muốn truyền OTP/email qua màn đặt lại mật khẩu:
                intent.putExtra("otp", otp);
                // intent.putExtra("email", getIntent().getStringExtra("email"));

                startActivity(intent);
                finish();
            }
        });
    }
}
