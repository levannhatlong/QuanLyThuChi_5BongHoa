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

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.ViewHolder> {

    private Context context;
    private List<DanhMuc> danhMucList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(DanhMuc danhMuc, int position);
        void onDeleteClick(DanhMuc danhMuc, int position);
    }

    public DanhMucAdapter(Context context, List<DanhMuc> danhMucList, OnItemClickListener listener) {
        this.context = context;
        this.danhMucList = danhMucList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_muc_quan_ly, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMuc dm = danhMucList.get(position);

        holder.tvTenDanhMuc.setText(dm.getTenDanhMuc());
        holder.tvMoTa.setText(dm.getMoTa() != null ? dm.getMoTa() : "");

<<<<<<< HEAD
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
=======
        // Set icon - giữ nguyên màu gốc PNG
        int iconRes = getIconResource(dm.getBieuTuong());
        holder.ivIconDanhMuc.setImageResource(iconRes);
        holder.ivIconDanhMuc.setColorFilter(null); // Xóa hoàn toàn color filter
        holder.ivIconDanhMuc.setImageTintList(null); // Xóa tint

        // Background nhẹ theo loại
        int bgColor = dm.isChiTieu() ? Color.parseColor("#FFF3E0") : Color.parseColor("#E8F5E9");
        holder.cardIcon.setCardBackgroundColor(bgColor);

        // Hiển thị nút sửa và xóa cho tất cả danh mục
        holder.btnDelete.setVisibility(View.VISIBLE);
        holder.btnEdit.setVisibility(View.VISIBLE);
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(dm, holder.getAdapterPosition());
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(dm, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return danhMucList != null ? danhMucList.size() : 0;
    }

    public void updateData(List<DanhMuc> newData) {
        this.danhMucList = newData;
        notifyDataSetChanged();
    }

    private int getIconResource(String bieuTuong) {
        if (bieuTuong == null) return R.drawable.ic_category;
        switch (bieuTuong.toLowerCase()) {
            case "food.png":
                return R.drawable.ic_thuc_pham;
            case "car.png":
                return R.drawable.ic_di_chuyen;
            case "salary.png":
                return R.drawable.ic_salary;
            case "diet.png":
                return R.drawable.ic_che_do_an;
            case "fashion.png":
                return R.drawable.ic_thoi_trang;
            case "pet.png":
                return R.drawable.ic_thu_y;
            case "edu.png":
                return R.drawable.ic_giao_duc;
            case "bonus.png":
                return R.drawable.ic_tien_thuong;
            case "invest.png":
                return R.drawable.ic_dau_tu;
            case "travel.png":
                return R.drawable.ic_travel;
            case "wallet.png":
                return R.drawable.ic_wallet;
            case "drink.png":
                return R.drawable.ic_thuc_uong;
            case "other.png":
                return R.drawable.ic_tien_khac;
            default:
                return R.drawable.ic_category;
        }
    }

<<<<<<< HEAD
    public static class DanhMucViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardIcon, cardTypeBadge;
=======
    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardIcon;
>>>>>>> a0ce8161d352ffe6589a014822fb5db602910066
        ImageView ivIconDanhMuc, btnEdit, btnDelete;
        TextView tvTenDanhMuc, tvMoTa, tvTypeBadge;

        ViewHolder(View itemView) {
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
