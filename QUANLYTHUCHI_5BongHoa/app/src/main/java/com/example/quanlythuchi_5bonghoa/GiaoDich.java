package com.example.quanlythuchi_5bonghoa;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "GiaoDich")
public class GiaoDich {
    @PrimaryKey(autoGenerate = true)
    public int MaGiaoDich;
    public int MaNguoiDung;
    public int MaDanhMuc;
    public String TenGiaoDich;
    public double SoTien;
    public String NgayGiaoDich;
    public String GhiChu;
    public String AnhHoaDon;
}
