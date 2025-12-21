package com.example.quanlythuchi_5bonghoa;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DanhMuc")
public class DanhMuc {
    @PrimaryKey(autoGenerate = true)
    public int MaDanhMuc;
    public Integer MaNguoiDung;
    public String TenDanhMuc;
    public String LoaiDanhMuc;
    public String MoTa;
    public String BieuTuong;
}
