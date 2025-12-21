package com.example.quanlythuchi_5bonghoa;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "LienKetNganHang")
public class LienKetNganHang {
    @PrimaryKey(autoGenerate = true)
    public int MaLienKet;
    public int MaNguoiDung;
    public String TenNganHang;
    public String SoTaiKhoan;
    public String ChuTaiKhoan;
    public String SoCCCD;
    public String NoiCap;
    public String NgayCap;
}
