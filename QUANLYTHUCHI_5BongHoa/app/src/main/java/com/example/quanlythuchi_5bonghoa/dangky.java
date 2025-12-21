package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class dangky extends AppCompatActivity {

    private MaterialButton btnSignUp;
    private TextView tvAlreadyHave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);

        btnSignUp = findViewById(R.id.btnSignUp);
        tvAlreadyHave = findViewById(R.id.tvAlreadyHave);

        // Nút Đăng ký → chuyển sang màn hình đăng nhập
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dangky.this, dangnhap.class);
                startActivity(intent);
                finish();
            }
        });

        // Dòng "Already have an account?" → chuyển sang đăng nhập
        tvAlreadyHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dangky.this, dangnhap.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
