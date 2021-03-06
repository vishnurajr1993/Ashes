package com.application.ashes.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.application.ashes.Models.BookSlots;
import com.application.ashes.R;

/*
public class BookSlotRecyclerViewAdapter {
}
*/
public class BookSlotRecyclerViewAdapter extends RecyclerView.Adapter<BookSlotRecyclerViewAdapter.ViewHolder>{
    private BookSlots[] listdata;
    private Context context;

    // RecyclerView recyclerView;
    public BookSlotRecyclerViewAdapter(BookSlots[] listdata, Context context) {
        this.context=context;
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.bookslot_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BookSlots bookSlots = listdata[position];
        holder.title.setText(listdata[position].getTitle());
        holder.desc.setText(listdata[position].getDescription());
        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       // holder.sportImageView.setImageDrawable(ContextCompat.getDrawable(context, listdata[position].getImgId()));
        //holder.sportImageView.setImageResource(listdata[position].getImgId());
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView sportImageView;
        public TextView title;
        public TextView desc;
        public LinearLayout linearlayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.sportImageView = (ImageView) itemView.findViewById(R.id.sportImageview);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.desc = (TextView) itemView.findViewById(R.id.desc);
            this.linearlayout=itemView.findViewById(R.id.linearlayout);
           // relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}