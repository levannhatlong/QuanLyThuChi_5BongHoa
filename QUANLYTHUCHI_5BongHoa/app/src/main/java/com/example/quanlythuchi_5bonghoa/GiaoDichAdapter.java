package com.example.quanlythuchi_5bonghoa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class GiaoDichAdapter extends RecyclerView.Adapter<GiaoDichAdapter.GiaoDichViewHolder> {

    private Context context;
    private List<GiaoDich> danhSachGiaoDich;

    public GiaoDichAdapter(Context context, List<GiaoDich> danhSachGiaoDich) {
        this.context = context;
        this.danhSachGiaoDich = danhSachGiaoDich;
    }

    @NonNull
    @Override
    public GiaoDichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_giao_dich, parent, false);
        return new GiaoDichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiaoDichViewHolder holder, int position) {
        GiaoDich giaoDich = danhSachGiaoDich.get(position);

        // Hiển thị tên giao dịch
        holder.tvTenGiaoDich.setText(giaoDich.getTenGiaoDich());
        
        // Hiển thị tên danh mục
        if (holder.tvTenDanhMuc != null) {
            String tenDanhMuc = giaoDich.getTenDanhMuc();
            if (tenDanhMuc != null && !tenDanhMuc.isEmpty()) {
                holder.tvTenDanhMuc.setText(tenDanhMuc);
                holder.tvTenDanhMuc.setVisibility(View.VISIBLE);
            } else {
                holder.tvTenDanhMuc.setVisibility(View.GONE);
            }
        }
        
        // Hiển thị ngày giao dịch
        if (holder.tvNgayGiaoDich != null) {
            if (giaoDich.getNgayGiaoDichDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
                holder.tvNgayGiaoDich.setText(sdf.format(giaoDich.getNgayGiaoDichDate()));
                holder.tvNgayGiaoDich.setVisibility(View.VISIBLE);
            } else {
                holder.tvNgayGiaoDich.setVisibility(View.GONE);
            }
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String soTienFormatted = formatter.format(giaoDich.getSoTien());

        if (giaoDich.isTienVao()) {
            holder.ivLoaiGiaoDich.setImageResource(R.drawable.bieu_tuong_mui_ten_len);
            holder.tvSoTien.setText("+ " + soTienFormatted);
            holder.tvSoTien.setTextColor(ContextCompat.getColor(context, R.color.mau_xanh_luc));
        } else {
            holder.ivLoaiGiaoDich.setImageResource(R.drawable.bieu_tuong_mui_ten_xuong);
            holder.tvSoTien.setText("- " + soTienFormatted);
            holder.tvSoTien.setTextColor(ContextCompat.getColor(context, R.color.mau_do));
        }
    }

    @Override
    public int getItemCount() {
        return danhSachGiaoDich.size();
    }

    void setGiaoDichs(List<GiaoDich> newGiaoDichs) {
        danhSachGiaoDich = newGiaoDichs;
        notifyDataSetChanged();
    }

    public static class GiaoDichViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLoaiGiaoDich;
        TextView tvTenGiaoDich, tvSoTien, tvTenDanhMuc, tvNgayGiaoDich;

        public GiaoDichViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLoaiGiaoDich = itemView.findViewById(R.id.iv_loai_giao_dich);
            tvTenGiaoDich = itemView.findViewById(R.id.tv_ten_giao_dich);
            tvSoTien = itemView.findViewById(R.id.tv_so_tien);
            tvTenDanhMuc = itemView.findViewById(R.id.tv_ten_danh_muc);
            tvNgayGiaoDich = itemView.findViewById(R.id.tv_ngay_giao_dich);
        }
    }
}
