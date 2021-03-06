package com.application.ashes.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.application.ashes.Models.BookSlotPojo;
import com.application.ashes.R;
import com.application.ashes.Retrofit.ApiInterface;
import com.application.ashes.Retrofit.CreateSlots;
import com.application.ashes.Retrofit.DeleteSlots;
import com.application.ashes.Retrofit.Models.BookSlotParams;
import com.application.ashes.Retrofit.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSlotsActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    SharedPreferences permissionStatus;
    private boolean sentToSettings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_slots);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        SmsPermission();
        SendMessage("7510387713","Ashes");
        getBookedSlots();

    }

    /*
    basically it call the slot availability API with current date.If the user wants to get the slot availability of a specific date use date picker
    @SetType
           0-Machine bowling
           1-half ground
           2-full ground    use dropdown to choose

     */
    private void getBookedSlots() {
        ApiInterface apiInterface2 = ServiceGenerator.createService(ApiInterface.class);
        String date = getDateTime();
        BookSlotParams bookSlotParams = new BookSlotParams();
        bookSlotParams.setDate(date);
        bookSlotParams.setType(0);
        Log.d("TAG", date);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(bookSlotParams);
        Log.d("Merly", json);
        Call<BookSlotPojo> call = apiInterface2.getBookingSlots(bookSlotParams);
        call.enqueue(new Callback<BookSlotPojo>() {
            @Override
            public void onResponse(Call<BookSlotPojo> call, Response<BookSlotPojo> response) {
                BookSlotPojo result = response.body();
                //bookSlots(0);
                Log.d("TAG", response.body().toString());

            }

            @Override
            public void onFailure(Call<BookSlotPojo> call, Throwable t) {

                Log.d("TAG", t.toString());

                call.cancel();
            }
        });
    }


    /*
    Method used to lock and save a slot
    @status used to identify the status of the slot
          1-payment successful

     */
    private void bookSlots(int status) {
        ApiInterface apiInterface2 = ServiceGenerator.createService(ApiInterface.class);
        String date = getDateTime();
        CreateSlots createSlots = new CreateSlots();
        createSlots.setDate(date);
        createSlots.setStatus(status);
        createSlots.setSlot("11 AM - 12 AM");//the time string should be in the format given by API
        createSlots.setSlotId("603e7e39f87d755e5a59a0ef");// from API reponse
        createSlots.setAmount("500");
        createSlots.setType(0);
        Log.d("TAG", date);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(createSlots);
        Log.d("Merly", json);
        Call<CreateSlots> call = apiInterface2.createSlots(createSlots);
        call.enqueue(new Callback<CreateSlots>() {
            @Override
            public void onResponse(Call<CreateSlots> call, Response<CreateSlots> response) {
                CreateSlots result = response.body();
                String id = response.body().getId();
                // initiate paymen

            }

            @Override
            public void onFailure(Call<CreateSlots> call, Throwable t) {
               //delete slot
                Log.d("TAG", t.toString());

                call.cancel();
            }
        });
    }

    /*
    Delete slot with booked id when payment fails
      @id Booking id
     */
    private void deleteSlots(String id) {
        ApiInterface apiInterface2 = ServiceGenerator.createService(ApiInterface.class);
        DeleteSlots deleteSlots = new DeleteSlots();
        deleteSlots.setId(id);
        Call<DeleteSlots> call = apiInterface2.deleteSlots(deleteSlots);
        call.enqueue(new Callback<DeleteSlots>() {
            @Override
            public void onResponse(Call<DeleteSlots> call, Response<DeleteSlots> response) {
                Log.d("TAAG", response.body().toString());
               // send Message

            }

            @Override
            public void onFailure(Call<DeleteSlots> call, Throwable t) {
                Log.d("TAAG", t.toString());

            }
        });
    }

    public String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public void SmsPermission() {
        if (ActivityCompat.checkSelfPermission(BookSlotsActivity.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BookSlotsActivity.this, Manifest.permission.SEND_SMS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookSlotsActivity.this);
                builder.setTitle("Need SMS Permission");
                builder.setMessage("This app needs SMS permission to send Messages.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(BookSlotsActivity.this,
                                new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.SEND_SMS, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookSlotsActivity.this);
                builder.setTitle("Need SMS Permission");
                builder.setMessage("This app needs SMS permission to send Messages.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(),
                                "Go to Permissions to Grant SMS permissions", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(BookSlotsActivity.this, new String[]{Manifest.permission.SEND_SMS}
                        , SMS_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.SEND_SMS, true);
            editor.commit();

        }
    }

    /*

    /*Send Message to the user about the slot status
    @strMobileNo Reciepient number
    @StrMessage Message about the booking Id(Time,Slot id,Place,Status)
     */

    public void SendMessage(String strMobileNo, String strMessage) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(strMobileNo, null, strMessage, null, null);
            Toast.makeText(getApplicationContext(), "Your Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }
}