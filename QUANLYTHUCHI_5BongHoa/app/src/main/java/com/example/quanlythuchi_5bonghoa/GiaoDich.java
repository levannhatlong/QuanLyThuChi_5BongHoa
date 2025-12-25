package com.example.quanlythuchi_5bonghoa;

import java.util.Date;

public class GiaoDich {

    public int MaGiaoDich;
    public int MaNguoiDung;
    public int MaDanhMuc;
    public String TenGiaoDich;
    public double SoTien;
    public String NgayGiaoDich;
    public String GhiChu;
    public String AnhHoaDon;
    public boolean TienVao;

    public String TenDanhMuc;
    public String LoaiDanhMuc;
    public String BieuTuong;
    public Date NgayGiaoDichDate;

    public GiaoDich(String tenGiaoDich, double soTien, boolean tienVao) {
        this.TenGiaoDich = tenGiaoDich;
        this.SoTien = soTien;
        this.TienVao = tienVao;
    }

    public GiaoDich(String tenGiaoDich, double soTien, Date ngayGiaoDich, String tenDanhMuc, String loaiDanhMuc, String bieuTuong) {
        this.TenGiaoDich = tenGiaoDich;
        this.SoTien = soTien;
        this.NgayGiaoDichDate = ngayGiaoDich;
        this.TenDanhMuc = tenDanhMuc;
        this.LoaiDanhMuc = loaiDanhMuc;
        this.BieuTuong = bieuTuong;
        this.TienVao = "Thu nhập".equals(loaiDanhMuc);
    }

    public GiaoDich() {}

    // Getters
    public String getTenGiaoDich() {
        return TenGiaoDich;
    }

    public double getSoTien() {
        return SoTien;
    }

    public boolean isTienVao() {
        return TienVao;
    }
    
    public String getTenDanhMuc() {
        return TenDanhMuc;
    }
    
    public String getLoaiDanhMuc() {
        return LoaiDanhMuc;
    }
    
    public String getBieuTuong() {
        return BieuTuong;
    }
    
    public Date getNgayGiaoDichDate() {
        return NgayGiaoDichDate;
    }
}
