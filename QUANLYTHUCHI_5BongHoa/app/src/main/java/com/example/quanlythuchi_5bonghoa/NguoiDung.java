package com.example.quanlythuchi_5bonghoa;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "NguoiDung")
public class NguoiDung {
    @PrimaryKey(autoGenerate = true)
    public int MaNguoiDung;
    public String TenDangNhap;
    public String MatKhau;
    public String HoTen;
    public String EmailSoDienThoai;
    public String NgaySinh;
    public String AnhDaiDien;
    public String NgayTao;
}
