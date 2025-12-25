package com.example.quanlythuchi_5bonghoa;

<<<<<<< HEAD
import java.util.Date;

// POJO cho bảng GiaoDich (có thêm field phục vụ hiển thị)
=======
<<<<<<< HEAD
=======
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
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
import java.util.Date;

>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
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

<<<<<<< HEAD
=======
<<<<<<< HEAD
    // Constructors
=======
>>>>>>> HoThiMyHa
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
    public int MaGiaoDich;
    public int MaNguoiDung;
    public int MaDanhMuc;

    public String TenGiaoDich;
    public double SoTien;

    // Có thể dùng 1 trong 2, tuỳ nơi bạn lấy dữ liệu
    public String NgayGiaoDich;      // dạng chuỗi (vd: "2025-12-23")
    public Date NgayGiaoDichDate;    // dạng Date (Timestamp từ DB)

    public String GhiChu;
    public String AnhHoaDon;
<<<<<<< HEAD
=======
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
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa

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

<<<<<<< HEAD
    // Constructor dùng khi lấy từ DB + join DanhMuc
    public GiaoDich(String tenGiaoDich, double soTien, Date ngayGiaoDich,
                    String tenDanhMuc, String loaiDanhMuc, String bieuTuong) {
=======
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
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
        this.TenGiaoDich = tenGiaoDich;
        this.SoTien = soTien;
        this.NgayGiaoDichDate = ngayGiaoDich;
        this.TenDanhMuc = tenDanhMuc;
        this.LoaiDanhMuc = loaiDanhMuc;
        this.BieuTuong = bieuTuong;
        this.TienVao = isLoaiThuNhap(loaiDanhMuc);
    }

<<<<<<< HEAD
    private boolean isLoaiThuNhap(String loaiDanhMuc) {
        return loaiDanhMuc != null && loaiDanhMuc.trim().equalsIgnoreCase("Thu nhập");
    }

    // Getters (đúng chuẩn JavaBean)
=======
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> HoThiMyHa
>>>>>>> 1ee33c8ca1ac369a9ddd4b55a3b94b5f81ef69a4
=======
>>>>>>> d5871c4dd5d140e60271c9ed846f1800707f2d2f
    // Hàm khởi tạo mặc định
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
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

>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
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
<<<<<<< HEAD
        // nếu có LoaiDanhMuc thì ưu tiên theo LoaiDanhMuc (tránh sai lệch)
        if (LoaiDanhMuc != null && !LoaiDanhMuc.trim().isEmpty()) {
            return isLoaiThuNhap(LoaiDanhMuc);
        }
=======
<<<<<<< HEAD
        return "Thu nhập".equals(loaiDanhMuc);
=======
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
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
<<<<<<< HEAD

    public String getNgayGiaoDich() {
        return NgayGiaoDich;
=======
    
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
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
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
<<<<<<< HEAD
        return ngayGiaoDich;
=======
        return NgayGiaoDichDate;
>>>>>>> 47c1b5a0d0124fda7137816422bd72d5efbb41c3
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
    }
}
