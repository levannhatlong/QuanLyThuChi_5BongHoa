package com.example.quanlythuchi_5bonghoa;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CanhBaoChiTieu")
public class CanhBaoChiTieu {
    @PrimaryKey(autoGenerate = true)
    public int MaCanhBao;
    public int MaNguoiDung;
    public String ChuKy;
    public double HanMuc;
    public String NgayBatDau;
    public boolean TrangThai;
}
