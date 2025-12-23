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

        // Nút Back
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
            etContent.requestFocus();
            return;
        }

        // Nếu tiêu đề trống → tự sinh từ 20 ký tự đầu nội dung
        if (title.isEmpty()) {
            title = content.length() > 20 ? content.substring(0, 20) + "..." : content;
        }

        // TODO: Lưu vào Room hoặc SQLite
        // Ví dụ: GhiChu ghiChu = new GhiChu(0, title, content, maNguoiDung);
        // ghiChuRepository.insert(ghiChu);

        Toast.makeText(this, "Đã lưu ghi chú: " + title, Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK);
        finish();
    }
}