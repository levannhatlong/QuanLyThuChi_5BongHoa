package com.example.quanlythuchi_5bonghoa;

import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GhiChuRepository {

    // Lấy danh sách ghi chú chưa xóa
    public static List<GhiChu> getActiveNotes(int userId) {
        List<GhiChu> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaGhiChu, MaNguoiDung, TieuDe, NoiDung, " +
                        "FORMAT(NgayTao, 'dd/MM/yyyy, HH:mm') as NgayTao, " +
                        "FORMAT(NgayCapNhat, 'dd/MM/yyyy, HH:mm') as NgayCapNhat, DaXoa " +
                        "FROM GhiChu WHERE MaNguoiDung = ? AND DaXoa = 0 " +
                        "ORDER BY COALESCE(NgayCapNhat, NgayTao) DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    GhiChu gc = new GhiChu();
                    gc.setMaGhiChu(rs.getInt("MaGhiChu"));
                    gc.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                    gc.setTieuDe(rs.getString("TieuDe"));
                    gc.setNoiDung(rs.getString("NoiDung"));
                    gc.setNgayTao(rs.getString("NgayTao"));
                    gc.setNgayCapNhat(rs.getString("NgayCapNhat"));
                    gc.setDaXoa(rs.getBoolean("DaXoa"));
                    list.add(gc);
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("GhiChuRepo", "Lỗi lấy ghi chú: " + e.getMessage());
        }
        return list;
    }

    // Lấy danh sách ghi chú đã xóa (lịch sử)
    public static List<GhiChu> getDeletedNotes(int userId) {
        List<GhiChu> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaGhiChu, MaNguoiDung, TieuDe, NoiDung, " +
                        "FORMAT(NgayTao, 'dd/MM/yyyy, HH:mm') as NgayTao, " +
                        "FORMAT(NgayCapNhat, 'dd/MM/yyyy, HH:mm') as NgayCapNhat, DaXoa " +
                        "FROM GhiChu WHERE MaNguoiDung = ? AND DaXoa = 1 " +
                        "ORDER BY NgayCapNhat DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    GhiChu gc = new GhiChu();
                    gc.setMaGhiChu(rs.getInt("MaGhiChu"));
                    gc.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                    gc.setTieuDe(rs.getString("TieuDe"));
                    gc.setNoiDung(rs.getString("NoiDung"));
                    gc.setNgayTao(rs.getString("NgayTao"));
                    gc.setNgayCapNhat(rs.getString("NgayCapNhat"));
                    gc.setDaXoa(rs.getBoolean("DaXoa"));
                    list.add(gc);
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("GhiChuRepo", "Lỗi lấy lịch sử: " + e.getMessage());
        }
        return list;
    }

    // Thêm ghi chú mới
    public static boolean addNote(int userId, String tieuDe, String noiDung) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "INSERT INTO GhiChu (MaNguoiDung, TieuDe, NoiDung) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, tieuDe);
                stmt.setString(3, noiDung);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("GhiChuRepo", "Lỗi thêm ghi chú: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật ghi chú
    public static boolean updateNote(int maGhiChu, String tieuDe, String noiDung) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "UPDATE GhiChu SET TieuDe = ?, NoiDung = ?, NgayCapNhat = GETDATE() WHERE MaGhiChu = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, tieuDe);
                stmt.setString(2, noiDung);
                stmt.setInt(3, maGhiChu);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("GhiChuRepo", "Lỗi cập nhật: " + e.getMessage());
        }
        return false;
    }

    // Xóa mềm (chuyển vào lịch sử)
    public static boolean softDeleteNote(int maGhiChu) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "UPDATE GhiChu SET DaXoa = 1, NgayCapNhat = GETDATE() WHERE MaGhiChu = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, maGhiChu);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("GhiChuRepo", "Lỗi xóa: " + e.getMessage());
        }
        return false;
    }

    // Khôi phục ghi chú từ lịch sử
    public static boolean restoreNote(int maGhiChu) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "UPDATE GhiChu SET DaXoa = 0, NgayCapNhat = GETDATE() WHERE MaGhiChu = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, maGhiChu);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("GhiChuRepo", "Lỗi khôi phục: " + e.getMessage());
        }
        return false;
    }

    // Xóa vĩnh viễn
    public static boolean permanentDeleteNote(int maGhiChu) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "DELETE FROM GhiChu WHERE MaGhiChu = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, maGhiChu);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("GhiChuRepo", "Lỗi xóa vĩnh viễn: " + e.getMessage());
        }
        return false;
    }
}
