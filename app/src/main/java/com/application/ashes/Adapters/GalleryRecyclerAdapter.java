package com.application.ashes.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.ashes.Models.BookSlots;
import com.application.ashes.R;


public class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder>{
    private BookSlots[] listdata;

    // RecyclerView recyclerView;
    public GalleryRecyclerAdapter(BookSlots[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.gallery_recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BookSlots bookSlots = listdata[position];

        holder.sportImageView.setImageResource(listdata[position].getImgId());
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView sportImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.sportImageView = (ImageView) itemView.findViewById(R.id.roundedImageView);

        }
    }
}