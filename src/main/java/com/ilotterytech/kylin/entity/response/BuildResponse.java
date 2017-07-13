package com.ilotterytech.kylin.entity.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by hxd on 2017/6/6.
 */
@Setter
@Getter
public class BuildResponse {
    //JOBID
    private String uuid;

    private Long last_modified;

    private String name;
    //BUILD,MERGE,REFRESH
    private String type;

    private Long duration;

    private String related_cube;

    private String related_segment;

    private Long exec_start_time;

    private Long exec_end_time;

    private Long mr_waiting;

    private List<Steps> steps;
    // job_atus:NEW,PENDING(等待),RUNNING,STOPPED
    //FINISHED,ERROR,DISCARDED(丢弃)
    private String job_status;
    //进度
    private Float progress;

}
