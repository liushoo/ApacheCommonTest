package com.ilotterytech.hadoop.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;


import java.util.Date;

/**
 * Created by liush on 17-6-8.
 */
@Data
public class App {
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date finishedTime;
    private String amContainerLogs;
    private String trackingUI;
    private String user;
    private String id;
    private String clusterId;
    private String finalStatus;
    private String amHostHttpAddress;
    private Float progress;
    private String name;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startedTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date elapsedTime;
    private String diagnostics;
    private String trackingUrl;
    private String queue;

}
