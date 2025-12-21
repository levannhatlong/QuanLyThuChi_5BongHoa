package com.example.quanlythuchi_5bonghoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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

    // Lấy danh sách giao dịch gần đây
    public List<GiaoDich> layGiaoDichGanDay(int maND) {
        List<GiaoDich> list = new ArrayList<>();
        try (Connection conn = DatabaseConnector.getConnection()) {
            if (conn != null) {
                String sql = "SELECT g.TenGiaoDich, g.SoTien, d.LoaiDanhMuc " +
                        "FROM GiaoDich g JOIN DanhMuc d ON g.MaDanhMuc = d.MaDanhMuc " +
                        "WHERE g.MaNguoiDung = ? ORDER BY g.NgayGiaoDich DESC";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, maND);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    boolean laThu = rs.getString("LoaiDanhMuc").equals("Thu nhập");
                    list.add(new GiaoDich(rs.getString("TenGiaoDich"), rs.getDouble("SoTien"), laThu));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}