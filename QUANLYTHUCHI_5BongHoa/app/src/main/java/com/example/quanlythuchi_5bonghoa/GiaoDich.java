package com.example.quanlythuchi_5bonghoa;

// Lớp GiaoDich đã được chuyển thành một lớp Java thông thường (POJO)
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

    // Hàm khởi tạo cho dữ liệu mẫu trong TrangChuActivity
    public GiaoDich(String tenGiaoDich, double soTien, boolean tienVao) {
        this.TenGiaoDich = tenGiaoDich;
        this.SoTien = soTien;
        this.TienVao = tienVao;
    }

    // Hàm khởi tạo mặc định
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
}
