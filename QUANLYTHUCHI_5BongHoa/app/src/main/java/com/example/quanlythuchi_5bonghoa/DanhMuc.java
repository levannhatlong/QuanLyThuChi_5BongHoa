package com.example.quanlythuchi_5bonghoa;

public class DanhMuc {
    private int maDanhMuc;
<<<<<<< HEAD
    private String tenDanhMuc;
    private String moTa;
    private String bieuTuong;
    private String mauSac;
    private boolean isChiTieu;
    private Integer maNguoiDung;
    private String loaiDanhMuc;

    public DanhMuc() {}

    public DanhMuc(int maDanhMuc, String tenDanhMuc, String moTa, String bieuTuong, String mauSac, boolean isChiTieu) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.moTa = moTa;
        this.bieuTuong = bieuTuong;
        this.mauSac = mauSac;
        this.isChiTieu = isChiTieu;
        this.loaiDanhMuc = isChiTieu ? "Chi tiêu" : "Thu nhập";
    }

    // Getters
    public int getMaDanhMuc() {
        return maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public String getMoTa() {
        return moTa;
    }

    public String getBieuTuong() {
        return bieuTuong;
    }

    public String getMauSac() {
        return mauSac;
    }

    public boolean isChiTieu() {
        return isChiTieu;
    }

    public Integer getMaNguoiDung() {
        return maNguoiDung;
    }

    public String getLoaiDanhMuc() {
        return loaiDanhMuc;
    }

    // Setters
    public void setMaDanhMuc(int maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setBieuTuong(String bieuTuong) {
        this.bieuTuong = bieuTuong;
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }

    public void setChiTieu(boolean chiTieu) {
        isChiTieu = chiTieu;
        this.loaiDanhMuc = chiTieu ? "Chi tiêu" : "Thu nhập";
    }

    public void setMaNguoiDung(Integer maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public void setLoaiDanhMuc(String loaiDanhMuc) {
        this.loaiDanhMuc = loaiDanhMuc;
        this.isChiTieu = "Chi tiêu".equals(loaiDanhMuc);
    }
}
=======
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
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
