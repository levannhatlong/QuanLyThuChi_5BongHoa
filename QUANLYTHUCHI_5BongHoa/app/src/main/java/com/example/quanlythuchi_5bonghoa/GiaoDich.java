package com.example.quanlythuchi_5bonghoa;

<<<<<<< HEAD
// Lớp GiaoDich đã được chuyển thành một lớp Java thông thường (POJO)
public class GiaoDich {

=======
import java.util.Date;

// Lớp GiaoDich đã được chuyển thành một lớp Java thông thường (POJO)
public class GiaoDich {

>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
    public int MaGiaoDich;
    public int MaNguoiDung;
    public int MaDanhMuc;
    public String TenGiaoDich;
    public double SoTien;
    public String NgayGiaoDich;
    public String GhiChu;
    public String AnhHoaDon;
    public boolean TienVao;
<<<<<<< HEAD
=======
    
    // Thêm các trường mới cho hiển thị
    public String TenDanhMuc;
    public String LoaiDanhMuc;
    public String BieuTuong;
    public Date NgayGiaoDichDate;
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066

    // Hàm khởi tạo cho dữ liệu mẫu trong TrangChuActivity
    public GiaoDich(String tenGiaoDich, double soTien, boolean tienVao) {
        this.TenGiaoDich = tenGiaoDich;
        this.SoTien = soTien;
        this.TienVao = tienVao;
    }

<<<<<<< HEAD
=======
    // Hàm khởi tạo đầy đủ từ database
    public GiaoDich(String tenGiaoDich, double soTien, Date ngayGiaoDich, String tenDanhMuc, String loaiDanhMuc, String bieuTuong) {
        this.TenGiaoDich = tenGiaoDich;
        this.SoTien = soTien;
        this.NgayGiaoDichDate = ngayGiaoDich;
        this.TenDanhMuc = tenDanhMuc;
        this.LoaiDanhMuc = loaiDanhMuc;
        this.BieuTuong = bieuTuong;
        this.TienVao = "Thu nhập".equals(loaiDanhMuc);
    }

>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
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
<<<<<<< HEAD
=======
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
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
    }
}
