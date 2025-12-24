package com.example.quanlythuchi_5bonghoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiaoDichRepository {

    // Lấy tổng thu và tổng chi của người dùng
    public double[] layTongThuChi(int maND) {
        double thu = 0, chi = 0;
        try (Connection conn = DatabaseConnector.getConnection()) {
            if (conn != null) {
                String sql = "SELECT d.LoaiDanhMuc, SUM(g.SoTien) as Tong " +
                        "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                        "WHERE g.MaNguoiDung = ? GROUP BY d.LoaiDanhMuc";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, maND);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString("LoaiDanhMuc").equals("Thu nhập")) thu = rs.getDouble("Tong");
                    else chi = rs.getDouble("Tong");
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return new double[]{thu, chi};
    }

    // Lấy danh sách giao dịch gần đây với đầy đủ thông tin
    public List<GiaoDich> layGiaoDichGanDay(int maND) {
        List<GiaoDich> list = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection()) {
            if (conn != null) {
                String sql = "SELECT TOP 5 g.TenGiaoDich, g.SoTien, g.NgayGiaoDich, d.TenDanhMuc, d.LoaiDanhMuc, d.BieuTuong " +
                        "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                        "WHERE g.MaNguoiDung = ? ORDER BY g.NgayGiaoDich DESC";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, maND);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String tenGiaoDich = rs.getString("TenGiaoDich");
                    double soTien = rs.getDouble("SoTien");
                    Date ngayGiaoDich = rs.getTimestamp("NgayGiaoDich");
                    String tenDanhMuc = rs.getString("TenDanhMuc");
                    String loaiDanhMuc = rs.getString("LoaiDanhMuc");
                    String bieuTuong = rs.getString("BieuTuong");
                    list.add(new GiaoDich(tenGiaoDich, soTien, ngayGiaoDich, tenDanhMuc, loaiDanhMuc, bieuTuong));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}