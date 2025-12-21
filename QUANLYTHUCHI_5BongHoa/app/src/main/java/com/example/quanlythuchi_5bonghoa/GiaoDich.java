package com.example.quanlythuchi_5bonghoa;

import java.util.Date;

public class GiaoDich {
    private String tenGiaoDich;
    private double soTien;
    private Date ngayGiaoDich;
    private String tenDanhMuc;
    private String loaiDanhMuc; // 'Thu nhập' or 'Chi tiêu'
    private String bieuTuong;

    public GiaoDich(String tenGiaoDich, double soTien, Date ngayGiaoDich, String tenDanhMuc, String loaiDanhMuc, String bieuTuong) {
        this.tenGiaoDich = tenGiaoDich;
        this.soTien = soTien;
        this.ngayGiaoDich = ngayGiaoDich;
        this.tenDanhMuc = tenDanhMuc;
        this.loaiDanhMuc = loaiDanhMuc;
        this.bieuTuong = bieuTuong;
    }

    // Getters for all fields
    public String getTenGiaoDich() { return tenGiaoDich; }
    public double getSoTien() { return soTien; }
    public Date getNgayGiaoDich() { return ngayGiaoDich; }
    public String getTenDanhMuc() { return tenDanhMuc; }
    public String getLoaiDanhMuc() { return loaiDanhMuc; }
    public String getBieuTuong() { return bieuTuong; }
}
