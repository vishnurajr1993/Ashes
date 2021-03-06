package com.application.ashes.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.application.ashes.Adapters.AvailableSlotsAdapter;
import com.application.ashes.Models.BookSlotPojo;
import com.application.ashes.R;
import com.application.ashes.Retrofit.ApiInterface;
import com.application.ashes.Retrofit.CreateSlots;
import com.application.ashes.Retrofit.DeleteSlots;
import com.application.ashes.Retrofit.Models.BookSlotParams;
import com.application.ashes.Retrofit.ServiceGenerator;
import com.application.ashes.Utils.Preferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView back;
    Button pay;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    int amount=200;
    String upiId = "7907124184@ybl";
    String transactionNote = "Ashes slot booking fee";
    String status;
    Uri uri;
    String name = "Ashes";
    GridView gridView;
    final Calendar myCalendar = Calendar.getInstance();
    EditText datepicker;
    RadioGroup rg ;
    ToggleButton lights;
    String slotName="";
    String slotId="";
    boolean isSlotSelected=false;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();
        retrofitBookSlots();
    }
    private void retrofitBookSlots() {
        ApiInterface apiInterface2 = ServiceGenerator.createService(ApiInterface.class);
        String date = datepicker.getTag().toString().trim();
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
                final BookSlotPojo result = response.body();
                //bookSlots(0);
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", response.body().toString());
                final AvailableSlotsAdapter customAdapter = new AvailableSlotsAdapter(PaymentActivity.this,result.getSlots());
                gridView.setAdapter(customAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @SuppressLint("ShowToast")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(!result.getSlots().get(position).getStatus()) {
                            if(position>=13){
                                lights.setChecked(true);
                            }else{
                                lights.setChecked(false);
                            }
                            slotName=result.getSlots().get(position).getSlot();
                            slotId=result.getSlots().get(position).getId();
                            isSlotSelected=true;
                            view.setBackgroundColor(Color.WHITE);
                            customAdapter.makeAllUnselect(position);
                            customAdapter.notifyDataSetChanged();
                            Log.d("TAG", "onItemClick: " + result.getSlots().get(position).getId());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<BookSlotPojo> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                Log.d("TAG", t.toString());

                call.cancel();
            }
        });
    }
    private void bookSlots(int status,String slot,String slotId) {
        ApiInterface apiInterface2 = ServiceGenerator.createService(ApiInterface.class);
        String date = getDateTime("YYYY-MM-dd");
        CreateSlots createSlots = new CreateSlots();
        createSlots.setDate(date);
        createSlots.setStatus(status);
        createSlots.setSlot(slot);//the time string should be in the format given by API
        createSlots.setSlotId(slotId);// from API reponse
        createSlots.setAmount(String.valueOf(amount));
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
                Preferences.writeString(PaymentActivity.this,Preferences.BOOKING_ID,id);
                // initiate paymen
                uri = getUpiPaymentUri(name,upiId, transactionNote, String.valueOf(amount));
                payWithGPay();

            }

            @Override
            public void onFailure(Call<CreateSlots> call, Throwable t) {
                //delete slot
                Log.d("TAG", t.toString());

                call.cancel();
            }
        });
    }
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
    public String getDateTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }
    private void init() {
        back=findViewById(R.id.back);
        pay=findViewById(R.id.pay);
        back.setOnClickListener(this);
        pay.setOnClickListener(this);
        gridView=findViewById(R.id.slotsGrid);
        progressBar=findViewById(R.id.progressBar);
        datepicker=findViewById(R.id.datePicker);
         rg = (RadioGroup) findViewById(R.id.ground_selector);
         lights=findViewById(R.id.lights);
        datepicker.setTag(getDateTime("YYYY-MM-dd"));
        datepicker.setText(getDateTime("dd-MM-yyyy"));
        pay.setText("Pay "+String.valueOf(amount));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                view.setMinDate(System.currentTimeMillis() - 1000);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        datepicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dp=  new DatePickerDialog(PaymentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                        dp.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                        dp.show();

            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio0:

                        lights.setChecked(false);
                        amount=200;
                        pay.setText("Pay "+String.valueOf(amount));
                        break;
                    case R.id.radio1:
                        lights.setChecked(false);
                        amount=400;
                        pay.setText("Pay "+String.valueOf(amount));
                        break;
                    case R.id.radio2:
                        lights.setChecked(false);
                        amount=500;
                        pay.setText("Pay "+String.valueOf(amount));
                        break;
                }
            }
        });
        lights.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    amount+=200;
                    pay.setText("Pay "+String.valueOf(amount));
                }
                else
                {
                   amount-=200;
                    pay.setText("Pay "+String.valueOf(amount));
                }
            }
        });

    }
    private void updateLabel() {
        String myFormat = "dd-MM-yyyy";
        String myFormat2 = "YYYY-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);

        datepicker.setText(sdf.format(myCalendar.getTime()));
        datepicker.setTag(sdf2.format(myCalendar.getTime()));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.pay :
                    if(isSlotSelected) {

                bookSlots(0,slotName,slotId);
                   }else{
                        Toast.makeText(this, "Please Select a Time Slot", Toast.LENGTH_SHORT).show();
                    }
                break;
        }

    }
    private static Uri getUpiPaymentUri(String name,String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }
    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(PaymentActivity.this, "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == resultCode) && status.equals("success")) {
            Toast.makeText(PaymentActivity.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PaymentActivity.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
            deleteSlots(Preferences.readString(PaymentActivity.this,Preferences.BOOKING_ID,""));
        }
    }
}
