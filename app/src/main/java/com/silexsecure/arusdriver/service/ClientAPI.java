package com.silexsecure.arusdriver.service;


import com.silexsecure.arusdriver.model.CarRepair;
import com.silexsecure.arusdriver.model.ChangePasswordRequest;
import com.silexsecure.arusdriver.model.ChangePasswordResponse;
import com.silexsecure.arusdriver.model.LoginRequest;
import com.silexsecure.arusdriver.model.LoginResponse;
import com.silexsecure.arusdriver.model.OtpVerifyRequest;
import com.silexsecure.arusdriver.model.OtpVerifyResponse;
import com.silexsecure.arusdriver.model.RegisterDriverRequest;
import com.silexsecure.arusdriver.model.RegisterRequest;
import com.silexsecure.arusdriver.model.RegisterResponse;
import com.silexsecure.arusdriver.model.ReportsHistoryResponse;
import com.silexsecure.arusdriver.model.TowinRequestResponse;
import com.silexsecure.arusdriver.model.TowingRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Habeeb-Bizzdesk on 4/11/18.
 */

public interface ClientAPI {

//    @Headers("Content-Type: application/json")
//    @POST("user")
//    Call<ResponseObject> Register(@Body User user);
//
//    @Headers("Content-Type: application/json")
//    @POST("rides")
//    Call<RideResponse> SendRequest(@Header("Authorization") String token, @Body RideRequest request);
//
//    @Headers("Content-Type: application/json")
//    @POST("ratings")
//    Call<RateResponse> RateReview(@Header("Authorization") String token, @Body RateRequest request);

//    @Headers("Content-Type: application/json")
//    @PUT("rides/{rideId}")
//    Call<RideResponse> CancelRide(@Header("Authorization") String token, @Path("rideId") String rideId, @Body Status status);

    @Headers("Content-Type: application/json")
    @POST("login_driver")
    Call<LoginResponse> LoginUser(@Body LoginRequest request);

    @Headers("Content-Type: application/json")
    @POST("towingrequest")
    Call<TowinRequestResponse> sendRequestToDB(@Body TowingRequest request);

    @Headers("Content-Type: application/json")
    @POST("towingrequest")
    Call<TowinRequestResponse> sendMechanicRequestToDB(@Body CarRepair request);

    @Headers("Content-Type: application/json")
    @POST("register")
    Call<RegisterResponse> RegisterUser(@Body RegisterRequest request);

    @Headers("Content-Type: application/json")
    @POST("register_driver")
    Call<RegisterResponse> RegisterDriver(@Body RegisterDriverRequest request);

    @Headers("Content-Type: application/json")
    @POST("driver_loginWithOtp")
    Call<OtpVerifyResponse> SendOTP(@Body OtpVerifyRequest request);

    @Headers("Content-Type: application/json")
    @GET("consumersreports/getconsumerreports/{userid}")
    Call<ReportsHistoryResponse> ReportHistory(@Header("Authorization") String token, @Path("userid") int username);

    @Headers("Content-Type: application/json")
    @POST("driver_update_pass/{user_id}")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request, @Path("user_id") String userId);


    @Headers("Content-Type: application/json")
    @GET("https://api.paystack.co/bank/resolve?/account_number={account_number}&bank_code={bank_code}")
    Call<ReportsHistoryResponse> ReportHistory(@Header("Authorization") String token, @Path("account_number") int acctNo, @Path("bank_code") String bank_code );

//    @Headers("Content-Type: application/json")
//    @GET("/notification/1")
//    Call<ArrayList<Notification>> Notification(@Header("Authorization") String token, @Query("city") String city);
//
//    @Headers("Content-Type: application/json")
//    @GET("reward")
//    Call<ArrayList<RewardsModel>> Rewards(@Header("Authorization") String token);
//
//    @Headers("Content-Type: application/json")
//    @GET("passenger/rides/")
//    Call<HistoryModel> History(@Header("Authorization") String token, @Query("id") String id);


}
