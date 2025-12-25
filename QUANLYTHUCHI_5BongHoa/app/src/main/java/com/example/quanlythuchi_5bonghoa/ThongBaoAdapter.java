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

    private List<ThongBao> thongBaoList;
    private OnNotificationActionListener listener;

    public interface OnNotificationActionListener {
        void onClick(ThongBao thongBao);
        void onDelete(ThongBao thongBao);
    }

    public ThongBaoAdapter(List<ThongBao> thongBaoList, OnNotificationActionListener listener) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThongBao tb = thongBaoList.get(position);

        holder.tvTieuDe.setText(tb.getTieuDe() != null ? tb.getTieuDe() : "Thông báo");
        holder.tvNoiDung.setText(tb.getNoiDung() != null ? tb.getNoiDung() : "");
        holder.tvThoiGian.setText(tb.getNgayTao() != null ? tb.getNgayTao() : "");

        if (tb.isDaDoc()) {
            holder.viewUnread.setVisibility(View.GONE);
            holder.tvTieuDe.setTypeface(null, Typeface.NORMAL);
            holder.tvNoiDung.setTextColor(0xFF999999);
        } else {
            holder.viewUnread.setVisibility(View.VISIBLE);
            holder.tvTieuDe.setTypeface(null, Typeface.BOLD);
            holder.tvNoiDung.setTextColor(0xFF666666);
        }

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

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(tb);
        });

        // Xóa thông báo
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(tb);
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
