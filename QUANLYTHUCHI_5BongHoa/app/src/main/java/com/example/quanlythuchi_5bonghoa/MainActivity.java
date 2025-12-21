package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ĐẶT Ở ĐÂY - TRƯỚC setContentView()
        setAppLanguage();

        EdgeToEdge.enable(this);
        setContentView(R.layout.dangnhap);

        // --- BỔ SUNG: GỌI HÀM KIỂM TRA KẾT NỐI ---
        kiemTraKetNoi();
        // ---------------------------------------

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // --- BỔ SUNG: HÀM KIỂM TRA KẾT NỐI SQL SERVER ---
    private void kiemTraKetNoi() {
        // Chạy kết nối thông qua Class DatabaseConnector đã tạo trước đó
        Connection connection = DatabaseConnector.getConnection();

        if (connection != null) {
            // Thông báo thành công trên màn hình App
            Toast.makeText(this, "Kết nối CSDL 5BongHoa thành công!", Toast.LENGTH_LONG).show();
            try {
                // Đóng kết nối sau khi kiểm tra xong để tiết kiệm tài nguyên
                connection.close();
            } catch (SQLException e) {
                Log.e("SQL_Error", "Lỗi khi đóng kết nối: " + e.getMessage());
            }
        } else {
            // Thông báo thất bại
            Toast.makeText(this, "Kết nối thất bại! Hãy kiểm tra IP và SQL Server.", Toast.LENGTH_LONG).show();
        }
    }

    private void setAppLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "vi");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}