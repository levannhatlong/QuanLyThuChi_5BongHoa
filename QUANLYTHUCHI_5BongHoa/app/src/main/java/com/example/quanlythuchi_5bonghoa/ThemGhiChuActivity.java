package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ThemGhiChuActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private TextView tvHeader;
    private int userId;
    private int maGhiChu = -1; // -1 = thêm mới, > 0 = sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_ghi_chu);

        // Lấy user ID
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        loadEditData();
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        tvHeader = findViewById(R.id.tvHeader);

        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút Lưu
        findViewById(R.id.btnSave).setOnClickListener(v -> luuGhiChu());

        // Nút Hủy
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
    }

    private void loadEditData() {
        // Kiểm tra xem có đang sửa ghi chú không
        maGhiChu = getIntent().getIntExtra("ma_ghi_chu", -1);
        
        if (maGhiChu > 0) {
            // Chế độ sửa
            tvHeader.setText("SỬA GHI CHÚ");
            String tieuDe = getIntent().getStringExtra("tieu_de");
            String noiDung = getIntent().getStringExtra("noi_dung");
            
            if (tieuDe != null) etTitle.setText(tieuDe);
            if (noiDung != null) etContent.setText(noiDung);
        } else {
            tvHeader.setText("THÊM GHI CHÚ");
        }
    }

    private void luuGhiChu() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung ghi chú", Toast.LENGTH_SHORT).show();
            etContent.requestFocus();
            return;
        }

        // Nếu tiêu đề trống → tự sinh tiêu đề từ 20 ký tự đầu nội dung
        if (title.isEmpty()) {
            title = content.length() > 20 ? content.substring(0, 20) + "..." : content;
        }

        final String finalTitle = title;

        new Thread(() -> {
            boolean success;
            if (maGhiChu > 0) {
                // Cập nhật
                success = GhiChuRepository.updateNote(maGhiChu, finalTitle, content);
            } else {
                // Thêm mới
                success = GhiChuRepository.addNote(userId, finalTitle, content);
            }

            runOnUiThread(() -> {
                if (success) {
                    String msg = maGhiChu > 0 ? "Đã cập nhật ghi chú" : "Đã lưu ghi chú";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Lỗi khi lưu ghi chú", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
