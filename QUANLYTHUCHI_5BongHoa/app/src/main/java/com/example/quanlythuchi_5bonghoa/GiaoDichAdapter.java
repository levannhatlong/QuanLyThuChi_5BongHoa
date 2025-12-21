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
import java.util.List;
import java.util.Locale;

public class GiaoDichAdapter extends RecyclerView.Adapter<GiaoDichAdapter.GiaoDichViewHolder> {

    private Context context;
    private List<GiaoDich> giaoDichList;

    public GiaoDichAdapter(Context context, List<GiaoDich> giaoDichList) {
        this.context = context;
        this.giaoDichList = giaoDichList;
    }

    @NonNull
    @Override
    public GiaoDichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_giao_dich, parent, false);
        return new GiaoDichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiaoDichViewHolder holder, int position) {
        GiaoDich giaoDich = giaoDichList.get(position);

        holder.tvTenGiaoDich.setText(giaoDich.getTenGiaoDich());
        holder.tvDanhMuc.setText(giaoDich.getTenDanhMuc());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedAmount = formatter.format(giaoDich.getSoTien());

        if ("Thu nhập".equals(giaoDich.getLoaiDanhMuc())) {
            holder.tvSoTien.setText("+ " + formattedAmount);
            holder.tvSoTien.setTextColor(ContextCompat.getColor(context, R.color.green)); // Assuming you have a green color
        } else {
            holder.tvSoTien.setText("- " + formattedAmount);
            holder.tvSoTien.setTextColor(ContextCompat.getColor(context, R.color.red)); // Assuming you have a red color
        }

        // You can set the icon based on giaoDich.getBieuTuong() here
        // Example: int resId = context.getResources().getIdentifier(giaoDich.getBieuTuong(), "drawable", context.getPackageName());
        // holder.ivIcon.setImageResource(resId);
    }

    @Override
    public int getItemCount() {
        return giaoDichList.size();
    }

    public static class GiaoDichViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTenGiaoDich, tvDanhMuc, tvSoTien;

        public GiaoDichViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvTenGiaoDich = itemView.findViewById(R.id.tv_ten_giao_dich);
            tvDanhMuc = itemView.findViewById(R.id.tv_danh_muc);
            tvSoTien = itemView.findViewById(R.id.tv_so_tien);
        }
    }
}
