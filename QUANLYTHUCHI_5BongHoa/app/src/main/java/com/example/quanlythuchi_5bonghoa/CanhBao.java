package com.example.quanlythuchi_5bonghoa;

public class CanhBao {
    private int maCanhBao;
    private int maNguoiDung;
    private String chuKy; // "Theo ngày", "Theo tháng", "Theo năm"
    private double hanMuc;
    private String ngayBatDau;
    private boolean trangThai;

    public CanhBao() {}

    // Getters and Setters
    public int getMaCanhBao() { return maCanhBao; }
    public void setMaCanhBao(int maCanhBao) { this.maCanhBao = maCanhBao; }

    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getChuKy() { return chuKy; }
    public void setChuKy(String chuKy) { this.chuKy = chuKy; }

    public double getHanMuc() { return hanMuc; }
    public void setHanMuc(double hanMuc) { this.hanMuc = hanMuc; }

    public String getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(String ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }
}
