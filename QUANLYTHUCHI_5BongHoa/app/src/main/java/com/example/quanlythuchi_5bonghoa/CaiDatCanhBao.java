package com.example.quanlythuchi_5bonghoa;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Calendar;

public class CaiDatCanhBao extends AppCompatActivity {

    private SeekBar seekBarPrice;
    private TextView tvPrice, tvResetDate;
    private LinearLayout layoutResetDate;
    private TextView btnConfirm, btnCancel;
    private CheckBox cbDay, cbMonth, cbYear;

    // Giá trị hiện tại (13 triệu = 13_000_000)
    private long currentPrice = 13_000_000L;
    private final long MAX_PRICE = 500_000_000L; // 500 triệu
    private final DecimalFormat formatter = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cai_dat_canh_bao); // tên file XML của bạn

        initViews();
        setupSeekBar();
        setupDatePicker();
        setupButtons();
        updatePriceText();
    }

    private void initViews() {
        seekBarPrice = findViewById(R.id.seekBarPrice);
        tvPrice = findViewById(R.id.tvPrice);
        tvResetDate = findViewById(R.id.tvResetDate);
        layoutResetDate = findViewById(R.id.layoutResetDate);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        cbDay = findViewById(R.id.cbDay);
        cbMonth = findViewById(R.id.cbMonth);
        cbYear = findViewById(R.id.cbYear);
    }

    private void setupSeekBar() {
        seekBarPrice.setMax(500); // 0 -> 500 (tương ứng 0 -> 500 triệu)
        seekBarPrice.setProgress(13); // 13 triệu

        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentPrice = progress * 1_000_000L; // mỗi bước = 1 triệu
                updatePriceText();
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updatePriceText() {
        tvPrice.setText(formatter.format(currentPrice) + " VND");
    }

    private void setupDatePicker() {
        layoutResetDate.setOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                CaiDatCanhBao.this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvResetDate.setText("Đặt lại ngày: " + date);
                    tvResetDate.setTextColor(getResources().getColor(android.R.color.black));
                },
                year, month, day
        );
        datePicker.show();
    }

    private void setupButtons() {
        btnConfirm.setOnClickListener(v -> {
            // TODO: Lưu cài đặt cảnh báo ở đây
            String loai = "";
            if (cbDay.isChecked()) loai += "Ngày ";
            if (cbMonth.isChecked()) loai += "Tháng ";
            if (cbYear.isChecked()) loai += "Năm ";

            Toast.makeText(this,
                    "Đã đặt cảnh báo: " + formatter.format(currentPrice) + " VND\n"
                            + "Theo: " + (loai.isEmpty() ? "Không chọn" : loai.trim()),
                    Toast.LENGTH_LONG).show();

            finish(); // Quay lại màn trước
        });

        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Đã hủy", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Xử lý nút Back trên header
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}