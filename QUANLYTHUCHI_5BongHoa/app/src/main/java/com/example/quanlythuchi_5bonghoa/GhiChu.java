package com.example.quanlythuchi_5bonghoa;

public class GhiChu {
    private int maGhiChu;
    private int maNguoiDung;
    private String tieuDe;
    private String noiDung;
    private String ngayTao;
    private String ngayCapNhat;
    private boolean daXoa;

    public GhiChu() {}

    // Getters and Setters
    public int getMaGhiChu() { return maGhiChu; }
    public void setMaGhiChu(int maGhiChu) { this.maGhiChu = maGhiChu; }

    public int getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(int maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }

    public String getNgayCapNhat() { return ngayCapNhat; }
    public void setNgayCapNhat(String ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }

    public boolean isDaXoa() { return daXoa; }
    public void setDaXoa(boolean daXoa) { this.daXoa = daXoa; }
}
