package com.example.quanlythuchi_5bonghoa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class ThemSuaDanhMucActivity extends AppCompatActivity {

    private ImageView btnBack, btnSave;
    private TextView tvTitle, tvCharCount;
    private MaterialCardView cardExpenseType, cardIncomeType;
    private EditText edtTenDanhMuc, edtMoTa;
    private RecyclerView recyclerIcons, recyclerColors;
    
    // Preview elements
    private MaterialCardView cardPreviewIcon, cardPreviewBadge;
    private ImageView ivPreviewIcon;
    private TextView tvPreviewName, tvPreviewDescription, tvPreviewBadge;

    // Suggestion chips
    private com.google.android.material.chip.Chip chipSuggestion1, chipSuggestion2, chipSuggestion3;

    // Selection variables
    private boolean isExpenseSelected = true;
    private int selectedIconResId = R.drawable.ic_food_modern;
    private String selectedColor = "#0EA5E9";
    
    // Adapters
    private IconSelectorAdapter iconAdapter;
    private ColorSelectorAdapter colorAdapter;

    // Data arrays
    private final int[] iconList = {
            R.drawable.ic_food_modern,
            R.drawable.ic_transport_modern,
            R.drawable.ic_shopping_modern,
            R.drawable.ic_entertainment_modern,
            R.drawable.ic_bills_modern,
            R.drawable.ic_salary_modern,
            R.drawable.ic_investment_modern,
            R.drawable.ic_health_modern,
            R.drawable.ic_education_modern,
            R.drawable.ic_travel_modern
    };

    private final String[] colorList = {
            "#0EA5E9",  // Màu chủ đạo cũ
            "#E53935",  // Đỏ
            "#43A047",  // Xanh lá
            "#FB8C00",  // Cam
            "#8E24AA",  // Tím
            "#EC407A",  // Hồng
            "#1976D2",  // Xanh dương
            "#795548",  // Nâu
            "#607D8B",  // Xanh xám
            "#FF5722"   // Cam đậm
    };

    // Edit mode variables
    private boolean isEditMode = false;
    private int editCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_danh_muc);

        initViews();
        setupListeners();
        setupRecyclerViews();
        checkEditMode();
        updatePreview();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        tvTitle = findViewById(R.id.tvTitle);
        // tvCharCount = findViewById(R.id.tvCharCount);
        cardExpenseType = findViewById(R.id.cardExpenseType);
        cardIncomeType = findViewById(R.id.cardIncomeType);
        edtTenDanhMuc = findViewById(R.id.edtTenDanhMuc);
        edtMoTa = findViewById(R.id.edtMoTa);
        recyclerIcons = findViewById(R.id.recyclerIcons);
        recyclerColors = findViewById(R.id.recyclerColors);
        
        // Preview elements
        cardPreviewIcon = findViewById(R.id.cardPreviewIcon);
        cardPreviewBadge = findViewById(R.id.cardPreviewBadge);
        ivPreviewIcon = findViewById(R.id.ivPreviewIcon);
        tvPreviewName = findViewById(R.id.tvPreviewName);
        tvPreviewDescription = findViewById(R.id.tvPreviewDescription);
        tvPreviewBadge = findViewById(R.id.tvPreviewBadge);
        
        // Suggestion chips
        // chipSuggestion1 = findViewById(R.id.chipSuggestion1);
        // chipSuggestion2 = findViewById(R.id.chipSuggestion2);
        // chipSuggestion3 = findViewById(R.id.chipSuggestion3);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnSave.setOnClickListener(v -> saveCategory());

        // Type selection
        cardExpenseType.setOnClickListener(v -> {
            isExpenseSelected = true;
            updateTypeSelection();
            updatePreview();
        });

        cardIncomeType.setOnClickListener(v -> {
            isExpenseSelected = false;
            updateTypeSelection();
            updatePreview();
        });

        // Text watchers for preview
        edtTenDanhMuc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePreview();
                // updateCharCount();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edtMoTa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePreview();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Suggestion chip listeners
        /*
        chipSuggestion1.setOnClickListener(v -> {
            String currentText = edtMoTa.getText().toString();
            if (currentText.isEmpty()) {
                edtMoTa.setText("Chi phí hàng ngày");
            } else {
                edtMoTa.setText(currentText + " - Chi phí hàng ngày");
            }
        });

        chipSuggestion2.setOnClickListener(v -> {
            String currentText = edtMoTa.getText().toString();
            if (currentText.isEmpty()) {
                edtMoTa.setText("Cần thiết");
            } else {
                edtMoTa.setText(currentText + " - Cần thiết");
            }
        });

        chipSuggestion3.setOnClickListener(v -> {
            String currentText = edtMoTa.getText().toString();
            if (currentText.isEmpty()) {
                edtMoTa.setText("Thường xuyên");
            } else {
                edtMoTa.setText(currentText + " - Thường xuyên");
            }
        });
        */
    }

    private void setupRecyclerViews() {
        // Icon RecyclerView
        recyclerIcons.setLayoutManager(new GridLayoutManager(this, 5));
        iconAdapter = new IconSelectorAdapter(iconList, selectedIconResId, new IconSelectorAdapter.OnIconSelectedListener() {
            @Override
            public void onIconSelected(int iconResId) {
                selectedIconResId = iconResId;
                updatePreview();
            }
        });
        recyclerIcons.setAdapter(iconAdapter);

        // Color RecyclerView
        recyclerColors.setLayoutManager(new GridLayoutManager(this, 5));
        colorAdapter = new ColorSelectorAdapter(colorList, selectedColor, new ColorSelectorAdapter.OnColorSelectedListener() {
            @Override
            public void onColorSelected(String color) {
                selectedColor = color;
                updatePreview();
            }
        });
        recyclerColors.setAdapter(colorAdapter);
    }

    private void checkEditMode() {
        Intent intent = getIntent();
        if (intent.hasExtra("EDIT_MODE")) {
            isEditMode = intent.getBooleanExtra("EDIT_MODE", false);
            editCategoryId = intent.getIntExtra("CATEGORY_ID", -1);
            
            if (isEditMode) {
                tvTitle.setText("Sửa danh mục");
                
                // Load category data
                String categoryName = intent.getStringExtra("CATEGORY_NAME");
                String categoryDesc = intent.getStringExtra("CATEGORY_DESC");
                selectedIconResId = intent.getIntExtra("CATEGORY_ICON", R.drawable.ic_food_modern);
                selectedColor = intent.getStringExtra("CATEGORY_COLOR");
                isExpenseSelected = intent.getBooleanExtra("IS_EXPENSE", true);
                
                edtTenDanhMuc.setText(categoryName);
                edtMoTa.setText(categoryDesc);
                
                // Update adapters
                iconAdapter.updateSelectedIcon(selectedIconResId);
                colorAdapter.updateSelectedColor(selectedColor);
            }
        }
        
        updateTypeSelection();
    }

    private void updateCharCount() {
        /*
        int length = edtTenDanhMuc.getText().toString().length();
        tvCharCount.setText(length + "/20");
        
        if (length > 15) {
            tvCharCount.setTextColor(Color.parseColor("#E53935"));
        } else {
            tvCharCount.setTextColor(Color.parseColor("#757575"));
        }
        */
    }

    private void updateTypeSelection() {
        if (isExpenseSelected) {
            // Expense selected
            cardExpenseType.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
            cardExpenseType.setStrokeColor(Color.parseColor("#E53935"));
            cardExpenseType.setStrokeWidth(4);
            
            cardIncomeType.setCardBackgroundColor(Color.parseColor("#F0F0F0"));
            cardIncomeType.setStrokeColor(Color.parseColor("#CCCCCC"));
            cardIncomeType.setStrokeWidth(2);
        } else {
            // Income selected
            cardIncomeType.setCardBackgroundColor(Color.parseColor("#E8F5E8"));
            cardIncomeType.setStrokeColor(Color.parseColor("#43A047"));
            cardIncomeType.setStrokeWidth(4);
            
            cardExpenseType.setCardBackgroundColor(Color.parseColor("#F0F0F0"));
            cardExpenseType.setStrokeColor(Color.parseColor("#CCCCCC"));
            cardExpenseType.setStrokeWidth(2);
        }
    }

    private void updatePreview() {
        // Update preview name
        String name = edtTenDanhMuc.getText().toString().trim();
        if (name.isEmpty()) {
            tvPreviewName.setText("Tên danh mục");
        } else {
            tvPreviewName.setText(name);
        }

        // Update preview description
        String desc = edtMoTa.getText().toString().trim();
        if (desc.isEmpty()) {
            tvPreviewDescription.setText("Mô tả danh mục");
        } else {
            tvPreviewDescription.setText(desc);
        }

        // Update preview icon
        ivPreviewIcon.setImageResource(selectedIconResId);
        try {
            int color = Color.parseColor(selectedColor);
            cardPreviewIcon.setCardBackgroundColor(adjustAlpha(color, 0.15f));
            ivPreviewIcon.setColorFilter(color);
        } catch (Exception e) {
            cardPreviewIcon.setCardBackgroundColor(Color.parseColor("#E8F4F8"));
            ivPreviewIcon.setColorFilter(Color.parseColor("#0EA5E9"));
        }

        // Update preview badge
        if (isExpenseSelected) {
            tvPreviewBadge.setText("Chi tiêu");
            tvPreviewBadge.setTextColor(Color.parseColor("#E53935"));
            cardPreviewBadge.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
        } else {
            tvPreviewBadge.setText("Thu nhập");
            tvPreviewBadge.setTextColor(Color.parseColor("#43A047"));
            cardPreviewBadge.setCardBackgroundColor(Color.parseColor("#E8F5E8"));
        }
    }

    private void saveCategory() {
        String name = edtTenDanhMuc.getText().toString().trim();
        String desc = edtMoTa.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
            edtTenDanhMuc.requestFocus();
            return;
        }

        DatabaseHelper dbHelper = null;
        try {
            // Kiểm tra tên danh mục đã tồn tại chưa
            dbHelper = new DatabaseHelper(this);
            boolean nameExists = dbHelper.isCategoryNameExists(name, isExpenseSelected, isEditMode ? editCategoryId : -1);
            
            if (nameExists) {
                Toast.makeText(this, "Tên danh mục đã tồn tại. Vui lòng chọn tên khác.", Toast.LENGTH_SHORT).show();
                edtTenDanhMuc.requestFocus();
                edtTenDanhMuc.setError("Tên danh mục đã tồn tại");
                return;
            }
            
            Intent resultIntent = new Intent();
            resultIntent.putExtra("CATEGORY_NAME", name);
            resultIntent.putExtra("CATEGORY_DESC", desc);
            resultIntent.putExtra("CATEGORY_ICON", selectedIconResId);
            resultIntent.putExtra("CATEGORY_COLOR", selectedColor);
            resultIntent.putExtra("IS_EXPENSE", isExpenseSelected);
            
            if (isEditMode) {
                resultIntent.putExtra("CATEGORY_ID", editCategoryId);
                Toast.makeText(this, "Đã cập nhật danh mục", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đã thêm danh mục mới", Toast.LENGTH_SHORT).show();
            }
            
            setResult(RESULT_OK, resultIntent);
            finish();
            
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi kiểm tra dữ liệu. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        } finally {
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
    }

    // Helper method để tạo màu nhạt hơn
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}