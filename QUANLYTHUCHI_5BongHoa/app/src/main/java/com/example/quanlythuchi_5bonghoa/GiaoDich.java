package com.example.quanlythuchi_5bonghoa;

public class GiaoDich {
    private String tenGiaoDich;
    private double soTien;
    private boolean isTienVao;

    public GiaoDich(String tenGiaoDich, double soTien, boolean isTienVao) {
        this.tenGiaoDich = tenGiaoDich;
        this.soTien = soTien;
        this.isTienVao = isTienVao;
    }

    public String getTenGiaoDich() {
        return tenGiaoDich;
    }

    public double getSoTien() {
        return soTien;
    }

    public boolean isTienVao() {
        return isTienVao;
    }
}
