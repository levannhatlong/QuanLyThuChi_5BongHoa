package com.example.quanlythuchi_5bonghoa;

import android.content.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    
    private Context context;
    
    public DatabaseHelper(Context context) {
        this.context = context;
    }

    // Lấy danh mục theo loại
    public List<DanhMuc> getDanhMucByLoai(String loaiDanhMuc) {
        List<DanhMuc> categories = new ArrayList<>();
        
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) return;
            
            try {
                String query = "SELECT MaDanhMuc, TenDanhMuc, MoTa, Icon, MauSac, LoaiDanhMuc FROM DanhMuc WHERE LoaiDanhMuc = ? ORDER BY TenDanhMuc";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, loaiDanhMuc);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    DanhMuc category = new DanhMuc(
                        rs.getInt("MaDanhMuc"),
                        rs.getString("TenDanhMuc"),
                        rs.getString("MoTa"),
                        rs.getString("Icon"),
                        rs.getString("MauSac"),
                        "Chi tiêu".equals(rs.getString("LoaiDanhMuc"))
                    );
                    categories.add(category);
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        return categories;
    }

    // Tìm kiếm danh mục
    public List<DanhMuc> searchDanhMuc(String keyword, String loaiDanhMuc) {
        List<DanhMuc> categories = new ArrayList<>();
        
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) return;
            
            try {
                String query = "SELECT MaDanhMuc, TenDanhMuc, MoTa, Icon, MauSac, LoaiDanhMuc FROM DanhMuc WHERE TenDanhMuc LIKE ? AND LoaiDanhMuc = ? ORDER BY TenDanhMuc";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, "%" + keyword + "%");
                stmt.setString(2, loaiDanhMuc);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    DanhMuc category = new DanhMuc(
                        rs.getInt("MaDanhMuc"),
                        rs.getString("TenDanhMuc"),
                        rs.getString("MoTa"),
                        rs.getString("Icon"),
                        rs.getString("MauSac"),
                        "Chi tiêu".equals(rs.getString("LoaiDanhMuc"))
                    );
                    categories.add(category);
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        return categories;
    }

    // Xóa danh mục
    public boolean deleteDanhMuc(int maDanhMuc) {
        final boolean[] success = {false};
        
        Thread thread = new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) return;
            
            try {
                String query = "DELETE FROM DanhMuc WHERE MaDanhMuc = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, maDanhMuc);
                int rowsAffected = stmt.executeUpdate();
                success[0] = rowsAffected > 0;
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        thread.start();
        try {
            thread.join(); // Wait for thread to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return success[0];
    }

    // Kiểm tra tên danh mục đã tồn tại chưa
    public boolean isCategoryNameExists(String name, boolean isExpense, int excludeId) {
        final boolean[] exists = {false};
        
        Thread thread = new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) return;
            
            try {
                String loaiDanhMuc = isExpense ? "Chi tiêu" : "Thu nhập";
                String query = "SELECT COUNT(*) FROM DanhMuc WHERE TenDanhMuc = ? AND LoaiDanhMuc = ?";
                
                if (excludeId > 0) {
                    query += " AND MaDanhMuc != ?";
                }
                
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, loaiDanhMuc);
                
                if (excludeId > 0) {
                    stmt.setInt(3, excludeId);
                }
                
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    exists[0] = rs.getInt(1) > 0;
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        thread.start();
        try {
            thread.join(); // Wait for thread to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return exists[0];
    }

    // Thêm danh mục mới
    public boolean addDanhMuc(String tenDanhMuc, String moTa, String bieuTuong, String mauSac, String loaiDanhMuc) {
        final boolean[] success = {false};
        
        Thread thread = new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) return;
            
            try {
                String query = "INSERT INTO DanhMuc (TenDanhMuc, MoTa, Icon, MauSac, LoaiDanhMuc) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, tenDanhMuc);
                stmt.setString(2, moTa);
                stmt.setString(3, bieuTuong);
                stmt.setString(4, mauSac);
                stmt.setString(5, loaiDanhMuc);
                
                int rowsAffected = stmt.executeUpdate();
                success[0] = rowsAffected > 0;
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        thread.start();
        try {
            thread.join(); // Wait for thread to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return success[0];
    }

    // Cập nhật danh mục
    public boolean updateDanhMuc(int maDanhMuc, String tenDanhMuc, String moTa, String bieuTuong, String mauSac, String loaiDanhMuc) {
        final boolean[] success = {false};
        
        Thread thread = new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            if (connection == null) return;
            
            try {
                String query = "UPDATE DanhMuc SET TenDanhMuc = ?, MoTa = ?, Icon = ?, MauSac = ?, LoaiDanhMuc = ? WHERE MaDanhMuc = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, tenDanhMuc);
                stmt.setString(2, moTa);
                stmt.setString(3, bieuTuong);
                stmt.setString(4, mauSac);
                stmt.setString(5, loaiDanhMuc);
                stmt.setInt(6, maDanhMuc);
                
                int rowsAffected = stmt.executeUpdate();
                success[0] = rowsAffected > 0;
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        
        thread.start();
        try {
            thread.join(); // Wait for thread to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return success[0];
    }
}