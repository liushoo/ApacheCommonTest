package com.ilotterytech.hadoop.servlet;

import com.ilotterytech.hadoop.entity.Application;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by liushu on 2017/7/7.
 */
public interface Service {
/*

    @PUT("api/cubes/{cubeName}/build")
    Call<BuildResponse> buildCube(@Header("Authorization") String authorization, @Path("cubeName") String cubeName, @Body RequestBody requestBody);
*/



    @GET("v1/cluster/apps/{appId}")
    Call<Application> getApplicationInfo(@Path("appId") String appId);

}
