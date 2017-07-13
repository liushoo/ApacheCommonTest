package com.ilotterytech.hadoop.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by hxd on 2017/6/8.
 */
@Setter
@Getter
public class Job {
    private Integer runningReduceAttempts;
    private Float reduceProgress;
    private Integer failedReduceAttempts;
    private Integer mapsRunning;
    private String state;
    private Integer successfulReduceAttempts;
    private Integer reducesRunning;
    private  Integer reducesPending;
    private String user;
    private Integer reducesTotal;
    private Integer mapsCompleted;
    private Integer startTime;
    private String id;
    private  String successfulMapAttempts;
    private Integer runningMapAttempts;
    private Integer newReduceAttempts;
    private  String name;
    private Integer mapsPending;
    private Integer elapsedTime;
    private  Integer reducesCompleted;
    private Integer mapProgress;
    private  String diagnostics;
    private Integer failedMapAttempts;
    private Integer killedReduceAttempts;
    private  Integer mapsTotal;
    private Boolean uberized;
    private Integer killedMapAttempts;
    private Integer finishTime;
    private List<Acls> acls;
}
