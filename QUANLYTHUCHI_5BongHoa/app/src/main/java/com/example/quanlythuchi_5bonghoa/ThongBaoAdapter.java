package com.example.quanlythuchi_5bonghoa;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {

<<<<<<< HEAD
    public interface OnThongBaoClickListener {
        void onThongBaoClick(ThongBaoActivity.ThongBao thongBao);
    }

    private List<ThongBaoActivity.ThongBao> thongBaoList;
    private final OnThongBaoClickListener listener;

    public ThongBaoAdapter(List<ThongBaoActivity.ThongBao> thongBaoList, OnThongBaoClickListener listener) {
=======
    private List<ThongBao> thongBaoList;
    private OnNotificationActionListener listener;

    public interface OnNotificationActionListener {
        void onClick(ThongBao thongBao);
        void onDelete(ThongBao thongBao);
    }

    public ThongBaoAdapter(List<ThongBao> thongBaoList, OnNotificationActionListener listener) {
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
        this.thongBaoList = thongBaoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thong_bao, parent, false);
        return new ViewHolder(view);
    }

    @Override
<<<<<<< HEAD
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        ThongBaoActivity.ThongBao thongBao = thongBaoList.get(position);

        holder.tvNoiDung.setText(thongBao.getNoiDung());
        holder.tvThoiGian.setText(thongBao.getThoiGian());
=======
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongBao tb = thongBaoList.get(position);
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa

        holder.tvTieuDe.setText(tb.getTieuDe() != null ? tb.getTieuDe() : "Thông báo");
        holder.tvNoiDung.setText(tb.getNoiDung() != null ? tb.getNoiDung() : "");
        holder.tvThoiGian.setText(tb.getNgayTao() != null ? tb.getNgayTao() : "");

        // Hiển thị chấm chưa đọc và style
        if (tb.isDaDoc()) {
            holder.viewUnread.setVisibility(View.GONE);
            holder.tvTieuDe.setTypeface(null, Typeface.NORMAL);
            holder.tvNoiDung.setTextColor(0xFF999999);
        } else {
            holder.viewUnread.setVisibility(View.VISIBLE);
            holder.tvTieuDe.setTypeface(null, Typeface.BOLD);
            holder.tvNoiDung.setTextColor(0xFF666666);
        }

<<<<<<< HEAD
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onThongBaoClick(thongBao);
=======
        // Icon theo loại thông báo
        if (tb.getLoaiThongBao() != null) {
            switch (tb.getLoaiThongBao()) {
                case "canh_bao":
                    holder.ivIcon.setImageResource(R.drawable.ic_alert);
                    break;
                case "nhac_nho":
                    holder.ivIcon.setImageResource(R.drawable.ic_calendar);
                    break;
                default:
                    holder.ivIcon.setImageResource(R.drawable.ic_notification);
            }
        }

        // Click để đánh dấu đã đọc
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(tb);
        });

        // Xóa thông báo
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(tb);
>>>>>>> 21f642b447fe83afa3f79fcbc14a938f6b06beaa
        });
    }

    @Override
    public int getItemCount() {
        return thongBaoList != null ? thongBaoList.size() : 0;
    }

    public void updateData(List<ThongBao> newList) {
        this.thongBaoList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon, btnDelete;
        TextView tvTieuDe, tvNoiDung, tvThoiGian;
        View viewUnread;

        ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTieuDe = itemView.findViewById(R.id.tvTieuDe);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            viewUnread = itemView.findViewById(R.id.viewUnread);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
