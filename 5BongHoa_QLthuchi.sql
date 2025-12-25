USE master;
GO

-- 1. Xóa cơ sở dữ liệu cũ nếu tồn tại để làm mới
IF DB_ID(N'5BongHoa_QLthuchi') IS NOT NULL
BEGIN
    ALTER DATABASE [5BongHoa_QLthuchi] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [5BongHoa_QLthuchi];
END
GO

-- 2. Tạo cơ sở dữ liệu mới
CREATE DATABASE [5BongHoa_QLthuchi];
GO

USE [5BongHoa_QLthuchi];
GO

-- 3. Tạo cấu trúc các bảng
CREATE TABLE NguoiDung (
    MaNguoiDung INT IDENTITY PRIMARY KEY,
    TenDangNhap NVARCHAR(50) NOT NULL UNIQUE,
    MatKhau NVARCHAR(255) NOT NULL,
    HoTen NVARCHAR(100),
    EmailSoDienThoai NVARCHAR(100) NOT NULL,
    NgaySinh DATE,
    AnhDaiDien NVARCHAR(MAX),
    NgayTao DATETIME DEFAULT GETDATE()
);
GO

CREATE TABLE DanhMuc (
    MaDanhMuc INT IDENTITY PRIMARY KEY,
    MaNguoiDung INT NULL, -- NULL nghĩa là danh mục mặc định của hệ thống
    TenDanhMuc NVARCHAR(100) NOT NULL,
    LoaiDanhMuc NVARCHAR(20) CHECK (LoaiDanhMuc IN (N'Chi tiêu', N'Thu nhập')),
    MoTa NVARCHAR(255),
    BieuTuong NVARCHAR(50)
);
GO

CREATE TABLE GiaoDich (
    MaGiaoDich INT IDENTITY PRIMARY KEY,
    MaNguoiDung INT NOT NULL,
    MaDanhMuc INT NOT NULL,
    TenGiaoDich NVARCHAR(255),
    SoTien DECIMAL(18,2) NOT NULL,
    NgayGiaoDich DATETIME DEFAULT GETDATE(),
    GhiChu NVARCHAR(MAX),
    AnhHoaDon NVARCHAR(MAX),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung),
    FOREIGN KEY (MaDanhMuc) REFERENCES DanhMuc(MaDanhMuc)
);
GO

CREATE TABLE LienKetNganHang (
    MaLienKet INT IDENTITY PRIMARY KEY,
    MaNguoiDung INT NOT NULL,
    TenNganHang NVARCHAR(100),
    SoTaiKhoan NVARCHAR(50),
    ChuTaiKhoan NVARCHAR(100),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung)
);
GO

CREATE TABLE CanhBaoChiTieu (
    MaCanhBao INT IDENTITY PRIMARY KEY,
    MaNguoiDung INT NOT NULL,
    ChuKy NVARCHAR(20) CHECK (ChuKy IN (N'Theo ngày', N'Theo tháng', N'Theo năm')),
    HanMuc DECIMAL(18,2) NOT NULL,
    NgayBatDau DATE,
    TrangThai BIT DEFAULT 1,
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung)
);
GO

CREATE TABLE ThongBao (
    MaThongBao INT IDENTITY PRIMARY KEY,
    MaNguoiDung INT NOT NULL,
    TieuDe NVARCHAR(255),
    NoiDung NVARCHAR(MAX),
    DaDoc BIT DEFAULT 0,
    NgayTao DATETIME DEFAULT GETDATE(),
    LoaiThongBao NVARCHAR(50),
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung)
);
GO

CREATE TABLE GhiChu (
    MaGhiChu INT IDENTITY PRIMARY KEY,
    MaNguoiDung INT NOT NULL,
    TieuDe NVARCHAR(200) NOT NULL,
    NoiDung NVARCHAR(MAX) NOT NULL,
    NgayTao DATETIME NOT NULL DEFAULT GETDATE(),
    NgayCapNhat DATETIME NULL,
    DaXoa BIT NOT NULL DEFAULT 0,
    FOREIGN KEY (MaNguoiDung) REFERENCES NguoiDung(MaNguoiDung)
);
GO

