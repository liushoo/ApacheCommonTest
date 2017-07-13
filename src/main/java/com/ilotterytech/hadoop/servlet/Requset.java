package com.ilotterytech.hadoop.servlet;

import com.ilotterytech.hadoop.entity.Application;
import retrofit.Call;

/**
 * Created by liush on 2017/6/6.
 */
public class Requset extends RetrofitClient {

    private static Service service = null;

    /**
     * 初始化
     */
    public Requset(String url) {

       super(url);
       service=retrofit.create(Service.class);
    }

    public Application getApplicationInfo(String applicationID) {
        Call<Application> call = service.getApplicationInfo(applicationID);
        return  executeCall(call);
    }


}
