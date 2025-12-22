package com.example.quanlythuchi_5bonghoa;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.DanhMucViewHolder> {

    private Context context;
    private List<QuanLyDanhMucActivity.DanhMuc> danhSachDanhMuc;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(QuanLyDanhMucActivity.DanhMuc danhMuc, int position);
        void onDeleteClick(QuanLyDanhMucActivity.DanhMuc danhMuc, int position);
    }

    public DanhMucAdapter(Context context, List<QuanLyDanhMucActivity.DanhMuc> danhSachDanhMuc, OnItemClickListener listener) {
        this.context = context;
        this.danhSachDanhMuc = danhSachDanhMuc;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DanhMucViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_muc_quan_ly, parent, false);
        return new DanhMucViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhMucViewHolder holder, int position) {
        QuanLyDanhMucActivity.DanhMuc danhMuc = danhSachDanhMuc.get(position);

        holder.tvTenDanhMuc.setText(danhMuc.getTen());
        holder.tvMoTa.setText(danhMuc.getMoTa());
        holder.ivIconDanhMuc.setImageResource(danhMuc.getIconResId());

        // Set màu cho icon background
        try {
            int color = Color.parseColor(danhMuc.getMauSac());
            holder.cardIcon.setCardBackgroundColor(adjustAlpha(color, 0.15f));
            holder.ivIconDanhMuc.setColorFilter(color);
        } catch (Exception e) {
            holder.cardIcon.setCardBackgroundColor(Color.parseColor("#E8F4F8"));
            holder.ivIconDanhMuc.setColorFilter(Color.parseColor("#0EA5E9"));
        }

        // Set type badge
        if (danhMuc.isChiTieu()) {
            holder.tvTypeBadge.setText("Chi tiêu");
            holder.tvTypeBadge.setTextColor(Color.parseColor("#E53935"));
            holder.cardTypeBadge.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
        } else {
            holder.tvTypeBadge.setText("Thu nhập");
            holder.tvTypeBadge.setTextColor(Color.parseColor("#43A047"));
            holder.cardTypeBadge.setCardBackgroundColor(Color.parseColor("#E8F5E8"));
        }

        // Click listeners
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(danhMuc, holder.getAdapterPosition());
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(danhMuc, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachDanhMuc != null ? danhSachDanhMuc.size() : 0;
    }

    public void updateData(List<QuanLyDanhMucActivity.DanhMuc> newData) {
        this.danhSachDanhMuc = newData;
        notifyDataSetChanged();
    }

    // Helper method để tạo màu nhạt hơn
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static class DanhMucViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardIcon, cardTypeBadge;
        ImageView ivIconDanhMuc, btnEdit, btnDelete;
        TextView tvTenDanhMuc, tvMoTa, tvTypeBadge;

        public DanhMucViewHolder(@NonNull View itemView) {
            super(itemView);
            cardIcon = itemView.findViewById(R.id.cardIcon);
            cardTypeBadge = itemView.findViewById(R.id.cardTypeBadge);
            ivIconDanhMuc = itemView.findViewById(R.id.ivIconDanhMuc);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            tvTypeBadge = itemView.findViewById(R.id.tvTypeBadge);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}