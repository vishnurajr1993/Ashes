package com.application.ashes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.application.ashes.Models.BookSlotPojo;
import com.application.ashes.Models.BookSlots;
import com.application.ashes.R;

import java.util.ArrayList;
import java.util.HashMap;

/*
public class AvailableSlotsAdapter {
}
*/
public class AvailableSlotsAdapter extends BaseAdapter {
    ArrayList<BookSlotPojo.Slot> mList;
    Context mContext;
    public HashMap<Integer, Boolean> hashMapSelected;
    public AvailableSlotsAdapter(Context mContext, ArrayList<BookSlotPojo.Slot> mList){
        this.mList=mList;
        this.mContext=mContext;
        hashMapSelected = new HashMap<>();
        for (int i = 0; i < mList.size(); i++) {
            hashMapSelected.put(i, false);
        }
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public void makeAllUnselect(int position) {
        hashMapSelected.put(position, true);
        for (int i = 0; i < hashMapSelected.size(); i++) {
            if (i != position)
                hashMapSelected.put(i, false);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        gridView = new View(mContext);

        gridView = inflater.inflate(R.layout.available_slot_item, null);
        TextView text=gridView.findViewById(R.id.slot);
        LinearLayout layout=gridView.findViewById(R.id.container);
        text.setText(mList.get(position).getSlot());

        if (hashMapSelected.get(position) == true) {
            layout.setBackgroundResource(R.drawable.selected_slot);
            text.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
        } else {
            if(mList.get(position).getStatus()){
                layout.setBackgroundResource(R.drawable.greem_bg);
                text.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }else{
                layout.setBackgroundResource(R.drawable.loginbtn_bg);
                text.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            }
        }

        return gridView;
    }
}