---------------------------------------------------------
-- 4. CHÈN DỮ LIỆU MẪU (Mỗi bảng ít nhất 15 dòng)
---------------------------------------------------------

-- NguoiDung (15 dòng)
INSERT INTO NguoiDung (TenDangNhap, MatKhau, HoTen, EmailSoDienThoai, NgaySinh) VALUES 
(N'5bonghoa', '123456', N'5 Bông Hoa', '5bonghoa@gmail.com', '2001-04-12'),
(N'hoangnam', 'pass123', N'Nguyễn Hoàng Nam', 'namnh@gmail.com', '1995-05-20'),
(N'minhtu', 'pass123', N'Trần Minh Tú', 'tutm@gmail.com', '1998-11-02'),
(N'lananh', 'pass123', N'Lê Lan Anh', 'lananhle@gmail.com', '1992-03-15'),
(N'quocbao', 'pass123', N'Phạm Quốc Bảo', 'baopq@gmail.com', '2000-08-25'),
(N'thuylinh', 'pass123', N'Đặng Thùy Linh', 'linhdt@gmail.com', '1997-12-10'),
(N'thanhson', 'pass123', N'Vũ Thanh Sơn', 'sonvt@gmail.com', '1990-01-30'),
(N'khanhvy', 'pass123', N'Ngô Khánh Vy', 'vyvk@gmail.com', '2002-06-18'),
(N'ducmanh', 'pass123', N'Bùi Đức Mạnh', 'manhbd@gmail.com', '1994-09-12'),
(N'hongngoc', 'pass123', N'Trương Hồng Ngọc', 'ngocth@gmail.com', '1996-04-05'),
(N'giahuynh', 'pass123', N'Đỗ Gia Huy', 'huytg@gmail.com', '1999-07-22'),
(N'thuyduong', 'pass123', N'Nguyễn Thùy Dương', 'duongnt@gmail.com', '1993-02-14'),
(N'minhkhoa', 'pass123', N'Phan Minh Khoa', 'khoapm@gmail.com', '2001-10-28'),
(N'anhthu', 'pass123', N'Lý Anh Thư', 'thula@gmail.com', '1991-11-11'),
(N'quangkhai', 'pass123', N'Trịnh Quang Khải', 'khaitq@gmail.com', '1998-05-09');

-- DanhMuc (15 dòng)
INSERT INTO DanhMuc (MaNguoiDung, TenDanhMuc, LoaiDanhMuc, BieuTuong) VALUES 
(NULL, N'Thực phẩm', N'Chi tiêu', 'food.png'),
(NULL, N'Di chuyển', N'Chi tiêu', 'car.png'),
(NULL, N'Tiền lương', N'Thu nhập', 'salary.png'),
(NULL, N'Giải trí', N'Chi tiêu', 'game.png'),
(NULL, N'Sức khỏe', N'Chi tiêu', 'health.png'),
(1, N'Chế độ ăn', N'Chi tiêu', 'diet.png'),
(1, N'Thời trang', N'Chi tiêu', 'fashion.png'),
(1, N'Dịch vụ thú y', N'Chi tiêu', 'pet.png'),
(1, N'Giáo dục', N'Chi tiêu', 'edu.png'),
(1, N'Tiền thưởng', N'Thu nhập', 'bonus.png'),
(1, N'Tiền đầu tư', N'Thu nhập', 'invest.png'),
(1, N'Kinh doanh online', N'Thu nhập', 'shop.png'),
(2, N'Làm thêm', N'Thu nhập', 'parttime.png'),
(3, N'Tiền nhà', N'Chi tiêu', 'house.png'),
(5, N'Du lịch', N'Chi tiêu', 'travel.png');

