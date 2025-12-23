package com.example.quanlythuchi_5bonghoa;

public class LienKetNganHang {
    private int maLienKet;
    private int maNguoiDung;
    private String tenNganHang;
    private String soTaiKhoan;
    private String chuTaiKhoan;

    public LienKetNganHang() {}

    public LienKetNganHang(int maLienKet, int maNguoiDung, String tenNganHang,
                           String soTaiKhoan, String chuTaiKhoan) {
        this.maLienKet = maLienKet;
        this.maNguoiDung = maNguoiDung;
        this.tenNganHang = tenNganHang;
        this.soTaiKhoan = soTaiKhoan;
        this.chuTaiKhoan = chuTaiKhoan;
    }

    public int getMaLienKet() { return maLienKet; }
    public void setMaLienKet(int maLienKet) { this.maLienKet = maLienKet; }

    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTenNganHang() { return tenNganHang; }
    public void setTenNganHang(String tenNganHang) { this.tenNganHang = tenNganHang; }

    public String getSoTaiKhoan() { return soTaiKhoan; }
    public void setSoTaiKhoan(String soTaiKhoan) { this.soTaiKhoan = soTaiKhoan; }

    public String getChuTaiKhoan() { return chuTaiKhoan; }
    public void setChuTaiKhoan(String chuTaiKhoan) { this.chuTaiKhoan = chuTaiKhoan; }
}
