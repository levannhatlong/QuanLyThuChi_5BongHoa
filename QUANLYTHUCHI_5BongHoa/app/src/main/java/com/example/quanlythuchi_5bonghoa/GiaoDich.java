package com.example.quanlythuchi_5bonghoa;

<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< HEAD
// Lớp GiaoDich đã được chuyển thành một lớp Java thông thường (POJO)
public class GiaoDich {

=======
>>>>>>> 1ee33c8ca1ac369a9ddd4b55a3b94b5f81ef69a4
=======
>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
import java.util.Date;

// Lớp GiaoDich đã được chuyển thành một lớp Java thông thường (POJO)
public class GiaoDich {

>>>>>>> HoThiMyHa
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
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> 1ee33c8ca1ac369a9ddd4b55a3b94b5f81ef69a4
=======
>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
    
    // Thêm các trường mới cho hiển thị
    public String TenDanhMuc;
    public String LoaiDanhMuc;
    public String BieuTuong;
    public Date NgayGiaoDichDate;
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> HoThiMyHa
>>>>>>> 1ee33c8ca1ac369a9ddd4b55a3b94b5f81ef69a4
=======
>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f

    // Hàm khởi tạo cho dữ liệu mẫu trong TrangChuActivity
    public GiaoDich(String tenGiaoDich, double soTien, boolean tienVao) {
        this.TenGiaoDich = tenGiaoDich;
        this.SoTien = soTien;
        this.TienVao = tienVao;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> 1ee33c8ca1ac369a9ddd4b55a3b94b5f81ef69a4
=======
>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
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

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> HoThiMyHa
>>>>>>> 1ee33c8ca1ac369a9ddd4b55a3b94b5f81ef69a4
=======
>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
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
>>>>>>> HoThiMyHa
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
