package com.example.quanlythuchi_5bonghoa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DatLaiMatKhauActivity extends Activity {

    EditText edtNewPassword, edtConfirmPassword;
    Button btnXacNhanDatLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lai_mat_khau);

        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnXacNhanDatLai = findViewById(R.id.btnXacNhanDatLai);

        btnXacNhanDatLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pass1 = edtNewPassword.getText().toString().trim();
                String pass2 = edtConfirmPassword.getText().toString().trim();

                if (pass1.isEmpty() || pass2.isEmpty()) {
                    Toast.makeText(DatLaiMatKhauActivity.this, "Không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pass1.equals(pass2)) {
                    Toast.makeText(DatLaiMatKhauActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(DatLaiMatKhauActivity.this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                // Quay lại màn hình đăng nhập
                Intent intent = new Intent(DatLaiMatKhauActivity.this, dangnhap.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}

