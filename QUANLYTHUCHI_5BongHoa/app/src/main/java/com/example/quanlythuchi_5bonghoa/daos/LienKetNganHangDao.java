package com.example.quanlythuchi_5bonghoa.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import com.example.quanlythuchi_5bonghoa.LienKetNganHang;

@Dao
public interface LienKetNganHangDao {
    @Insert
    void insert(LienKetNganHang lienKetNganHang);
}
