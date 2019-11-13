package com.manuh.mudrabook;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataServices {

    /*@GET("/photos")
    Call<List<RetroPhoto>> getAllPhotos();*/

 /*   @POST("/sendOTP")
    Call<List<>> getOTP();*/


 @POST("sendOTP")
 Call<POST> getOTP(@Query("mobileNo") String mobileNo);

 @POST("validateOTPWithMobile")
 Call<POST> verifyOTP (@Query("mobileNo") String mobileNo, @Query("otp") String otp);
}