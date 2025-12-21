package com.example.quanlythuchi_5bonghoa.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import com.example.quanlythuchi_5bonghoa.DanhMuc;

@Dao
public interface DanhMucDao {
    @Insert
    void insert(DanhMuc danhMuc);
}
