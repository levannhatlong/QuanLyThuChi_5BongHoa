package com.example.quanlythuchi_5bonghoa;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class NgonNguActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView btnSave, btnDiscard;

    // Các item mới
    private LinearLayout itemVietnamese, itemEnglish;
    private View checkVietnamese, checkEnglish;  // dùng View hoặc ImageView đều được

    private SharedPreferences sharedPreferences;
    private String selectedLanguage = "vi";
    private String originalLanguage = "vi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Áp dụng ngôn ngữ đã lưu trước khi setContentView
        applySavedLanguage();
        setContentView(R.layout.activity_ngon_ngu);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        initViews();
        loadSavedLanguage();
        setupEventListeners();
    }

    private void applySavedLanguage() {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String language = prefs.getString("language", "vi");
        setAppLanguage(language);
    }

    private void setAppLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);

        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Cập nhật cả context để Activity hiện tại cũng thay đổi ngay
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnDiscard = findViewById(R.id.btnDiscard);

        itemVietnamese = findViewById(R.id.itemVietnamese);
        itemEnglish = findViewById(R.id.itemEnglish);
        checkVietnamese = findViewById(R.id.checkVietnamese);
        checkEnglish = findViewById(R.id.checkEnglish);
    }

    private void loadSavedLanguage() {
        String savedLanguage = sharedPreferences.getString("language", "vi");
        originalLanguage = savedLanguage;
        selectedLanguage = savedLanguage;
        updateSelection();
    }

    private void updateSelection() {
        if ("vi".equals(selectedLanguage)) {
            itemVietnamese.setSelected(true);
            itemEnglish.setSelected(false);
            checkVietnamese.setVisibility(View.VISIBLE);
            checkEnglish.setVisibility(View.GONE);
        } else {
            itemVietnamese.setSelected(false);
            itemEnglish.setSelected(true);
            checkVietnamese.setVisibility(View.GONE);
            checkEnglish.setVisibility(View.VISIBLE);
        }
    }

    private void setupEventListeners() {
        btnBack.setOnClickListener(v -> finish());

        itemVietnamese.setOnClickListener(v -> {
            selectedLanguage = "vi";
            updateSelection();
        });

        itemEnglish.setOnClickListener(v -> {
            selectedLanguage = "en";
            updateSelection();
        });

        btnDiscard.setOnClickListener(v -> {
            selectedLanguage = originalLanguage;
            updateSelection();
            Toast.makeText(this, "Đã hủy thay đổi", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> saveLanguageSettings());
    }

    // HÀM QUAN TRỌNG ĐÂY NÈ!
    private void saveLanguageSettings() {
        // Lưu vào SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", selectedLanguage);
        editor.apply();

        // Áp dụng ngôn ngữ mới ngay lập tức
        setAppLanguage(selectedLanguage);

        String languageName = selectedLanguage.equals("vi") ? "Tiếng Việt" : "English";
        Toast.makeText(this, "Đã lưu: " + languageName, Toast.LENGTH_SHORT).show();

        // Nếu đổi ngôn ngữ thật thì thông báo cần khởi động lại
        if (!selectedLanguage.equals(originalLanguage)) {
            Toast.makeText(this, "Vui lòng khởi động lại ứng dụng để áp dụng hoàn toàn", Toast.LENGTH_LONG).show();
            recreate(); // Cập nhật giao diện Activity hiện tại ngay
        } else {
            finish();
        }
    }
}