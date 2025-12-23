package com.example.quanlythuchi_5bonghoa;

import java.util.Date;

// POJO cho bảng GiaoDich (có thêm field phục vụ hiển thị)
public class GiaoDich {

    public int MaGiaoDich;
    public int MaNguoiDung;
    public int MaDanhMuc;

    public String TenGiaoDich;
    public double SoTien;

    // Có thể dùng 1 trong 2, tuỳ nơi bạn lấy dữ liệu
    public String NgayGiaoDich;      // dạng chuỗi
    public Date NgayGiaoDichDate;    // dạng Date

    public String GhiChu;
    public String AnhHoaDon;

    // true = Thu nhập, false = Chi tiêu
    public boolean TienVao;

    // Field bổ sung khi join DanhMuc để hiển thị
    public String TenDanhMuc;
    public String LoaiDanhMuc; // "Thu nhập" / "Chi tiêu"
    public String BieuTuong;

    // Constructor mặc định
    public GiaoDich() {}

    // Constructor dùng cho dữ liệu đơn giản (VD: Thống kê/Trang chủ)
    public GiaoDich(String tenGiaoDich, double soTien, boolean tienVao) {
        this.TenGiaoDich = tenGiaoDich;
        this.SoTien = soTien;
        this.TienVao = tienVao;
    }

    // Constructor dùng khi lấy từ DB + join DanhMuc
    public GiaoDich(String tenGiaoDich, double soTien, Date ngayGiaoDich,
                    String tenDanhMuc, String loaiDanhMuc, String bieuTuong) {
        this.TenGiaoDich = tenGiaoDich;
        this.SoTien = soTien;
        this.NgayGiaoDichDate = ngayGiaoDich;
        this.TenDanhMuc = tenDanhMuc;
        this.LoaiDanhMuc = loaiDanhMuc;
        this.BieuTuong = bieuTuong;
        this.TienVao = loaiDanhMuc != null && loaiDanhMuc.trim().equalsIgnoreCase("Thu nhập");
    }

    // Getters (đúng chuẩn JavaBean)
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
