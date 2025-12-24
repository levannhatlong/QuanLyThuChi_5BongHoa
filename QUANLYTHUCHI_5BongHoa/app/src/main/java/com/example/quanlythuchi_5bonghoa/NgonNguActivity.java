package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Locale;

public class NgonNguActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView btnSave, btnDiscard;
    private TextView tvCurrentLanguage;
    private TextView tvCurrentFlag;

    private LinearLayout itemVietnamese, itemEnglish;
    private CardView cardVietnamese, cardEnglish;
    private View checkVietnamese, checkEnglish;

    private SharedPreferences sharedPreferences;
    private String selectedLanguage = "vi";
    private String originalLanguage = "vi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySavedLanguage();
        setContentView(R.layout.activity_ngon_ngu);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        initViews();
        loadSavedLanguage();
        setupListeners();
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
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnDiscard = findViewById(R.id.btnDiscard);
        tvCurrentLanguage = findViewById(R.id.tvCurrentLanguage);
        tvCurrentFlag = findViewById(R.id.tvCurrentFlag);

        itemVietnamese = findViewById(R.id.itemVietnamese);
        itemEnglish = findViewById(R.id.itemEnglish);
        cardVietnamese = findViewById(R.id.cardVietnamese);
        cardEnglish = findViewById(R.id.cardEnglish);
        checkVietnamese = findViewById(R.id.checkVietnamese);
        checkEnglish = findViewById(R.id.checkEnglish);
    }

    private void loadSavedLanguage() {
        String savedLanguage = sharedPreferences.getString("language", "vi");
        originalLanguage = savedLanguage;
        selectedLanguage = savedLanguage;

        updateCurrentLanguageDisplay();
        updateSelection();
    }

    private void updateCurrentLanguageDisplay() {
        if ("vi".equals(selectedLanguage)) {
            tvCurrentLanguage.setText(R.string.vietnamese);
            tvCurrentFlag.setText("🇻🇳");
        } else {
            tvCurrentLanguage.setText(R.string.english);
            tvCurrentFlag.setText("🇺🇸");
        }
    }

    private void updateSelection() {
        if ("vi".equals(selectedLanguage)) {
            // Vietnamese selected
            itemVietnamese.setSelected(true);
            itemEnglish.setSelected(false);
            checkVietnamese.setVisibility(View.VISIBLE);
            checkEnglish.setVisibility(View.GONE);

            // Update card elevation for visual feedback
            cardVietnamese.setCardElevation(8f);
            cardEnglish.setCardElevation(2f);
        } else {
            // English selected
            itemVietnamese.setSelected(false);
            itemEnglish.setSelected(true);
            checkVietnamese.setVisibility(View.GONE);
            checkEnglish.setVisibility(View.VISIBLE);

            // Update card elevation for visual feedback
            cardVietnamese.setCardElevation(2f);
            cardEnglish.setCardElevation(8f);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Click on Vietnamese card
        cardVietnamese.setOnClickListener(v -> {
            selectedLanguage = "vi";
            updateCurrentLanguageDisplay();
            updateSelection();
        });

        itemVietnamese.setOnClickListener(v -> {
            selectedLanguage = "vi";
            updateCurrentLanguageDisplay();
            updateSelection();
        });

        // Click on English card
        cardEnglish.setOnClickListener(v -> {
            selectedLanguage = "en";
            updateCurrentLanguageDisplay();
            updateSelection();
        });

        itemEnglish.setOnClickListener(v -> {
            selectedLanguage = "en";
            updateCurrentLanguageDisplay();
            updateSelection();
        });

        // Cancel button
        btnDiscard.setOnClickListener(v -> {
            selectedLanguage = originalLanguage;
            updateCurrentLanguageDisplay();
            updateSelection();
            Toast.makeText(this, R.string.changes_cancelled, Toast.LENGTH_SHORT).show();
            finish();
        });

        // Save button
        btnSave.setOnClickListener(v -> saveLanguageSettings());
    }

    private void saveLanguageSettings() {
        // Save to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", selectedLanguage);
        editor.apply();

        String languageName = selectedLanguage.equals("vi") ? getString(R.string.vietnamese) : getString(R.string.english);
        Toast.makeText(this, getString(R.string.saved) + ": " + languageName, Toast.LENGTH_SHORT).show();

        if (!selectedLanguage.equals(originalLanguage)) {
            // Apply new language
            setAppLanguage(selectedLanguage);

            Toast.makeText(this, R.string.restarting_app, Toast.LENGTH_SHORT).show();

            // Restart app to apply language change
            restartApp();
        } else {
            finish();
        }
    }

    private void restartApp() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            recreate();
        }
    }

    @Override
    public void onBackPressed() {
        if (!selectedLanguage.equals(originalLanguage)) {
            // Show confirmation if there are unsaved changes
            Toast.makeText(this, R.string.unsaved_changes, Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }
}
