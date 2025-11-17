package com.example.quanlythuchi_5bonghoa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ThemGiaoDichActivity extends AppCompatActivity {

    private EditText editTenGiaoDich, editSoTien, editNgayGiaoDich, editGhiChu;
    private Spinner spinnerTheLoai;
    private FloatingActionButton fabSave;
    private ImageView ivCancel, ivReset;
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_giao_dich);

        // Find views
        editTenGiaoDich = findViewById(R.id.edit_ten_giao_dich);
        editSoTien = findViewById(R.id.edit_so_tien);
        editNgayGiaoDich = findViewById(R.id.edit_ngay_giao_dich);
        editGhiChu = findViewById(R.id.edit_ghi_chu);
        spinnerTheLoai = findViewById(R.id.spinner_the_loai);
        fabSave = findViewById(R.id.fab_save);
        ivCancel = findViewById(R.id.iv_cancel);
        ivReset = findViewById(R.id.iv_reset);

        // Date picker setup
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        editNgayGiaoDich.setOnClickListener(v -> 
            new DatePickerDialog(ThemGiaoDichActivity.this, date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        );

        // Button click listeners
        fabSave.setOnClickListener(v -> {
            if (validateInput()) {
                // In a real app, you would save the data here.
                Toast.makeText(ThemGiaoDichActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ThemGiaoDichActivity.this, ThongKeActivity.class);
                startActivity(intent);
            }
        });

        ivCancel.setOnClickListener(v -> finish());

        ivReset.setOnClickListener(v -> resetForm());
    }
    
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editNgayGiaoDich.setText(sdf.format(myCalendar.getTime()));
    }

    private void resetForm() {
        editTenGiaoDich.setText("");
        editSoTien.setText("");
        editNgayGiaoDich.setText("");
        editGhiChu.setText("");
        spinnerTheLoai.setSelection(0);
        // You might want to reset the RadioGroup as well, e.g., ((RadioButton)findViewById(R.id.radio_tien_ra)).setChecked(true);
        Toast.makeText(this, "Đã đặt lại biểu mẫu", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInput() {
        if (spinnerTheLoai.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Vui lòng chọn thể loại", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(editTenGiaoDich.getText().toString().trim())) {
            editTenGiaoDich.setError("Tên giao dịch là bắt buộc");
            return false;
        }

        if (TextUtils.isEmpty(editSoTien.getText().toString().trim())) {
            editSoTien.setError("Số tiền là bắt buộc");
            return false;
        }

        if (TextUtils.isEmpty(editNgayGiaoDich.getText().toString().trim())) {
            editNgayGiaoDich.setError("Ngày giao dịch là bắt buộc");
            // Since we use a DatePickerDialog, this check might be redundant
            // but it's good for robustness.
            return false;
        }

        return true;
    }
}
