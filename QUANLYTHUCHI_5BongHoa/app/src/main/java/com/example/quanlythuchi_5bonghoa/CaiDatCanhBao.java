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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class CaiDatCanhBao extends AppCompatActivity {

    private SeekBar seekBarPrice;
    private EditText edtPrice;
    private ImageView ivConfirmPrice, btnBack;
    private TextView tvResetDate, btnConfirm, btnCancel, btnDelete;
    private TextView tvCurrentLimit, tvCurrentCycle;
    private CardView cardCurrentAlert;
    private LinearLayout layoutResetDate;
    private RadioGroup radioGroupCanhBao;
    private RadioButton radioNgay, radioThang, radioNam;

    private int userId;
    private String selectedDate = "";
    private boolean isUpdatingFromCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat_canh_bao);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        loadCurrentAlert();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        seekBarPrice = findViewById(R.id.seekBarPrice);
        edtPrice = findViewById(R.id.edtPrice);
        ivConfirmPrice = findViewById(R.id.ivConfirmPrice);
        tvResetDate = findViewById(R.id.tvResetDate);
        layoutResetDate = findViewById(R.id.layoutResetDate);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);
        radioGroupCanhBao = findViewById(R.id.radioGroupCanhBao);
        radioNgay = findViewById(R.id.radioNgay);
        radioThang = findViewById(R.id.radioThang);
        radioNam = findViewById(R.id.radioNam);
        cardCurrentAlert = findViewById(R.id.cardCurrentAlert);
        tvCurrentLimit = findViewById(R.id.tvCurrentLimit);
        tvCurrentCycle = findViewById(R.id.tvCurrentCycle);

        seekBarPrice.setMax(500);
    }

    private void loadCurrentAlert() {
        new Thread(() -> {
            CanhBao cb = CanhBaoRepository.getCanhBao(userId);
            runOnUiThread(() -> {
                if (cb != null) {
                    // Hiển thị thông tin hiện tại
                    cardCurrentAlert.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.VISIBLE);

                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    tvCurrentLimit.setText(formatter.format(cb.getHanMuc()) + " VNĐ");
                    tvCurrentCycle.setText(cb.getChuKy());

                    // Điền vào form
                    long hanMuc = (long) cb.getHanMuc();
                    int progress = (int) (hanMuc / 1_000_000L);
                    seekBarPrice.setProgress(progress);
                    
                    isUpdatingFromCode = true;
                    edtPrice.setText(formatter.format(hanMuc));
                    isUpdatingFromCode = false;

                    // Chọn chu kỳ
                    switch (cb.getChuKy()) {
                        case "Theo ngày":
                            radioNgay.setChecked(true);
                            break;
                        case "Theo năm":
                            radioNam.setChecked(true);
                            break;
                        default:
                            radioThang.setChecked(true);
                    }

                    // Ngày bắt đầu
                    if (cb.getNgayBatDau() != null) {
                        selectedDate = cb.getNgayBatDau();
                        tvResetDate.setText(selectedDate);
                        tvResetDate.setTextColor(getResources().getColor(android.R.color.black));
                    }
                } else {
                    // Giá trị mặc định
                    cardCurrentAlert.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.GONE);
                    seekBarPrice.setProgress(10);
                    
                    isUpdatingFromCode = true;
                    edtPrice.setText("10,000,000");
                    isUpdatingFromCode = false;
                }
            });
        }).start();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        // SeekBar
        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    isUpdatingFromCode = true;
                    long price = (long) progress * 1_000_000L;
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    edtPrice.setText(formatter.format(price));
                    ivConfirmPrice.setVisibility(View.GONE);
                    isUpdatingFromCode = false;
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // EditText
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
                        if (longval > 500_000_000L) longval = 500_000_000L;
                        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                        edtPrice.setText(formatter.format(longval));
                        edtPrice.setSelection(edtPrice.getText().length());
                    }
                } catch (NumberFormatException ignored) {}
                edtPrice.addTextChangedListener(this);
            }
        });

        // Confirm price
        ivConfirmPrice.setOnClickListener(v -> confirmPriceFromEditText());

        // Date picker
        layoutResetDate.setOnClickListener(v -> showDatePicker());

        // Save
        btnConfirm.setOnClickListener(v -> saveAlert());

        // Cancel
        btnCancel.setOnClickListener(v -> finish());

        // Delete
        btnDelete.setOnClickListener(v -> showDeleteConfirm());
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
        if (price > 500_000_000L) price = 500_000_000L;
        int progress = (int) (price / 1_000_000L);
        seekBarPrice.setProgress(progress);
        ivConfirmPrice.setVisibility(View.GONE);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                    tvResetDate.setText(selectedDate);
                    tvResetDate.setTextColor(getResources().getColor(android.R.color.black));
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void saveAlert() {
        if (ivConfirmPrice.getVisibility() == View.VISIBLE) {
            confirmPriceFromEditText();
        }

        String priceString = edtPrice.getText().toString().replaceAll("[.,]", "");
        if (priceString.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập hạn mức", Toast.LENGTH_SHORT).show();
            return;
        }

        long hanMuc = Long.parseLong(priceString);
        
        String chuKy;
        int selectedId = radioGroupCanhBao.getCheckedRadioButtonId();
        if (selectedId == R.id.radioNgay) {
            chuKy = "Theo ngày";
        } else if (selectedId == R.id.radioNam) {
            chuKy = "Theo năm";
        } else {
            chuKy = "Theo tháng";
        }

        new Thread(() -> {
            boolean success = CanhBaoRepository.saveCanhBao(userId, chuKy, hanMuc, selectedDate);
            runOnUiThread(() -> {
                if (success) {
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    Toast.makeText(this, "Đã lưu cảnh báo: " + formatter.format(hanMuc) + " VNĐ/" + chuKy, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Lỗi khi lưu cảnh báo", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void showDeleteConfirm() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa cảnh báo")
                .setMessage("Bạn có chắc muốn xóa cảnh báo chi tiêu này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    new Thread(() -> {
                        boolean success = CanhBaoRepository.deleteCanhBao(userId);
                        runOnUiThread(() -> {
                            if (success) {
                                Toast.makeText(this, "Đã xóa cảnh báo", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
