package com.ilotterytech.kylin.client;

import com.alibaba.fastjson.JSON;
import com.ilotterytech.hadoop.servlet.RetrofitClient;
import com.ilotterytech.kylin.entity.CubeConfig;
import com.ilotterytech.kylin.entity.KylinConfig;
import com.ilotterytech.kylin.entity.response.BuildResponse;
import com.ilotterytech.kylin.entity.response.KylinJob;
import com.squareup.okhttp.RequestBody;
import retrofit.Call;

/**
 * Created by hxd on 2017/6/6.
 */
public class KylinClient extends RetrofitClient{

    private KylinConfig kylinConfig = null;

    private static KylinService service = null;

    /**
     * 初始化
     */
    public KylinClient(KylinConfig kylinConfig) {
        super(kylinConfig.getUrl());
        this.kylinConfig=kylinConfig;
        service=retrofit.create(KylinService.class);

    }

    public BuildResponse buildCube(CubeConfig cubeConfig) {
        Call<BuildResponse> call = service.buildCube(kylinConfig.getAuthorization(),kylinConfig.getCubeName(), RequestBody.create(jsonReq, JSON.toJSONString(cubeConfig)));
        return executeCall(call);
    }


    public KylinJob jobResume(KylinJob job) {
        //kylinConfig.getAuthorization(),
        Call<KylinJob> call = service.jobResume(kylinConfig.getAuthorization(),job.getUuid());
        return executeCall(call);
    }

}
