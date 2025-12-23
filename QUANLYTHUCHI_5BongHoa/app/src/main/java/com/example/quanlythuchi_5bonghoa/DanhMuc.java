package com.example.quanlythuchi_5bonghoa;

public class DanhMuc {
    private int maDanhMuc;
    private Integer maNguoiDung; // null = danh mục mặc định
    private String tenDanhMuc;
    private String loaiDanhMuc; // "Chi tiêu" hoặc "Thu nhập"
    private String moTa;
    private String bieuTuong;

    public DanhMuc() {}

    public DanhMuc(int maDanhMuc, Integer maNguoiDung, String tenDanhMuc, 
                   String loaiDanhMuc, String moTa, String bieuTuong) {
        this.maDanhMuc = maDanhMuc;
        this.maNguoiDung = maNguoiDung;
        this.tenDanhMuc = tenDanhMuc;
        this.loaiDanhMuc = loaiDanhMuc;
        this.moTa = moTa;
        this.bieuTuong = bieuTuong;
    }

    // Getters and Setters
    public int getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(int maDanhMuc) { this.maDanhMuc = maDanhMuc; }

    public Integer getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(Integer maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }

    public String getLoaiDanhMuc() { return loaiDanhMuc; }
    public void setLoaiDanhMuc(String loaiDanhMuc) { this.loaiDanhMuc = loaiDanhMuc; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getBieuTuong() { return bieuTuong; }
    public void setBieuTuong(String bieuTuong) { this.bieuTuong = bieuTuong; }

    public boolean isChiTieu() {
        return "Chi tiêu".equals(loaiDanhMuc);
    }
}
