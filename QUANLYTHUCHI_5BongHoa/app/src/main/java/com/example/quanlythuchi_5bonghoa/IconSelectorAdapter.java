package com.example.quanlythuchi_5bonghoa;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class IconSelectorAdapter extends RecyclerView.Adapter<IconSelectorAdapter.IconViewHolder> {

    private int[] iconList;
    private int selectedIconResId;
    private OnIconSelectedListener listener;

    public interface OnIconSelectedListener {
        void onIconSelected(int iconResId);
    }

    public IconSelectorAdapter(int[] iconList, int selectedIconResId, OnIconSelectedListener listener) {
        this.iconList = iconList;
        this.selectedIconResId = selectedIconResId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_selector, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        int iconRes = iconList[position];
        
        holder.ivIcon.setImageResource(iconRes);

        // Update selection state
        if (iconRes == selectedIconResId) {
            holder.cardIcon.setCardBackgroundColor(Color.parseColor("#E8F4F8"));
            holder.cardIcon.setStrokeColor(Color.parseColor("#0EA5E9"));
            holder.cardIcon.setStrokeWidth(6);
            holder.ivIcon.setColorFilter(Color.parseColor("#0EA5E9"));
        } else {
            holder.cardIcon.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
            holder.cardIcon.setStrokeWidth(0);
            holder.ivIcon.setColorFilter(Color.parseColor("#757575"));
        }

        holder.cardIcon.setOnClickListener(v -> {
            selectedIconResId = iconRes;
            notifyDataSetChanged();
            if (listener != null) {
                listener.onIconSelected(iconRes);
            }
        });
    }

    @Override
    public int getItemCount() {
        return iconList.length;
    }

    public void updateSelectedIcon(int iconResId) {
        this.selectedIconResId = iconResId;
        notifyDataSetChanged();
    }

    public static class IconViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardIcon;
        ImageView ivIcon;

        public IconViewHolder(@NonNull View itemView) {
            super(itemView);
            cardIcon = itemView.findViewById(R.id.cardIconSelector);
            ivIcon = itemView.findViewById(R.id.ivIconSelector);
        }
    }
}