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

    private final Context context;
    private List<QuanLyDanhMucActivity.DanhMuc> danhSachDanhMuc;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(QuanLyDanhMucActivity.DanhMuc danhMuc, int position);
        void onDeleteClick(QuanLyDanhMucActivity.DanhMuc danhMuc, int position);
    }

    public DanhMucAdapter(Context context,
                          List<QuanLyDanhMucActivity.DanhMuc> danhSachDanhMuc,
                          OnItemClickListener listener) {
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

        holder.tvTenDanhMuc.setText(safeText(danhMuc.getTen()));
        holder.tvMoTa.setText(safeText(danhMuc.getMoTa()));
        holder.ivIconDanhMuc.setImageResource(danhMuc.getIconResId());

        // Set màu cho icon background
        try {
            int color = Color.parseColor(danhMuc.getMauSac());
            holder.cardIcon.setCardBackgroundColor(adjustAlpha(color, 0.18f));
            holder.ivIconDanhMuc.setColorFilter(color);
        } catch (Exception e) {
            holder.cardIcon.setCardBackgroundColor(Color.parseColor("#E3F2FD"));
            holder.ivIconDanhMuc.setColorFilter(Color.parseColor("#1976D2"));
        }

        holder.btnEdit.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            if (listener != null) listener.onEditClick(danhMuc, pos);
        });

        holder.btnDelete.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            if (listener != null) listener.onDeleteClick(danhMuc, pos);
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

    private String safeText(String s) {
        return s == null ? "" : s;
    }

    // Helper method để tạo màu nhạt hơn
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static class DanhMucViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardIcon;
        ImageView ivIconDanhMuc, btnEdit, btnDelete;
        TextView tvTenDanhMuc, tvMoTa;

        public DanhMucViewHolder(@NonNull View itemView) {
            super(itemView);
            cardIcon = itemView.findViewById(R.id.cardIcon);
            ivIconDanhMuc = itemView.findViewById(R.id.ivIconDanhMuc);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
