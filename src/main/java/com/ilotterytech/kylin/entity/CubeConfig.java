package com.ilotterytech.kylin.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by hxd on 2017/6/6.
 */
@Setter
@Getter
@AllArgsConstructor
public class CubeConfig {

    private Long startTime;

    private Long endTime;

    private BuildType buildType;


}
