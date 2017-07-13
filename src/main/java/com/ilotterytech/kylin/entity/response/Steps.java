package com.ilotterytech.kylin.entity.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by hxd on 2017/6/6.
 */
@Getter
@Setter
public class Steps {

    private String interruptCmd;

    private String name;

    private Long sequence_id;

    private String exec_cmd;

    private String interrupt_cmd;

    private Long exec_start_time;

    private Long exec_end_time;

    private Long exec_wait_time;

    private String step_status;

    private String cmd_type;

    private Object info;

    private Boolean run_async;
}
