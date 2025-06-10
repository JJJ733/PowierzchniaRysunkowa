package com.example.powierzchniarysunkowa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private List<Image> imageList;
    private final OnImageClickListener listener;

    public interface OnImageClickListener {
        void onClick(Image image);
    }

    public ImageListAdapter(List<Image> imageList, OnImageClickListener listener) {
        this.imageList = imageList;
        this.listener = listener;
    }

    public void setImageList(List<Image> newList) {
        this.imageList = newList;
        notifyDataSetChanged();
    }

    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageListAdapter.ViewHolder holder, int position) {
        Image image = imageList.get(position);
        ((TextView) holder.itemView).setText(image.name);
        holder.itemView.setOnClickListener(v -> listener.onClick(image));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
