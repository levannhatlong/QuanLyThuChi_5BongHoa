package com.example.quanlythuchi_5bonghoa;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class CaiDatCanhBao extends AppCompatActivity {

    private SeekBar seekBarPrice;
    private EditText edtPrice;
    private ImageView ivConfirmPrice;
    private TextView tvResetDate;
    private LinearLayout layoutResetDate;
    private TextView btnConfirm, btnCancel;
    private RadioGroup radioGroupCanhBao;
    private ImageView btnBack;

    private boolean isUpdatingFromCode = false; // Flag to prevent infinite loops

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat_canh_bao);

        initViews();
        setupInteraction();
        setupDatePicker();
        setupButtons();
    }

    private void initViews() {
        seekBarPrice = findViewById(R.id.seekBarPrice);
        edtPrice = findViewById(R.id.edtPrice);
        ivConfirmPrice = findViewById(R.id.ivConfirmPrice);
        tvResetDate = findViewById(R.id.tvResetDate);
        layoutResetDate = findViewById(R.id.layoutResetDate);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        radioGroupCanhBao = findViewById(R.id.radioGroupCanhBao);
        btnBack = findViewById(R.id.btnBack);

        long initialPrice = 13_000_000L;
        seekBarPrice.setMax(500);
        seekBarPrice.setProgress((int) (initialPrice / 1_000_000L));
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        edtPrice.setText(formatter.format(initialPrice));
    }

    private void setupInteraction() {
        // 1. When user drags SeekBar
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    isUpdatingFromCode = true; // Set flag
                    long price = (long) progress * 1_000_000L;
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    edtPrice.setText(formatter.format(price));
                    ivConfirmPrice.setVisibility(View.GONE); // Hide confirm button
                    isUpdatingFromCode = false; // Reset flag
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 2. When user types in EditText
        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isUpdatingFromCode) {
                    ivConfirmPrice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdatingFromCode) return;

                edtPrice.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    if (!originalString.isEmpty()) {
                        String cleanString = originalString.replaceAll("[.,]", "");
                        long longval = Long.parseLong(cleanString);

                        if (longval > 500_000_000L) {
                            longval = 500_000_000L;
                        }

                        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                        String formattedString = formatter.format(longval);
                        edtPrice.setText(formattedString);
                        edtPrice.setSelection(edtPrice.getText().length());
                    }
                } catch (NumberFormatException nfe) {
                    // Ignore - can happen while typing
                }
                edtPrice.addTextChangedListener(this);
            }
        });

        // 3. When user clicks the confirm checkmark
        ivConfirmPrice.setOnClickListener(v -> {
            confirmPriceFromEditText();
        });
    }

    private void confirmPriceFromEditText() {
        String cleanString = edtPrice.getText().toString().replaceAll("[.,]", "");
        long price = 0L;
        if (!cleanString.isEmpty()) {
            try {
                price = Long.parseLong(cleanString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (price > 500_000_000L) {
            price = 500_000_000L;
        }

        int progress = (int) (price / 1_000_000L);
        seekBarPrice.setProgress(progress);
        ivConfirmPrice.setVisibility(View.GONE);
    }

    private void setupDatePicker() {
        layoutResetDate.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(
                CaiDatCanhBao.this,
                (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    tvResetDate.setText("Đặt lại ngày: " + date);
                    tvResetDate.setTextColor(getResources().getColor(android.R.color.black));
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

   private void setupButtons() {
        btnBack.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            if (ivConfirmPrice.getVisibility() == View.VISIBLE) {
                confirmPriceFromEditText();
            }

            int selectedId = radioGroupCanhBao.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn một tùy chọn cảnh báo", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedOption = selectedRadioButton.getText().toString();

            String priceString = edtPrice.getText().toString().replaceAll("[.,]", "");
            long finalPrice = 0L;
            if (!priceString.isEmpty()) {
                finalPrice = Long.parseLong(priceString);
            }

            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("user_id", -1);

            if (userId == -1) {
                Toast.makeText(this, "Lỗi: Không tìm thấy người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
                return;
            }

            long finalPriceForThread = finalPrice;
            String finalSelectedOption = selectedOption;

            new Thread(() -> {
                Connection connection = null;
                PreparedStatement stmt = null;
                try {
                    connection = DBConnect.getConnection();
                    if (connection == null) {
                        runOnUiThread(() -> Toast.makeText(CaiDatCanhBao.this, "Không thể kết nối đến database", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    // First try to UPDATE
                    String updateQuery = "UPDATE canh_bao_settings SET price = ?, type = ? WHERE user_id = ?";
                    stmt = connection.prepareStatement(updateQuery);
                    stmt.setLong(1, finalPriceForThread);
                    stmt.setString(2, finalSelectedOption);
                    stmt.setInt(3, userId);

                    int rowsAffected = stmt.executeUpdate();
                    stmt.close(); // Close the update statement

                    if (rowsAffected == 0) {
                        // If update failed, INSERT
                        String insertQuery = "INSERT INTO canh_bao_settings (user_id, price, type) VALUES (?, ?, ?)";
                        stmt = connection.prepareStatement(insertQuery);
                        stmt.setInt(1, userId);
                        stmt.setLong(2, finalPriceForThread);
                        stmt.setString(3, finalSelectedOption);
                        stmt.executeUpdate();
                    }

                    runOnUiThread(() -> {
                        Toast.makeText(CaiDatCanhBao.this, "Đã lưu cài đặt cảnh báo", Toast.LENGTH_SHORT).show();
                        finish();
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(CaiDatCanhBao.this, "Lỗi khi lưu cài đặt", Toast.LENGTH_SHORT).show());
                } finally {
                    try {
                        if (stmt != null) stmt.close();
                    } catch (SQLException e) { e.printStackTrace(); }
                    try {
                        if (connection != null) connection.close();
                    } catch (SQLException e) { e.printStackTrace(); }
                }
            }).start();
        });

        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Đã hủy", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
