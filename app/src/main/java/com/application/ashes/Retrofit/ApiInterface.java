package com.application.ashes.Retrofit;

import com.application.ashes.Models.BookSlotPojo;
import com.application.ashes.Models.LogInParam;
import com.application.ashes.Models.SignUpParam;
import com.application.ashes.Retrofit.Models.BookSlotParams;
import com.application.ashes.Retrofit.Models.LoginModel;
import com.application.ashes.Retrofit.Models.SignUp;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @Headers({"Accept: application/json"})
    @POST("Members")
    Call<SignUp> signUp(@Body SignUpParam signUpParam);

    @Headers({"Accept: application/json"})
    @POST("Members/login")
    Call<LoginModel> login(@Body LogInParam login);

    @Headers({"Accept: application/json"})
    @POST("bookedSlots/fetchCurrentUserBookedSlots")
    Call<BookSlotPojo> getBookingSlots(@Body BookSlotParams date);

    @Headers({"Accept: application/json"})
    @POST("bookedSlots")
    Call<CreateSlots> createSlots(@Body CreateSlots createSlots);

   @Headers({"Accept: application/json"})
    @POST("bookedSlots/deleteBookdSlotById")
    Call<DeleteSlots> deleteSlots(@Body DeleteSlots deleteSlotID);


}