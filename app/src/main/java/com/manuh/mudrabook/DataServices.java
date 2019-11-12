package com.manuh.mudrabook;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface DataServices {

    /*@GET("/photos")
    Call<List<RetroPhoto>> getAllPhotos();*/

 /*   @POST("/sendOTP")
    Call<List<>> getOTP();*/

 @POST("/sendOTP")
    Call<POST> getOTP(@Field("mobileNO") String mobileNo);
}