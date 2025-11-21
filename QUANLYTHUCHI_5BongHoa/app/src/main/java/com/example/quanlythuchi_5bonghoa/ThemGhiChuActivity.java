package com.example.quanlythuchi_5bonghoa;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ThemGhiChuActivity extends AppCompatActivity {

    private EditText etTitle, etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_ghi_chu);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);

        // Nút Back trên header
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút Lưu
        findViewById(R.id.btnSave).setOnClickListener(v -> luuGhiChu());

        // Nút Hủy
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
    }

    private void luuGhiChu() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung ghi chú", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu tiêu đề trống → tự sinh tiêu đề từ 10 ký tự đầu nội dung
        if (title.isEmpty()) {
            title = content.length() > 10 ? content.substring(0, 10) + "..." : content;
        }

        // TODO: Lưu vào SQLite / Room / SharedPreferences
        // Ví dụ tạm thời dùng Toast
        Toast.makeText(this, "Đã lưu ghi chú:\n" + title, Toast.LENGTH_LONG).show();

        // Quay về trang danh sách
        setResult(RESULT_OK);
        finish();
    }
}