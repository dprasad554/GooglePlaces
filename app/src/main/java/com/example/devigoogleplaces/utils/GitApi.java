package com.example.devigoogleplaces.utils;


import com.example.devigoogleplaces.bean.MyPlaces;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GitApi {
    @GET("json?")
    Call<MyPlaces> googleresponce(@Query("location") String location,
                                  @Query("radius") int radius,
                                  @Query("type") String type,
                                  @Query("sensor") String sensor,
                                  @Query("key") String key);

}
