package com.example.quanlythuchi_5bonghoa.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import com.example.quanlythuchi_5bonghoa.ThongBao;

@Dao
public interface ThongBaoDao {
    @Insert
    void insert(ThongBao thongBao);
}
