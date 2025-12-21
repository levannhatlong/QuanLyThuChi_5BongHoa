package com.example.quanlythuchi_5bonghoa;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ThongBao")
public class ThongBao {
    @PrimaryKey(autoGenerate = true)
    public int MaThongBao;
    public int MaNguoiDung;
    public String TieuDe;
    public String NoiDung;
    public boolean DaDoc;
    public String NgayTao;
    public String LoaiThongBao;
}
