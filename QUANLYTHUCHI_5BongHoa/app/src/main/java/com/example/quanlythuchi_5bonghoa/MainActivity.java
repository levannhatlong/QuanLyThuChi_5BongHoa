package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ĐẶT Ở ĐÂY - TRƯỚC setContentView()
        setAppLanguage();

        EdgeToEdge.enable(this);
        setContentView(R.layout.dangnhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setAppLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "vi"); // Sửa "v1" thành "vi"

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
