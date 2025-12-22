package com.example.quanlythuchi_5bonghoa;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class ColorSelectorAdapter extends RecyclerView.Adapter<ColorSelectorAdapter.ColorViewHolder> {

    private String[] colorList;
    private String selectedColor;
    private OnColorSelectedListener listener;

    public interface OnColorSelectedListener {
        void onColorSelected(String color);
    }

    public ColorSelectorAdapter(String[] colorList, String selectedColor, OnColorSelectedListener listener) {
        this.colorList = colorList;
        this.selectedColor = selectedColor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color_selector, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        String color = colorList[position];
        
        try {
            holder.cardColor.setCardBackgroundColor(Color.parseColor(color));
        } catch (Exception e) {
            holder.cardColor.setCardBackgroundColor(Color.parseColor("#0EA5E9"));
        }

        // Update selection state
        if (color.equals(selectedColor)) {
            holder.cardColor.setStrokeColor(Color.parseColor("#263238"));
            holder.cardColor.setStrokeWidth(8);
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.cardColor.setStrokeWidth(0);
            holder.ivCheck.setVisibility(View.GONE);
        }

        holder.cardColor.setOnClickListener(v -> {
            selectedColor = color;
            notifyDataSetChanged();
            if (listener != null) {
                listener.onColorSelected(color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList.length;
    }

    public void updateSelectedColor(String color) {
        this.selectedColor = color;
        notifyDataSetChanged();
    }

    public static class ColorViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardColor;
        ImageView ivCheck;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            cardColor = itemView.findViewById(R.id.cardColorSelector);
            ivCheck = itemView.findViewById(R.id.ivColorCheck);
        }
    }
}