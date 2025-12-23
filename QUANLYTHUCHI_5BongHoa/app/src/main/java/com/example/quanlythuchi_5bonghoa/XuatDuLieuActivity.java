package com.example.quanlythuchi_5bonghoa;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class XuatDuLieuActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvSoLuong, tvKetQua;
    private MaterialButton btnXuatFile;

    private int userId;
    private int soLuongGiaoDich = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuat_du_lieu);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        initViews();
        setupListeners();
        loadSoLuongGiaoDich();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvSoLuong = findViewById(R.id.tvSoLuong);
        tvKetQua = findViewById(R.id.tvKetQua);
        btnXuatFile = findViewById(R.id.btnXuatFile);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnXuatFile.setOnClickListener(v -> xuatFileCSV());
    }

    private void loadSoLuongGiaoDich() {
        new Thread(() -> {
            try {
                Connection conn = DatabaseConnector.getConnection();
                if (conn != null) {
                    String sql = "SELECT COUNT(*) as SoLuong FROM GiaoDich WHERE MaNguoiDung = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        soLuongGiaoDich = rs.getInt("SoLuong");
                    }

                    rs.close();
                    stmt.close();
                    conn.close();

                    runOnUiThread(() -> {
                        tvSoLuong.setText(soLuongGiaoDich + " giao dịch");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void xuatFileCSV() {
        if (soLuongGiaoDich == 0) {
            Toast.makeText(this, "Không có giao dịch để xuất", Toast.LENGTH_SHORT).show();
            return;
        }

        btnXuatFile.setEnabled(false);
        btnXuatFile.setText("Đang xuất...");

        new Thread(() -> {
            try {
                Connection conn = DatabaseConnector.getConnection();
                if (conn != null) {
                    String sql = "SELECT g.TenGiaoDich, g.SoTien, " +
                            "FORMAT(g.NgayGiaoDich, 'dd/MM/yyyy') as NgayGiaoDich, " +
                            "d.TenDanhMuc, d.LoaiDanhMuc, g.GhiChu " +
                            "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                            "WHERE g.MaNguoiDung = ? ORDER BY g.NgayGiaoDich DESC";

                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    ResultSet rs = stmt.executeQuery();

                    // Tạo file CSV
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                            .format(new Date());
                    String fileName = "GiaoDich_" + timestamp + ".csv";

                    StringBuilder csvContent = new StringBuilder();
                    // Header
                    csvContent.append("Tên giao dịch,Số tiền,Ngày,Danh mục,Loại,Ghi chú\n");

                    // Data
                    while (rs.next()) {
                        String ten = rs.getString("TenGiaoDich");
                        double soTien = rs.getDouble("SoTien");
                        String ngay = rs.getString("NgayGiaoDich");
                        String danhMuc = rs.getString("TenDanhMuc");
                        String loai = rs.getString("LoaiDanhMuc");
                        String ghiChu = rs.getString("GhiChu");

                        csvContent.append(String.format("\"%s\",%s,\"%s\",\"%s\",\"%s\",\"%s\"\n",
                                ten != null ? ten : "",
                                soTien,
                                ngay != null ? ngay : "",
                                danhMuc != null ? danhMuc : "",
                                loai != null ? loai : "",
                                ghiChu != null ? ghiChu : ""));
                    }

                    rs.close();
                    stmt.close();
                    conn.close();

                    // Ghi file - sử dụng MediaStore cho Android 10+
                    String filePath;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                        values.put(MediaStore.Downloads.MIME_TYPE, "text/csv");
                        values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                        Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                        if (uri != null) {
                            OutputStream os = getContentResolver().openOutputStream(uri);
                            OutputStreamWriter writer = new OutputStreamWriter(os);
                            writer.write(csvContent.toString());
                            writer.flush();
                            writer.close();
                            os.close();
                        }
                        filePath = "Downloads/" + fileName;
                    } else {
                        File downloadDir = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS);
                        File file = new File(downloadDir, fileName);
                        FileWriter writer = new FileWriter(file);
                        writer.write(csvContent.toString());
                        writer.flush();
                        writer.close();
                        filePath = file.getAbsolutePath();
                    }

                    String finalFileName = fileName;
                    runOnUiThread(() -> {
                        btnXuatFile.setEnabled(true);
                        btnXuatFile.setText("📥 Xuất file CSV");

                        tvKetQua.setVisibility(View.VISIBLE);
                        tvKetQua.setBackgroundColor(0xFFE8F5E9);
                        tvKetQua.setTextColor(0xFF2E7D32);
                        tvKetQua.setText("✅ Xuất thành công!\n\n📁 " + finalFileName + "\n\n📍 Lưu tại: Downloads");

                        Toast.makeText(this, "Đã xuất file thành công!", Toast.LENGTH_LONG).show();
                    });
                } else {
                    runOnUiThread(() -> {
                        btnXuatFile.setEnabled(true);
                        btnXuatFile.setText("📥 Xuất file CSV");
                        Toast.makeText(this, "Không thể kết nối database", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    btnXuatFile.setEnabled(true);
                    btnXuatFile.setText("📥 Xuất file CSV");

                    tvKetQua.setVisibility(View.VISIBLE);
                    tvKetQua.setBackgroundColor(0xFFFFEBEE);
                    tvKetQua.setTextColor(0xFFC62828);
                    tvKetQua.setText("❌ Lỗi: " + e.getMessage());

                    Toast.makeText(this, "Lỗi xuất file", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