-- GiaoDich (15 dòng)
INSERT INTO GiaoDich (MaNguoiDung, MaDanhMuc, TenGiaoDich, SoTien, NgayGiaoDich, GhiChu) VALUES 
(1, 3, N'Nhận lương tháng 10', 30000000, '2025-10-10', N'Vietcombank'),
(1, 2, N'Du lịch Đà Lạt', 5000000, '2025-10-10', N'Vé máy bay'),
(1, 1, N'Ăn trưa', 200000, '2025-10-10', N'Cơm văn phòng'),
(1, 11, N'Chứng khoán', 2000000, '2025-10-11', N'Lãi cổ phiếu'),
(2, 3, N'Lương tháng 12', 15000000, '2025-12-05', N'Công ty A'),
(3, 14, N'Tiền nhà', 4500000, '2025-12-01', N'Phòng trọ'),
(4, 3, N'Lương Freelance', 8000000, '2025-12-15', N'Dự án web'),
(1, 4, N'Xem phim CGV', 250000, '2025-12-20', N'Spider-man'),
(5, 15, N'Đi Đà Nẵng', 3000000, '2025-12-22', N'Khách sạn'),
(6, 1, N'Siêu thị', 1200000, '2025-12-23', N'Winmart'),
(7, 2, N'Đổ xăng', 50000, '2025-12-23', N'Petrolimex'),
(8, 5, N'Khám răng', 500000, '2025-12-23', N'Nha khoa'),
(9, 10, N'Thưởng quý', 10000000, '2025-12-23', N'Công ty'),
(10, 1, N'Mua trà sữa', 60000, '2025-12-23', N'Phúc Long'),
(11, 4, N'Nạp game', 1000000, '2025-12-23', N'Garena');

-- LienKetNganHang (15 dòng)
INSERT INTO LienKetNganHang (MaNguoiDung, TenNganHang, SoTaiKhoan, ChuTaiKhoan) VALUES 
(1, N'Vietcombank', '1023456789', N'NGUYEN THI BONG HOA'),
(2, N'Techcombank', '190345678910', N'TRAN MINH TU'),
(3, N'BIDV', '2151000123456', N'LE LAN ANH'),
(4, N'Agribank', '3100205123456', N'PHAM QUOC BAO'),
(5, N'MB Bank', '0987654321', N'DANG THUY LINH'),
(6, N'TPBank', '00001234567', N'VU THANH SON'),
(7, N'Sacombank', '060123456789', N'NGO KHANH VY'),
(8, N'ACB', '12345678', N'BUI DUC MANH'),
(9, N'VPBank', '123456789', N'TRUONG HONG NGOC'),
(10, N'VietinBank', '10186754321', N'DO GIA HUY'),
(11, N'VIB', '6017041000123', N'NGUYEN THUY DUONG'),
(12, N'HSBC', '001234567041', N'PHAN MINH KHOA'),
(13, N'Shinhan Bank', '700123456789', N'LY ANH THU'),
(14, N'Standard Chartered', '88123456789', N'TRINH QUANG KHAI'),
(1, N'MoMo', '0901234567', N'5 BONG HOA');

-- CanhBaoChiTieu (15 dòng)
INSERT INTO CanhBaoChiTieu (MaNguoiDung, ChuKy, HanMuc) VALUES 
(1, N'Theo tháng', 13000000),
(2, N'Theo tháng', 8000000),
(3, N'Theo tháng', 15000000),
(4, N'Theo ngày', 500000),
(5, N'Theo năm', 200000000),
(6, N'Theo tháng', 10000000),
(7, N'Theo tháng', 5000000),
(8, N'Theo tháng', 12000000),
(9, N'Theo ngày', 300000),
(10, N'Theo tháng', 7000000),
(11, N'Theo tháng', 9000000),
(12, N'Theo năm', 150000000),
(13, N'Theo tháng', 20000000),
(14, N'Theo ngày', 1000000),
(1, N'Theo ngày', 1000000);

