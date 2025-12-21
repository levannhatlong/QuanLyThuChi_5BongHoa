package com.example.quanlythuchi_5bonghoa.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.quanlythuchi_5bonghoa.NguoiDung;

@Dao
public interface NguoiDungDao {
    @Insert
    void insert(NguoiDung nguoiDung);

    @Query("SELECT * FROM NguoiDung WHERE TenDangNhap = :username AND MatKhau = :password")
    NguoiDung login(String username, String password);
}
