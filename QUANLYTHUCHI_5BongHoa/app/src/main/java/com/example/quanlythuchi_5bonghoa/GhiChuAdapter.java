package com.example.quanlythuchi_5bonghoa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GhiChuAdapter extends RecyclerView.Adapter<GhiChuAdapter.ViewHolder> {

    private List<GhiChu> ghiChuList;
    private boolean isHistoryMode;
    private OnNoteActionListener listener;

    public interface OnNoteActionListener {
        void onEdit(GhiChu ghiChu);
        void onDelete(GhiChu ghiChu);
        void onRestore(GhiChu ghiChu);
        void onPermanentDelete(GhiChu ghiChu);
        void onClick(GhiChu ghiChu);
    }

    public GhiChuAdapter(List<GhiChu> ghiChuList, boolean isHistoryMode, OnNoteActionListener listener) {
        this.ghiChuList = ghiChuList;
        this.isHistoryMode = isHistoryMode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ghi_chu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GhiChu ghiChu = ghiChuList.get(position);

        holder.tvTitle.setText(ghiChu.getTieuDe() != null && !ghiChu.getTieuDe().isEmpty() 
                ? ghiChu.getTieuDe() : "Không có tiêu đề");
        holder.tvContent.setText(ghiChu.getNoiDung() != null ? ghiChu.getNoiDung() : "");
        
        // Hiển thị ngày cập nhật nếu có, không thì ngày tạo
        String displayDate = ghiChu.getNgayCapNhat() != null ? ghiChu.getNgayCapNhat() : ghiChu.getNgayTao();
        holder.tvDate.setText(displayDate != null ? displayDate : "");

        // Hiển thị nút theo chế độ
        if (isHistoryMode) {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnRestore.setVisibility(View.VISIBLE);
            holder.btnPermanentDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnRestore.setVisibility(View.GONE);
            holder.btnPermanentDelete.setVisibility(View.GONE);
        }

        // Sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(ghiChu);
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(ghiChu);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(ghiChu);
        });

        holder.btnRestore.setOnClickListener(v -> {
            if (listener != null) listener.onRestore(ghiChu);
        });

        holder.btnPermanentDelete.setOnClickListener(v -> {
            if (listener != null) listener.onPermanentDelete(ghiChu);
        });
    }

    @Override
    public int getItemCount() {
        return ghiChuList != null ? ghiChuList.size() : 0;
    }

    public void updateData(List<GhiChu> newList, boolean historyMode) {
        this.ghiChuList = newList;
        this.isHistoryMode = historyMode;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvDate;
        ImageView btnEdit, btnDelete, btnRestore, btnPermanentDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnRestore = itemView.findViewById(R.id.btnRestore);
            btnPermanentDelete = itemView.findViewById(R.id.btnPermanentDelete);
        }
    }
}
