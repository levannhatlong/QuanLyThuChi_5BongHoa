package com.example.quanlythuchi_5bonghoa;

import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DanhMucRepository {

    // Lấy danh mục theo loại
    public static List<DanhMuc> getDanhMucByLoai(int userId, String loai) {
        List<DanhMuc> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaDanhMuc, MaNguoiDung, TenDanhMuc, LoaiDanhMuc, MoTa, BieuTuong " +
                        "FROM DanhMuc WHERE (MaNguoiDung IS NULL OR MaNguoiDung = ?) AND LoaiDanhMuc = ? " +
                        "ORDER BY MaNguoiDung DESC, TenDanhMuc";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, loai);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    DanhMuc dm = new DanhMuc();
                    dm.setMaDanhMuc(rs.getInt("MaDanhMuc"));
                    dm.setMaNguoiDung(rs.getObject("MaNguoiDung") != null ? rs.getInt("MaNguoiDung") : null);
                    dm.setTenDanhMuc(rs.getString("TenDanhMuc"));
                    dm.setLoaiDanhMuc(rs.getString("LoaiDanhMuc"));
                    dm.setMoTa(rs.getString("MoTa"));
                    dm.setBieuTuong(rs.getString("BieuTuong"));
                    list.add(dm);
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("DanhMucRepo", "Lỗi lấy danh mục: " + e.getMessage());
        }
        return list;
    }

    // Lấy tất cả danh mục của user
    public static List<DanhMuc> getAllDanhMuc(int userId) {
        List<DanhMuc> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaDanhMuc, MaNguoiDung, TenDanhMuc, LoaiDanhMuc, MoTa, BieuTuong " +
                        "FROM DanhMuc WHERE MaNguoiDung IS NULL OR MaNguoiDung = ? " +
                        "ORDER BY LoaiDanhMuc, TenDanhMuc";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    DanhMuc dm = new DanhMuc();
                    dm.setMaDanhMuc(rs.getInt("MaDanhMuc"));
                    dm.setMaNguoiDung(rs.getObject("MaNguoiDung") != null ? rs.getInt("MaNguoiDung") : null);
                    dm.setTenDanhMuc(rs.getString("TenDanhMuc"));
                    dm.setLoaiDanhMuc(rs.getString("LoaiDanhMuc"));
                    dm.setMoTa(rs.getString("MoTa"));
                    dm.setBieuTuong(rs.getString("BieuTuong"));
                    list.add(dm);
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("DanhMucRepo", "Lỗi: " + e.getMessage());
        }
        return list;
    }

    // Thêm danh mục mới
    public static boolean addDanhMuc(int userId, String tenDanhMuc, String loaiDanhMuc, String moTa, String bieuTuong) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "INSERT INTO DanhMuc (MaNguoiDung, TenDanhMuc, LoaiDanhMuc, MoTa, BieuTuong) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, tenDanhMuc);
                stmt.setString(3, loaiDanhMuc);
                stmt.setString(4, moTa);
                stmt.setString(5, bieuTuong);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("DanhMucRepo", "Lỗi thêm: " + e.getMessage());
        }
        return false;
    }

    // Cập nhật danh mục
    public static boolean updateDanhMuc(int maDanhMuc, String tenDanhMuc, String loaiDanhMuc, String moTa, String bieuTuong) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "UPDATE DanhMuc SET TenDanhMuc = ?, LoaiDanhMuc = ?, MoTa = ?, BieuTuong = ? WHERE MaDanhMuc = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, tenDanhMuc);
                stmt.setString(2, loaiDanhMuc);
                stmt.setString(3, moTa);
                stmt.setString(4, bieuTuong);
                stmt.setInt(5, maDanhMuc);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("DanhMucRepo", "Lỗi cập nhật: " + e.getMessage());
        }
        return false;
    }

    // Xóa danh mục
    public static boolean deleteDanhMuc(int maDanhMuc) {
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "DELETE FROM DanhMuc WHERE MaDanhMuc = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, maDanhMuc);
                int result = stmt.executeUpdate();
                stmt.close();
                conn.close();
                return result > 0;
            }
        } catch (Exception e) {
            Log.e("DanhMucRepo", "Lỗi xóa: " + e.getMessage());
        }
        return false;
    }

    // Tìm kiếm danh mục
    public static List<DanhMuc> searchDanhMuc(int userId, String loai, String keyword) {
        List<DanhMuc> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnector.getConnection();
            if (conn != null) {
                String sql = "SELECT MaDanhMuc, MaNguoiDung, TenDanhMuc, LoaiDanhMuc, MoTa, BieuTuong " +
                        "FROM DanhMuc WHERE (MaNguoiDung IS NULL OR MaNguoiDung = ?) " +
                        "AND LoaiDanhMuc = ? AND TenDanhMuc LIKE ? " +
                        "ORDER BY TenDanhMuc";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, loai);
                stmt.setString(3, "%" + keyword + "%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    DanhMuc dm = new DanhMuc();
                    dm.setMaDanhMuc(rs.getInt("MaDanhMuc"));
                    dm.setMaNguoiDung(rs.getObject("MaNguoiDung") != null ? rs.getInt("MaNguoiDung") : null);
                    dm.setTenDanhMuc(rs.getString("TenDanhMuc"));
                    dm.setLoaiDanhMuc(rs.getString("LoaiDanhMuc"));
                    dm.setMoTa(rs.getString("MoTa"));
                    dm.setBieuTuong(rs.getString("BieuTuong"));
                    list.add(dm);
                }
                rs.close();
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            Log.e("DanhMucRepo", "Lỗi tìm kiếm: " + e.getMessage());
        }
        return list;
    }
}
