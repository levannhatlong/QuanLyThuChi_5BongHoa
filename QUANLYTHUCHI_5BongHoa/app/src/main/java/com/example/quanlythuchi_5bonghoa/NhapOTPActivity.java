package com.example.quanlythuchi_5bonghoa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NhapOTPActivity extends Activity {

    EditText edtOTP;
    Button btnXacNhanOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_otp);

        edtOTP = findViewById(R.id.edtOTP);
        btnXacNhanOTP = findViewById(R.id.btnXacNhanOTP);

        btnXacNhanOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NhapOTPActivity.this, DatLaiMatKhauActivity.class);
                startActivity(intent);
            }
        });
    }
}
