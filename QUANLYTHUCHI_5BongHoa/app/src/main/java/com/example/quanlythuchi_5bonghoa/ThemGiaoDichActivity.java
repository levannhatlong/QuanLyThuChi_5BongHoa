package com.example.quanlythuchi_5bonghoa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ThemGiaoDichActivity extends AppCompatActivity {

    private EditText editTenGiaoDich, editSoTien, editNgayGiaoDich, editGhiChu, editTenTheLoaiKhac;
    private AutoCompleteTextView autoCompleteTheLoai;
    private TextInputLayout layoutTenTheLoaiKhac;
    private FloatingActionButton fabSave;
    private ImageView ivCancel, ivReset, ivCalendarPicker;
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
        autoCompleteTheLoai = findViewById(R.id.auto_complete_the_loai);
        layoutTenTheLoaiKhac = findViewById(R.id.layout_ten_the_loai_khac);
        editTenTheLoaiKhac = findViewById(R.id.edit_ten_the_loai_khac);
        fabSave = findViewById(R.id.fab_save);
        ivCancel = findViewById(R.id.iv_cancel);
        ivReset = findViewById(R.id.iv_reset);
        ivCalendarPicker = findViewById(R.id.iv_calendar_picker);

        // Spinner setup
        setupCategoryMenu();

        // Date picker setup
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        ivCalendarPicker.setOnClickListener(v ->
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

    private void setupCategoryMenu() {
        // Create a list of categories
        String[] categories = new String[]{"Ăn uống", "Di chuyển", "Hóa đơn", "Mua sắm", "Giải trí", "Khác"};

        // Create an adapter for the menu
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);

        // Set the adapter to the AutoCompleteTextView
        autoCompleteTheLoai.setAdapter(adapter);

        autoCompleteTheLoai.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = (String) parent.getItemAtPosition(position);
            if (selectedCategory.equals("Khác")) {
                layoutTenTheLoaiKhac.setVisibility(View.VISIBLE);
            } else {
                layoutTenTheLoaiKhac.setVisibility(View.GONE);
            }
        });
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
        autoCompleteTheLoai.setText("", false);
        editTenTheLoaiKhac.setText("");
        layoutTenTheLoaiKhac.setVisibility(View.GONE);
        // You might want to reset the RadioGroup as well, e.g., ((RadioButton)findViewById(R.id.radio_tien_ra)).setChecked(true);
        Toast.makeText(this, "Đã đặt lại biểu mẫu", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(autoCompleteTheLoai.getText().toString())) {
            autoCompleteTheLoai.setError("Vui lòng chọn thể loại");
            return false;
        }

        if (autoCompleteTheLoai.getText().toString().equals("Khác") && TextUtils.isEmpty(editTenTheLoaiKhac.getText().toString().trim())) {
            editTenTheLoaiKhac.setError("Vui lòng nhập tên thể loại");
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
            return false;
        }

        return true;
    }
}
