package com.example.quanlythuchi_5bonghoa.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import com.example.quanlythuchi_5bonghoa.GiaoDich;

@Dao
public interface GiaoDichDao {
    @Insert
    void insert(GiaoDich giaoDich);
}