-- GhiChu (15 dòng)
INSERT INTO GhiChu (MaNguoiDung, TieuDe, NoiDung) VALUES
(1, N'Họp team', N'15h30 thứ 7'),
(1, N'Mua quà mẹ', N'Hoa và bánh kem'),
(1, N'Khám răng', N'9h sáng thứ 6'),
(1, N'Tiền điện', N'Đóng trước ngày 25'),
(2, N'Chợ búa', N'Trứng, sữa, rau'),
(3, N'Dự án app', N'Xây dựng database'),
(4, N'BHXH', N'Tra cứu mã số'),
(5, N'Lịch Gym', N'Tập chân thứ 2'),
(6, N'Mua sách', N'Đắc nhân tâm'),
(7, N'Sửa xe', N'Thay dầu xe máy'),
(8, N'Lịch bay', N'Bay lúc 10h sáng'),
(9, N'Hợp đồng', N'Ký hợp đồng thuê nhà'),
(10, N'Đám cưới', N'Đi đám cưới bạn'),
(11, N'Gửi tiết kiệm', N'Ra ngân hàng gửi 50tr'),
(12, N'Sinh nhật con', N'Mua đồ chơi');

-- ThongBao (15 dòng)
INSERT INTO ThongBao (MaNguoiDung, TieuDe, NoiDung, DaDoc, NgayTao, LoaiThongBao) VALUES
(1, N'Nhắc nhở', N'Sắp vượt hạn mức.', 0, GETDATE(), N'CanhBao'),
(1, N'Cảnh báo', N'Chi tiêu cao.', 0, GETDATE(), N'ChiTieu'),
(1, N'Giao dịch', N'Mới ghi nhận.', 0, GETDATE(), N'GiaoDich'),
(2, N'Chào mừng', N'Chào mừng bạn!', 1, GETDATE(), N'HeThong'),
(3, N'Lỗi đăng nhập', N'Phát hiện thiết bị lạ.', 0, GETDATE(), N'HeThong'),
(4, N'Hoàn thành mục tiêu', N'Tiết kiệm tốt.', 1, GETDATE(), N'BaoCao'),
(5, N'Khuyến mãi', N'Voucher 20k.', 0, GETDATE(), N'KhuyenMai'),
(6, N'Lương về', N'Tài khoản +10tr.', 1, GETDATE(), N'ThuNhap'),
(7, N'Cập nhật', N'Phiên bản mới 2.0.', 1, GETDATE(), N'HeThong'),
(8, N'Nhắc nợ', N'Bạn chưa đóng tiền điện.', 0, GETDATE(), N'NhacNho'),
(9, N'Giao dịch lỗi', N'Thất bại khi lưu.', 0, GETDATE(), N'GiaoDich'),
(10, N'Quảng cáo', N'Gói VIP giảm giá.', 1, GETDATE(), N'HeThong'),
(11, N'Xác minh', N'Xác minh email thành công.', 1, GETDATE(), N'HeThong'),
(12, N'Thẻ mới', N'Liên kết ngân hàng thành công.', 1, GETDATE(), N'GiaoDich'),
(1, N'Tổng kết tuần', N'Bạn đã tiêu 2tr tuần qua.', 0, GETDATE(), N'BaoCao');
GO

-- Kiểm tra dữ liệu sau khi nạp
SELECT 'NguoiDung' as Bang, COUNT(*) as SoDong FROM NguoiDung
UNION ALL SELECT 'DanhMuc', COUNT(*) FROM DanhMuc
UNION ALL SELECT 'GiaoDich', COUNT(*) FROM GiaoDich
UNION ALL SELECT 'LienKetNganHang', COUNT(*) FROM LienKetNganHang
UNION ALL SELECT 'CanhBaoChiTieu', COUNT(*) FROM CanhBaoChiTieu
UNION ALL SELECT 'GhiChu', COUNT(*) FROM GhiChu
UNION ALL SELECT 'ThongBao', COUNT(*) FROM ThongBao;