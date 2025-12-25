package com.example.quanlythuchi_5bonghoa;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ThemGiaoDichActivity extends AppCompatActivity {

    private TextInputEditText editTenGiaoDich, editSoTien, editNgayGiaoDich, editGhiChu;
    private AutoCompleteTextView autoCompleteTheLoai;
    private TextInputLayout layoutNgayGiaoDich;
    private RadioGroup radioGroupLoai;
    private RadioButton radioChiTieu, radioThuNhap;
    private MaterialButton btnSave;
    private ImageView ivBack, ivReset, ivCalendar;

    private Calendar myCalendar = Calendar.getInstance();
    private int currentUserId;

    private Map<String, Integer> danhMucMap = new HashMap<>();
    private List<String> danhMucChiTieu = new ArrayList<>();
    private List<String> danhMucThuNhap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_giao_dich);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Lỗi: không tìm thấy người dùng.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initViews();
        loadDanhMucFromDatabase();
        setupListeners();
        setDefaultDate();
    }

    private void initViews() {
        editTenGiaoDich = findViewById(R.id.edit_ten_giao_dich);
        editSoTien = findViewById(R.id.edit_so_tien);
        editNgayGiaoDich = findViewById(R.id.edit_ngay_giao_dich);
        editGhiChu = findViewById(R.id.edit_ghi_chu);
        autoCompleteTheLoai = findViewById(R.id.auto_complete_the_loai);
        layoutNgayGiaoDich = findViewById(R.id.layout_ngay_giao_dich);
        radioGroupLoai = findViewById(R.id.radio_group_loai);
        radioChiTieu = findViewById(R.id.radio_chi_tieu);
        radioThuNhap = findViewById(R.id.radio_thu_nhap);
        btnSave = findViewById(R.id.btn_save);
        ivBack = findViewById(R.id.iv_back);
        ivReset = findViewById(R.id.iv_reset);
        ivCalendar = findViewById(R.id.iv_calendar);
    }

    private void setupListeners() {
        // Back button
        ivBack.setOnClickListener(v -> finish());

        // Reset button
        ivReset.setOnClickListener(v -> resetForm());

        radioGroupLoai.setOnCheckedChangeListener((group, checkedId) -> {
            updateDanhMucDropdown();
        });

        ivCalendar.setOnClickListener(v -> showDatePicker());
        editNgayGiaoDich.setOnClickListener(v -> showDatePicker());

        // Save button
        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                saveGiaoDichToDatabase();
            }
        });
    }

    private void setDefaultDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        editNgayGiaoDich.setText(sdf.format(myCalendar.getTime()));
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, month);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    editNgayGiaoDich.setText(sdf.format(myCalendar.getTime()));
                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void loadDanhMucFromDatabase() {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) {
                runOnUiThread(() -> Toast.makeText(this, "Không thể kết nối database", Toast.LENGTH_SHORT).show());
                return;
            }

            try {
                String query = "SELECT MaDanhMuc, TenDanhMuc, LoaiDanhMuc FROM DanhMuc " +
                              "WHERE MaNguoiDung IS NULL OR MaNguoiDung = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, currentUserId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int maDanhMuc = rs.getInt("MaDanhMuc");
                    String tenDanhMuc = rs.getString("TenDanhMuc");
                    String loaiDanhMuc = rs.getString("LoaiDanhMuc");

                    danhMucMap.put(tenDanhMuc, maDanhMuc);

                    if ("Chi tiêu".equals(loaiDanhMuc)) {
                        danhMucChiTieu.add(tenDanhMuc);
                    } else if ("Thu nhập".equals(loaiDanhMuc)) {
                        danhMucThuNhap.add(tenDanhMuc);
                    }
                }
                rs.close();
                stmt.close();

                runOnUiThread(this::updateDanhMucDropdown);

            } catch (SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi tải danh mục", Toast.LENGTH_SHORT).show());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateDanhMucDropdown() {
        List<String> currentList = radioChiTieu.isChecked() ? danhMucChiTieu : danhMucThuNhap;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_dropdown_item_1line, currentList);
        autoCompleteTheLoai.setAdapter(adapter);
        autoCompleteTheLoai.setText("", false);
    }

    private void resetForm() {
        editTenGiaoDich.setText("");
        editSoTien.setText("");
        editGhiChu.setText("");
        autoCompleteTheLoai.setText("", false);
        radioChiTieu.setChecked(true);
        setDefaultDate();
        Toast.makeText(this, "Đã đặt lại biểu mẫu", Toast.LENGTH_SHORT).show();
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(autoCompleteTheLoai.getText().toString())) {
            autoCompleteTheLoai.setError("Vui lòng chọn danh mục");
            return false;
        }

        if (TextUtils.isEmpty(editTenGiaoDich.getText())) {
            editTenGiaoDich.setError("Tên giao dịch là bắt buộc");
            return false;
        }

        if (TextUtils.isEmpty(editSoTien.getText())) {
            editSoTien.setError("Số tiền là bắt buộc");
            return false;
        }

        try {
            double soTien = Double.parseDouble(editSoTien.getText().toString());
            if (soTien <= 0) {
                editSoTien.setError("Số tiền phải lớn hơn 0");
                return false;
            }
        } catch (NumberFormatException e) {
            editSoTien.setError("Số tiền không hợp lệ");
            return false;
        }

        if (TextUtils.isEmpty(editNgayGiaoDich.getText())) {
            editNgayGiaoDich.setError("Ngày giao dịch là bắt buộc");
            return false;
        }

        return true;
    }

    private void saveGiaoDichToDatabase() {
        String tenGiaoDich = editTenGiaoDich.getText().toString().trim();
        double soTien = Double.parseDouble(editSoTien.getText().toString().trim());
        String ghiChu = editGhiChu.getText() != null ? editGhiChu.getText().toString().trim() : "";
        String tenDanhMuc = autoCompleteTheLoai.getText().toString();
        Integer maDanhMuc = danhMucMap.get(tenDanhMuc);

        if (maDanhMuc == null) {
            Toast.makeText(this, "Danh mục không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setEnabled(false);
        btnSave.setText("Đang lưu...");

        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không thể kết nối database", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    btnSave.setText("Lưu giao dịch");
                });
                return;
            }

            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String ngayGiaoDichStr = outputFormat.format(inputFormat.parse(editNgayGiaoDich.getText().toString()));

                String insertQuery = "INSERT INTO GiaoDich (MaNguoiDung, MaDanhMuc, TenGiaoDich, SoTien, NgayGiaoDich, GhiChu) " +
                                    "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(insertQuery);
                stmt.setInt(1, currentUserId);
                stmt.setInt(2, maDanhMuc);
                stmt.setString(3, tenGiaoDich);
                stmt.setDouble(4, soTien);
                stmt.setString(5, ngayGiaoDichStr);
                stmt.setString(6, ghiChu);

                int rowsInserted = stmt.executeUpdate();
                stmt.close();

                if (rowsInserted > 0 && radioChiTieu.isChecked()) {
                    checkAndCreateWarningNotification(connection);
                }

                runOnUiThread(() -> {
                    if (rowsInserted > 0) {
                        Toast.makeText(this, "Thêm giao dịch thành công!", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(this, "Lỗi khi thêm giao dịch", Toast.LENGTH_SHORT).show();
                        btnSave.setEnabled(true);
                        btnSave.setText("Lưu giao dịch");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    btnSave.setText("Lưu giao dịch");
                });
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void checkAndCreateWarningNotification(Connection existingConn) {
        try {
            CanhBao canhBao = CanhBaoRepository.getCanhBao(currentUserId);
            
            if (canhBao != null && canhBao.isTrangThai() && canhBao.getHanMuc() > 0) {
                // Lấy tổng chi tiêu theo chu kỳ
                double tongChiTieu = CanhBaoRepository.getTongChiTieuTheoChuKy(
                        currentUserId, canhBao.getChuKy());
                
                // Kiểm tra vượt mức
                if (tongChiTieu > canhBao.getHanMuc()) {
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    
                    String tieuDe = "⚠️ Cảnh báo vượt mức chi tiêu!";
                    String noiDung = String.format(
                            "Bạn đã chi tiêu %s, vượt quá hạn mức %s (%s) đã đặt.\n\n" +
                            "Vượt mức: %s\n\n" +
                            "Hãy cân nhắc điều chỉnh chi tiêu của bạn.",
                            formatter.format(tongChiTieu),
                            formatter.format(canhBao.getHanMuc()),
                            canhBao.getChuKy().toLowerCase(),
                            formatter.format(tongChiTieu - canhBao.getHanMuc())
                    );
                    
                    // Tạo thông báo
                    ThongBaoRepository.createNotification(currentUserId, tieuDe, noiDung, "canh_bao");
                    
                    runOnUiThread(() -> {
                        Toast.makeText(this, "⚠️ Chi tiêu đã vượt mức cảnh báo!", 
                                Toast.LENGTH_LONG).show();
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}