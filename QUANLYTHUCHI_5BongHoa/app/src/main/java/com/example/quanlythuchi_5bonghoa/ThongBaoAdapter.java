package com.example.quanlythuchi_5bonghoa;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder> {

    public interface OnThongBaoClickListener {
        void onThongBaoClick(ThongBaoActivity.ThongBao thongBao);
    }

    private List<ThongBaoActivity.ThongBao> thongBaoList;
    private final OnThongBaoClickListener listener;

    public ThongBaoAdapter(List<ThongBaoActivity.ThongBao> thongBaoList, OnThongBaoClickListener listener) {
        this.thongBaoList = thongBaoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thong_bao, parent, false);
        return new ThongBaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        ThongBaoActivity.ThongBao thongBao = thongBaoList.get(position);

        holder.tvNoiDung.setText(thongBao.getNoiDung());
        holder.tvThoiGian.setText(thongBao.getThoiGian());

        if (thongBao.isDaDoc()) {
            holder.tvUnreadIndicator.setVisibility(View.GONE);
            holder.tvNoiDung.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.tvUnreadIndicator.setVisibility(View.VISIBLE);
            holder.tvNoiDung.setTypeface(null, Typeface.BOLD);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onThongBaoClick(thongBao);
        });
    }

    @Override
    public int getItemCount() {
        return thongBaoList != null ? thongBaoList.size() : 0;
    }

    public void updateData(List<ThongBaoActivity.ThongBao> newList) {
        this.thongBaoList = newList;
        notifyDataSetChanged();
    }

    static class ThongBaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoiDung, tvThoiGian, tvUnreadIndicator;

        public ThongBaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            tvThoiGian = itemView.findViewById(R.id.tvThoiGian);
            tvUnreadIndicator = itemView.findViewById(R.id.tvUnreadIndicator);
        }
    }
}
