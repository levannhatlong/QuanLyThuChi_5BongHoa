package com.example.quanlythuchi_5bonghoa;

import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThongBaoRepository {

    // Lấy tất cả thông báo
    public static List<ThongBao> getAllNotifications(int userId) {
        List<ThongBao> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaThongBao, MaNguoiDung, TieuDe, NoiDung, DaDoc, " +
                        "CASE " +
                        "  WHEN DATEDIFF(MINUTE, NgayTao, GETDATE()) < 60 THEN CAST(DATEDIFF(MINUTE, NgayTao, GETDATE()) AS VARCHAR) + N' phút trước' " +
                        "  WHEN DATEDIFF(HOUR, NgayTao, GETDATE()) < 24 THEN CAST(DATEDIFF(HOUR, NgayTao, GETDATE()) AS VARCHAR) + N' giờ trước' " +
                        "  ELSE CAST(DATEDIFF(DAY, NgayTao, GETDATE()) AS VARCHAR) + N' ngày trước' " +
                        "END as ThoiGian, LoaiThongBao " +
                        "FROM ThongBao WHERE MaNguoiDung = ? ORDER BY NgayTao DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ThongBao tb = new ThongBao();
                    tb.setMaThongBao(rs.getInt("MaThongBao"));
                    tb.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                    tb.setTieuDe(rs.getString("TieuDe"));
                    tb.setNoiDung(rs.getString("NoiDung"));
                    tb.setDaDoc(rs.getBoolean("DaDoc"));
                    tb.setNgayTao(rs.getString("ThoiGian"));
                    tb.setLoaiThongBao(rs.getString("LoaiThongBao"));
                    list.add(tb);
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("ThongBaoRepo", "Lỗi lấy thông báo: " + e.getMessage());
        }
        return list;
    }

    // Lấy thông báo đã đọc
    public static List<ThongBao> getReadNotifications(int userId) {
        List<ThongBao> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaThongBao, MaNguoiDung, TieuDe, NoiDung, DaDoc, " +
                        "CASE " +
                        "  WHEN DATEDIFF(MINUTE, NgayTao, GETDATE()) < 60 THEN CAST(DATEDIFF(MINUTE, NgayTao, GETDATE()) AS VARCHAR) + N' phút trước' " +
                        "  WHEN DATEDIFF(HOUR, NgayTao, GETDATE()) < 24 THEN CAST(DATEDIFF(HOUR, NgayTao, GETDATE()) AS VARCHAR) + N' giờ trước' " +
                        "  ELSE CAST(DATEDIFF(DAY, NgayTao, GETDATE()) AS VARCHAR) + N' ngày trước' " +
                        "END as ThoiGian, LoaiThongBao " +
                        "FROM ThongBao WHERE MaNguoiDung = ? AND DaDoc = 1 ORDER BY NgayTao DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ThongBao tb = new ThongBao();
                    tb.setMaThongBao(rs.getInt("MaThongBao"));
                    tb.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                    tb.setTieuDe(rs.getString("TieuDe"));
                    tb.setNoiDung(rs.getString("NoiDung"));
                    tb.setDaDoc(rs.getBoolean("DaDoc"));
                    tb.setNgayTao(rs.getString("ThoiGian"));
                    tb.setLoaiThongBao(rs.getString("LoaiThongBao"));
                    list.add(tb);
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("ThongBaoRepo", "Lỗi: " + e.getMessage());
        }
        return list;
    }

    // Lấy thông báo chưa đọc
    public static List<ThongBao> getUnreadNotifications(int userId) {
        List<ThongBao> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaThongBao, MaNguoiDung, TieuDe, NoiDung, DaDoc, " +
                        "CASE " +
                        "  WHEN DATEDIFF(MINUTE, NgayTao, GETDATE()) < 60 THEN CAST(DATEDIFF(MINUTE, NgayTao, GETDATE()) AS VARCHAR) + N' phút trước' " +
                        "  WHEN DATEDIFF(HOUR, NgayTao, GETDATE()) < 24 THEN CAST(DATEDIFF(HOUR, NgayTao, GETDATE()) AS VARCHAR) + N' giờ trước' " +
                        "  ELSE CAST(DATEDIFF(DAY, NgayTao, GETDATE()) AS VARCHAR) + N' ngày trước' " +
                        "END as ThoiGian, LoaiThongBao " +
                        "FROM ThongBao WHERE MaNguoiDung = ? AND DaDoc = 0 ORDER BY NgayTao DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ThongBao tb = new ThongBao();
                    tb.setMaThongBao(rs.getInt("MaThongBao"));
                    tb.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                    tb.setTieuDe(rs.getString("TieuDe"));
                    tb.setNoiDung(rs.getString("NoiDung"));
                    tb.setDaDoc(rs.getBoolean("DaDoc"));
                    tb.setNgayTao(rs.getString("ThoiGian"));
                    tb.setLoaiThongBao(rs.getString("LoaiThongBao"));
                    list.add(tb);
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("ThongBaoRepo", "Lỗi: " + e.getMessage());
        }
        return list;
    }

    // Đánh dấu đã đọc
    public static boolean markAsRead(int maThongBao) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "UPDATE ThongBao SET DaDoc = 1 WHERE MaThongBao = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, maThongBao);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("ThongBaoRepo", "Lỗi đánh dấu đã đọc: " + e.getMessage());
        }
        return false;
    }

    // Đánh dấu tất cả đã đọc
    public static boolean markAllAsRead(int userId) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "UPDATE ThongBao SET DaDoc = 1 WHERE MaNguoiDung = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("ThongBaoRepo", "Lỗi: " + e.getMessage());
        }
        return false;
    }

    // Xóa thông báo
    public static boolean deleteNotification(int maThongBao) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "DELETE FROM ThongBao WHERE MaThongBao = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, maThongBao);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("ThongBaoRepo", "Lỗi xóa: " + e.getMessage());
        }
        return false;
    }

    // Đếm số thông báo chưa đọc
    public static int countUnread(int userId) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT COUNT(*) FROM ThongBao WHERE MaNguoiDung = ? AND DaDoc = 0";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    rs.close();
                    stmt.close();
                    conn.close();
                    return count;
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("ThongBaoRepo", "Lỗi đếm: " + e.getMessage());
        }
        return 0;
    }

    // Tạo thông báo mới
    public static boolean createNotification(int userId, String tieuDe, String noiDung, String loaiThongBao) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "INSERT INTO ThongBao (MaNguoiDung, TieuDe, NoiDung, DaDoc, NgayTao, LoaiThongBao) " +
                        "VALUES (?, ?, ?, 0, GETDATE(), ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, tieuDe);
                stmt.setString(3, noiDung);
                stmt.setString(4, loaiThongBao);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                Log.d("ThongBaoRepo", "Tạo thông báo: " + (result > 0 ? "thành công" : "thất bại"));
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("ThongBaoRepo", "Lỗi tạo thông báo: " + e.getMessage());
        }
        return false;
    }
}
