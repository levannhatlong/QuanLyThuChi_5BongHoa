package com.example.quanlythuchi_5bonghoa.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import com.example.quanlythuchi_5bonghoa.CanhBaoChiTieu;

@Dao
public interface CanhBaoChiTieuDao {
    @Insert
    void insert(CanhBaoChiTieu canhBaoChiTieu);
}
