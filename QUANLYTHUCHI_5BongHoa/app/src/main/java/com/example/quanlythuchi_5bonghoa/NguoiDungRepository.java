package com.example.quanlythuchi_5bonghoa;

import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NguoiDungRepository {

    // Model class cho NguoiDung
    public static class NguoiDung {
        public int maNguoiDung;
        public String tenDangNhap;
        public String hoTen;
        public String emailSoDienThoai; // Cột gộp Email/SĐT
        public String ngaySinh;
        public String anhDaiDien;
        public String ngayTao;
    }

    // Lấy thông tin người dùng
    public static NguoiDung getNguoiDung(int userId) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaNguoiDung, TenDangNhap, HoTen, EmailSoDienThoai, " +
                        "FORMAT(NgaySinh, 'dd/MM/yyyy') as NgaySinh, AnhDaiDien, NgayTao " +
                        "FROM NguoiDung WHERE MaNguoiDung = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    NguoiDung nd = new NguoiDung();
                    nd.maNguoiDung = rs.getInt("MaNguoiDung");
                    nd.tenDangNhap = rs.getString("TenDangNhap");
                    nd.hoTen = rs.getString("HoTen");
                    nd.emailSoDienThoai = rs.getString("EmailSoDienThoai");
                    nd.ngaySinh = rs.getString("NgaySinh");
                    nd.anhDaiDien = rs.getString("AnhDaiDien");
                    rs.close();
                    stmt.close();
                    conn.close();
                    Log.d("NguoiDungRepo", "Lấy thành công: " + nd.hoTen);
                    return nd;
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("NguoiDungRepo", "Lỗi lấy thông tin: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật thông tin người dùng
    public static boolean updateNguoiDung(int userId, String hoTen, String emailSoDienThoai, String ngaySinh) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "UPDATE NguoiDung SET HoTen = ?, EmailSoDienThoai = ?, NgaySinh = ? WHERE MaNguoiDung = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, hoTen);
                stmt.setString(2, emailSoDienThoai);
                
                // Chuyển đổi ngày từ dd/MM/yyyy sang yyyy-MM-dd
                if (ngaySinh != null && !ngaySinh.isEmpty()) {
                    String[] parts = ngaySinh.split("/");
                    if (parts.length == 3) {
                        String sqlDate = parts[2] + "-" + parts[1] + "-" + parts[0];
                        stmt.setString(3, sqlDate);
                    } else {
                        stmt.setNull(3, java.sql.Types.DATE);
                    }
                } else {
                    stmt.setNull(3, java.sql.Types.DATE);
                }
                
                stmt.setInt(4, userId);

                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                Log.d("NguoiDungRepo", "Cập nhật thành công: " + result);
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("NguoiDungRepo", "Lỗi cập nhật: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Đổi mật khẩu
    public static boolean doiMatKhau(int userId, String matKhauCu, String matKhauMoi) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                // Kiểm tra mật khẩu cũ
                String checkSql = "SELECT MaNguoiDung FROM NguoiDung WHERE MaNguoiDung = ? AND MatKhau = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, userId);
                checkStmt.setString(2, matKhauCu);
                ResultSet rs = checkStmt.executeQuery();
                
                if (!rs.next()) {
                    rs.close();
                    checkStmt.close();
                    conn.close();
                    return false; // Mật khẩu cũ không đúng
                }
                rs.close();
                checkStmt.close();

                // Cập nhật mật khẩu mới
                String updateSql = "UPDATE NguoiDung SET MatKhau = ? WHERE MaNguoiDung = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, matKhauMoi);
                updateStmt.setInt(2, userId);
                int result = updateStmt.executeUpdate();
                updateStmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("NguoiDungRepo", "Lỗi đổi mật khẩu: " + e.getMessage());
        }
        return false;
    }

    // Xóa tài khoản
    public static boolean xoaTaiKhoan(int userId) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                // Xóa các dữ liệu liên quan trước
                String[] deleteSqls = {
                    "DELETE FROM GiaoDich WHERE MaNguoiDung = ?",
                    "DELETE FROM GhiChu WHERE MaNguoiDung = ?",
                    "DELETE FROM ThongBao WHERE MaNguoiDung = ?",
                    "DELETE FROM CanhBaoChiTieu WHERE MaNguoiDung = ?",
                    "DELETE FROM LienKetNganHang WHERE MaNguoiDung = ?",
                    "DELETE FROM DanhMuc WHERE MaNguoiDung = ?",
                    "DELETE FROM NguoiDung WHERE MaNguoiDung = ?"
                };

                for (String sql : deleteSqls) {
                    try {
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, userId);
                        stmt.executeUpdate();
                        stmt.close();
                    } catch (Exception ignored) {}
                }
                conn.close();
                return true;
            }
        } catch (Exception e) {
            Log.e("NguoiDungRepo", "Lỗi xóa tài khoản: " + e.getMessage());
        }
        return false;
    }
}
