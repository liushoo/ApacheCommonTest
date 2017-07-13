package com.ilotterytech.spark.submitShell;



/**
 * Created by liush on 17-6-20.
 */
public class SoutLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
