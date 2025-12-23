package com.example.quanlythuchi_5bonghoa;

import java.util.Date;

public class NguoiDung {
    private int maNguoiDung;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private Date ngaySinh;
    private String matKhau;

    public NguoiDung() {}

    public NguoiDung(int maNguoiDung, String hoTen, String email, String soDienThoai, Date ngaySinh) {
        this.maNguoiDung = maNguoiDung;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.ngaySinh = ngaySinh;
    }

    // Getters
    public int getMaNguoiDung() {
        return maNguoiDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getEmail() {
        return email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public String getMatKhau() {
        return matKhau;
    }

    // Setters
    public void setMaNguoiDung(int maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}