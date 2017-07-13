package com.ilotterytech.spark.util;

/**
 * id生成有关的工具类
 * Created by Zhang on 2017/7/8.
 */
public final class IdGeneratorUtils {
    /**
     * 生成一个以时间为基数的id，同时叠加上传入的seq值
     * 算法：
     * 当前时间的毫秒值，左移16位，然后累加seq值做为唯一id
     * @param seq
     * @return
     */
    public static long genTimestampBasedId(long seq){
        long ret = System.currentTimeMillis();
        ret = ret << 16;
        return ret + seq;
    }

    /**
     * 产生基于一个唯一ID的id
     * 算法：
     * base基数ID，左移4位，然后累加seq值做为唯一值
     * @param base
     * @param seq
     * @return
     */
    public static long genUniqueBasedId(long base, long seq){
        return (base << 4) + seq;
    }


}
