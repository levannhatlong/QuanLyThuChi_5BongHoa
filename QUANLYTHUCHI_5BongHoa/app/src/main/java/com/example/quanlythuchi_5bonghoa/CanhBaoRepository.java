package com.example.quanlythuchi_5bonghoa;

import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CanhBaoRepository {

    // Lấy cảnh báo chi tiêu của user
    public static CanhBao getCanhBao(int userId) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaCanhBao, MaNguoiDung, ChuKy, HanMuc, " +
                        "FORMAT(NgayBatDau, 'dd/MM/yyyy') as NgayBatDau, TrangThai " +
                        "FROM CanhBaoChiTieu WHERE MaNguoiDung = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    CanhBao cb = new CanhBao();
                    cb.setMaCanhBao(rs.getInt("MaCanhBao"));
                    cb.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                    cb.setChuKy(rs.getString("ChuKy"));
                    cb.setHanMuc(rs.getDouble("HanMuc"));
                    cb.setNgayBatDau(rs.getString("NgayBatDau"));
                    cb.setTrangThai(rs.getBoolean("TrangThai"));
                    rs.close();
                    stmt.close();
                    conn.close();
                    return cb;
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("CanhBaoRepo", "Lỗi lấy cảnh báo: " + e.getMessage());
        }
        return null;
    }

    // Thêm hoặc cập nhật cảnh báo
    public static boolean saveCanhBao(int userId, String chuKy, double hanMuc, String ngayBatDau) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String checkSql = "SELECT MaCanhBao FROM CanhBaoChiTieu WHERE MaNguoiDung = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setInt(1, userId);
                ResultSet rs = checkStmt.executeQuery();
                
                boolean exists = rs.next();
                rs.close();
                checkStmt.close();

                String sql;
                if (exists) {
                    // Cập nhật
                    sql = "UPDATE CanhBaoChiTieu SET ChuKy = ?, HanMuc = ?, NgayBatDau = ?, TrangThai = 1 WHERE MaNguoiDung = ?";
                } else {
                    // Thêm mới
                    sql = "INSERT INTO CanhBaoChiTieu (ChuKy, HanMuc, NgayBatDau, MaNguoiDung) VALUES (?, ?, ?, ?)";
                }

                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, chuKy);
                stmt.setDouble(2, hanMuc);
                if (ngayBatDau != null && !ngayBatDau.isEmpty()) {

                    String[] parts = ngayBatDau.split("/");
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
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("CanhBaoRepo", "Lỗi lưu cảnh báo: " + e.getMessage());
        }
        return false;
    }

    // Bật/tắt cảnh báo
    public static boolean toggleCanhBao(int userId, boolean trangThai) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "UPDATE CanhBaoChiTieu SET TrangThai = ? WHERE MaNguoiDung = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setBoolean(1, trangThai);
                stmt.setInt(2, userId);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("CanhBaoRepo", "Lỗi toggle: " + e.getMessage());
        }
        return false;
    }

    // Xóa cảnh báo
    public static boolean deleteCanhBao(int userId) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "DELETE FROM CanhBaoChiTieu WHERE MaNguoiDung = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("CanhBaoRepo", "Lỗi xóa: " + e.getMessage());
        }
        return false;
    }

    // Lấy tổng chi tiêu theo chu kỳ
    public static double getTongChiTieuTheoChuKy(int userId, String chuKy) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String dateCondition;
                switch (chuKy) {
                    case "Ngày":
                        dateCondition = "CAST(g.NgayGiaoDich AS DATE) = CAST(GETDATE() AS DATE)";
                        break;
                    case "Tuần":
                        dateCondition = "g.NgayGiaoDich >= DATEADD(DAY, -7, GETDATE())";
                        break;
                    case "Tháng":
                        dateCondition = "YEAR(g.NgayGiaoDich) = YEAR(GETDATE()) AND MONTH(g.NgayGiaoDich) = MONTH(GETDATE())";
                        break;
                    case "Năm":
                        dateCondition = "YEAR(g.NgayGiaoDich) = YEAR(GETDATE())";
                        break;
                    default:
                        dateCondition = "1=1";
                }

                String sql = "SELECT ISNULL(SUM(g.SoTien), 0) as TongChi " +
                        "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                        "WHERE g.MaNguoiDung = ? AND d.LoaiDanhMuc = N'Chi tiêu' AND " + dateCondition;
                
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                
                double tongChi = 0;
                if (rs.next()) {
                    tongChi = rs.getDouble("TongChi");
                }
                rs.close();
                stmt.close();
                conn.close();
                return tongChi;
            }
        } catch (Exception e) {
            Log.e("CanhBaoRepo", "Lỗi lấy tổng chi tiêu: " + e.getMessage());
        }
        return 0;
    }
}
