package com.example.quanlythuchi_5bonghoa;

public class ThongBao {
    private int maThongBao;
    private int maNguoiDung;
    private String tieuDe;
    private String noiDung;
    private boolean daDoc;
    private String ngayTao;
    private String loaiThongBao;

    public ThongBao() {}

    // Getters and Setters
    public int getMaThongBao() { return maThongBao; }
    public void setMaThongBao(int maThongBao) { this.maThongBao = maThongBao; }

    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public boolean isDaDoc() { return daDoc; }
    public void setDaDoc(boolean daDoc) { this.daDoc = daDoc; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }

    public String getLoaiThongBao() { return loaiThongBao; }
    public void setLoaiThongBao(String loaiThongBao) { this.loaiThongBao = loaiThongBao; }
}
