package com.application.ashes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.application.ashes.Adapters.BookSlotRecyclerViewAdapter;
import com.application.ashes.Adapters.GalleryRecyclerAdapter;
import com.application.ashes.Models.BookSlots;
import com.application.ashes.R;

public class Dashboard extends AppCompatActivity {
    TextView about;
    RecyclerView bookSlotsrecycler,galleryRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();
    }

    private void init() {
        about=findViewById(R.id.about);
        about.setText(getResources().getString(R.string.about));
        galleryRecycler=findViewById(R.id.gallery_recycler);
        bookSlotsrecycler=findViewById(R.id.bookSlotRecycler);
        galleryRecycler.setHasFixedSize(true);
        bookSlotsrecycler.setHasFixedSize(true);
        BookSlots[] galleryData = new BookSlots[]{
                new BookSlots(android.R.drawable.ic_dialog_email),
                new BookSlots( android.R.drawable.ic_dialog_info),
                new BookSlots( android.R.drawable.ic_delete),
                new BookSlots( android.R.drawable.ic_dialog_dialer),
                new BookSlots( android.R.drawable.ic_dialog_dialer),
                new BookSlots( android.R.drawable.ic_dialog_dialer),
                new BookSlots( android.R.drawable.ic_dialog_dialer),
                new BookSlots( android.R.drawable.ic_dialog_dialer),
                new BookSlots( android.R.drawable.ic_dialog_dialer),

        };
        GalleryRecyclerAdapter galleryRecyclerAdapter=new GalleryRecyclerAdapter(galleryData);
        galleryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        galleryRecycler.setAdapter(galleryRecyclerAdapter);

        BookSlots[] slotData = new BookSlots[]{
                new BookSlots("desc","Cricket",R.drawable.cricket),
                new BookSlots( "desc","Badminton",R.drawable.badminton),
                new BookSlots( "desc","Games",R.drawable.games),
                new BookSlots( "desc","Cricket",R.drawable.cricket),

        };
        BookSlotRecyclerViewAdapter bookSlotRecyclerViewAdapter=new BookSlotRecyclerViewAdapter(slotData,this);
        bookSlotsrecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bookSlotsrecycler.setAdapter(bookSlotRecyclerViewAdapter);
    }
}
