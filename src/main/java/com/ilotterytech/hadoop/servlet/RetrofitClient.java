//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ilotterytech.hadoop.servlet;

import com.ilotterytech.retrofit.json.FastJsonConverterFactory;
import com.squareup.okhttp.MediaType;
import java.io.IOException;
import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.Retrofit.Builder;

public abstract class RetrofitClient {
    protected static Retrofit retrofit = null;
    protected final MediaType jsonReq = MediaType.parse("application/json; charset=utf-8");

    public RetrofitClient(String baseUrl) {
        this.initRetrofit(baseUrl);
    }

    private void initRetrofit(String baseUrl) {
        if(!baseUrl.substring(baseUrl.length() - 1).equals("/")) {
            baseUrl = baseUrl + "/";
        }

        if(retrofit == null) {
            retrofit = (new Builder()).baseUrl(baseUrl).addConverterFactory(FastJsonConverterFactory.create()).build();
        }

    }

    protected <T> T executeCall(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if(response.isSuccess()) {
                return response.body();
            } else {
                throw new RuntimeException("execute error:" + response.code() + ":" + response.errorBody().string());
            }
        } catch (IOException var3) {
            throw new RuntimeException("execute error:" + var3.getMessage());
        }
    }
}
