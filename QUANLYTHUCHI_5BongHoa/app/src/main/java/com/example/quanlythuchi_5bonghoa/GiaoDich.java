package com.example.quanlythuchi_5bonghoa;

import java.util.Date;

public class GiaoDich {
    private int maGiaoDich;
    private int maNguoiDung;
    private int maDanhMuc;
    private String tenGiaoDich;
    private double soTien;
    private Date ngayGiaoDich;
    private String ghiChu;
    private String anhHoaDon;
    private String tenDanhMuc;
    private String loaiDanhMuc;
    private String bieuTuong;
    private String mauSac;

    // Constructors
    public GiaoDich() {}

    public GiaoDich(String tenGiaoDich, double soTien, boolean tienVao) {
        this.tenGiaoDich = tenGiaoDich;
        this.soTien = soTien;
        this.loaiDanhMuc = tienVao ? "Thu nhập" : "Chi tiêu";
    }

    public GiaoDich(String tenGiaoDich, double soTien, Date ngayGiaoDich, String tenDanhMuc, String loaiDanhMuc, String bieuTuong) {
        this.tenGiaoDich = tenGiaoDich;
        this.soTien = soTien;
        this.ngayGiaoDich = ngayGiaoDich;
        this.tenDanhMuc = tenDanhMuc;
        this.loaiDanhMuc = loaiDanhMuc;
        this.bieuTuong = bieuTuong;
    }

    // Getters
    public int getMaGiaoDich() {
        return maGiaoDich;
    }

    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public String getTenGiaoDich() {
        return tenGiaoDich;
    }

    public double getSoTien() {
        return soTien;
    }

    public Date getNgayGiaoDich() {
        return ngayGiaoDich;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public String getAnhHoaDon() {
        return anhHoaDon;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public String getLoaiDanhMuc() {
        return loaiDanhMuc;
    }

    public String getBieuTuong() {
        return bieuTuong;
    }

    public String getMauSac() {
        return mauSac;
    }

    public boolean isTienVao() {
        return "Thu nhập".equals(loaiDanhMuc);
    }

    // Setters
    public void setMaGiaoDich(int maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public void setTenGiaoDich(String tenGiaoDich) {
        this.tenGiaoDich = tenGiaoDich;
    }

    public void setSoTien(double soTien) {
        this.soTien = soTien;
    }

    public void setNgayGiaoDich(Date ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public void setAnhHoaDon(String anhHoaDon) {
        this.anhHoaDon = anhHoaDon;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public void setLoaiDanhMuc(String loaiDanhMuc) {
        this.loaiDanhMuc = loaiDanhMuc;
    }

    public void setBieuTuong(String bieuTuong) {
        this.bieuTuong = bieuTuong;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    // Backward compatibility
    @Deprecated
    public Date getNgayGiaoDichDate() {
        return ngayGiaoDich;
    }
}
