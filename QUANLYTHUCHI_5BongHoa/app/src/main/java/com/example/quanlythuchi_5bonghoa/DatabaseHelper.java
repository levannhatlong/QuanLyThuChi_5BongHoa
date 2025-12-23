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

    // Interface cho callback
    public interface DataCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    // Lấy danh mục theo loại với callback
    public void getDanhMucByLoai(String loaiDanhMuc, DataCallback<List<DanhMuc>> callback) {
        new Thread(() -> {
            List<DanhMuc> categories = new ArrayList<>();
            Connection connection = DatabaseConnector.getConnection();
            
            if (connection == null) {
                callback.onError("Không thể kết nối database");
                return;
            }
            
            try {
                String query = "SELECT MaDanhMuc, TenDanhMuc, MoTa, LoaiDanhMuc FROM DanhMuc WHERE LoaiDanhMuc = ? ORDER BY TenDanhMuc";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, loaiDanhMuc);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    DanhMuc category = new DanhMuc(
                        rs.getInt("MaDanhMuc"),
                        rs.getString("TenDanhMuc"),
                        rs.getString("MoTa") != null ? rs.getString("MoTa") : "",
                        "ic_category", // Default icon
                        "#0EA5E9", // Default color
                        "Chi tiêu".equals(rs.getString("LoaiDanhMuc"))
                    );
                    categories.add(category);
                }
                rs.close();
                stmt.close();
                callback.onSuccess(categories);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onError("Lỗi khi tải dữ liệu: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Tìm kiếm danh mục với callback
    public void searchDanhMuc(String keyword, String loaiDanhMuc, DataCallback<List<DanhMuc>> callback) {
        new Thread(() -> {
            List<DanhMuc> categories = new ArrayList<>();
            Connection connection = DatabaseConnector.getConnection();
            
            if (connection == null) {
                callback.onError("Không thể kết nối database");
                return;
            }
            
            try {
                String query = "SELECT MaDanhMuc, TenDanhMuc, MoTa, LoaiDanhMuc FROM DanhMuc WHERE TenDanhMuc LIKE ? AND LoaiDanhMuc = ? ORDER BY TenDanhMuc";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, "%" + keyword + "%");
                stmt.setString(2, loaiDanhMuc);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    DanhMuc category = new DanhMuc(
                        rs.getInt("MaDanhMuc"),
                        rs.getString("TenDanhMuc"),
                        rs.getString("MoTa") != null ? rs.getString("MoTa") : "",
                        "ic_category", // Default icon
                        "#0EA5E9", // Default color
                        "Chi tiêu".equals(rs.getString("LoaiDanhMuc"))
                    );
                    categories.add(category);
                }
                rs.close();
                stmt.close();
                callback.onSuccess(categories);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onError("Lỗi khi tìm kiếm: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Xóa danh mục với callback
    public void deleteDanhMuc(int maDanhMuc, DataCallback<Boolean> callback) {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            
            if (connection == null) {
                callback.onError("Không thể kết nối database");
                return;
            }
            
            try {
                String query = "DELETE FROM DanhMuc WHERE MaDanhMuc = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, maDanhMuc);
                int rowsAffected = stmt.executeUpdate();
                stmt.close();
                callback.onSuccess(rowsAffected > 0);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onError("Lỗi khi xóa: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Kiểm tra tên danh mục đã tồn tại với callback
    public void isCategoryNameExists(String name, boolean isExpense, int excludeId, DataCallback<Boolean> callback) {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            
            if (connection == null) {
                callback.onError("Không thể kết nối database");
                return;
            }
            
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
                boolean exists = false;
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
                rs.close();
                stmt.close();
                callback.onSuccess(exists);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onError("Lỗi khi kiểm tra: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Thêm danh mục mới với callback
    public void addDanhMuc(String tenDanhMuc, String moTa, String bieuTuong, String mauSac, String loaiDanhMuc, DataCallback<Boolean> callback) {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            
            if (connection == null) {
                callback.onError("Không thể kết nối database");
                return;
            }
            
            try {
                String query = "INSERT INTO DanhMuc (TenDanhMuc, MoTa, LoaiDanhMuc) VALUES (?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, tenDanhMuc);
                stmt.setString(2, moTa);
                stmt.setString(3, loaiDanhMuc);
                
                int rowsAffected = stmt.executeUpdate();
                stmt.close();
                callback.onSuccess(rowsAffected > 0);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onError("Lỗi khi thêm: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Cập nhật danh mục với callback
    public void updateDanhMuc(int maDanhMuc, String tenDanhMuc, String moTa, String bieuTuong, String mauSac, String loaiDanhMuc, DataCallback<Boolean> callback) {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            
            if (connection == null) {
                callback.onError("Không thể kết nối database");
                return;
            }
            
            try {
                String query = "UPDATE DanhMuc SET TenDanhMuc = ?, MoTa = ?, LoaiDanhMuc = ? WHERE MaDanhMuc = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, tenDanhMuc);
                stmt.setString(2, moTa);
                stmt.setString(3, loaiDanhMuc);
                stmt.setInt(4, maDanhMuc);
                
                int rowsAffected = stmt.executeUpdate();
                stmt.close();
                callback.onSuccess(rowsAffected > 0);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onError("Lỗi khi cập nhật: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Lấy thông tin người dùng với callback
    public void getUserInfo(int userId, DataCallback<NguoiDung> callback) {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            
            if (connection == null) {
                callback.onError("Không thể kết nối database");
                return;
            }
            
            try {
                String query = "SELECT MaNguoiDung, HoTen FROM NguoiDung WHERE MaNguoiDung = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    NguoiDung user = new NguoiDung();
                    user.setMaNguoiDung(rs.getInt("MaNguoiDung"));
                    user.setHoTen(rs.getString("HoTen"));
                    user.setEmail("user@example.com"); // Default email
                    user.setSoDienThoai("0123456789"); // Default phone
                    callback.onSuccess(user);
                } else {
                    callback.onError("Không tìm thấy người dùng");
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onError("Lỗi khi tải thông tin người dùng: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Lấy giao dịch với callback
    public void getGiaoDich(int userId, String dateFilter, DataCallback<List<GiaoDich>> callback) {
        new Thread(() -> {
            List<GiaoDich> transactions = new ArrayList<>();
            Connection connection = DatabaseConnector.getConnection();
            
            if (connection == null) {
                callback.onError("Không thể kết nối database");
                return;
            }
            
            try {
                String query = "SELECT g.MaGiaoDich, g.TenGiaoDich, g.SoTien, g.NgayGiaoDich, " +
                              "d.TenDanhMuc, d.LoaiDanhMuc " +
                              "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                              "WHERE g.MaNguoiDung = ? " + dateFilter +
                              " ORDER BY g.NgayGiaoDich DESC";
                
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    GiaoDich gd = new GiaoDich();
                    gd.setMaGiaoDich(rs.getInt("MaGiaoDich"));
                    gd.setTenGiaoDich(rs.getString("TenGiaoDich"));
                    gd.setSoTien(rs.getDouble("SoTien"));
                    gd.setNgayGiaoDich(rs.getTimestamp("NgayGiaoDich"));
                    gd.setTenDanhMuc(rs.getString("TenDanhMuc"));
                    gd.setLoaiDanhMuc(rs.getString("LoaiDanhMuc"));
                    gd.setBieuTuong("ic_category"); // Default icon
                    gd.setMauSac("#0EA5E9"); // Default color
                    transactions.add(gd);
                }
                rs.close();
                stmt.close();
                callback.onSuccess(transactions);
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onError("Lỗi khi tải giao dịch: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Phương thức đồng bộ cho compatibility (deprecated)
    @Deprecated
    public List<DanhMuc> getDanhMucByLoai(String loaiDanhMuc) {
        // Trả về danh sách rỗng để tránh lỗi, khuyến khích dùng callback
        return new ArrayList<>();
    }

    @Deprecated
    public List<DanhMuc> searchDanhMuc(String keyword, String loaiDanhMuc) {
        return new ArrayList<>();
    }

    @Deprecated
    public boolean deleteDanhMuc(int maDanhMuc) {
        return false;
    }

    @Deprecated
    public boolean isCategoryNameExists(String name, boolean isExpense, int excludeId) {
        return false;
    }

    // Test method để kiểm tra kết nối database
    public void testConnection(DataCallback<String> callback) {
        new Thread(() -> {
            Connection connection = DatabaseConnector.getConnection();
            
            if (connection == null) {
                callback.onError("Không thể kết nối database");
                return;
            }
            
            try {
                String query = "SELECT COUNT(*) as total FROM DanhMuc";
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    int count = rs.getInt("total");
                    callback.onSuccess("Kết nối thành công! Có " + count + " danh mục trong database.");
                } else {
                    callback.onSuccess("Kết nối thành công! Database trống.");
                }
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                callback.onError("Lỗi khi test: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}