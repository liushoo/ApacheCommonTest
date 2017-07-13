package com.ilotterytech.kylin;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liush on 17-6-7.
 */
public class Main {
    /**
     * 1,根据构建日期段
     * 2,构建club线程监控club完成情况
     * 3,启动线程监控club完成情况
     *
     * @param args
     */
    public static void main(String[] args) {
        final String jobID = "my_job_1";
        final AtomicInteger count = new AtomicInteger(0);
        final Map<String, Future> futures = new HashMap<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Calendar calst = Calendar.getInstance();
        System.out.println(calst.getTimeInMillis());

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
        scheduleAtFixedRate(scheduledThreadPool,count);
        //scheduleAtFixedRate(scheduledThreadPool, 6000);

       // scheduleWithFixedDelay(scheduledThreadPool, 1000);
        //scheduleWithFixedDelay(scheduledThreadPool, 6000);
        // 从现在开始1秒钟之后，每隔1秒钟执行一次job1
/*        scheduledThreadPool.schedule(new Runnable() {
            public void run() {
                System.out.println(count.getAndIncrement());
                System.out.println("delay 3 seconds");
                if (count.get() > 10) {
                    System.out.println("===shutdown===");
                    scheduledThreadPool.shutdown();
                    System.out.println("======");

                }
            }
        }, 3,TimeUnit.SECONDS);*/

        // futures.put(jobID, future);
        //  scheduledThreadPool.await();

        //scheduler.shutdown();
    }

    private static void scheduleAtFixedRate(final ScheduledExecutorService service, final AtomicInteger count) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                long start = new Date().getTime();
                System.out.println("scheduleAtFixedRate 开始执行时间:" +
                        DateFormat.getTimeInstance().format(new Date()));
             System.out.println("===="+count.incrementAndGet());
                if(count.get()>10){
                    service.shutdown();
                }

           /*     try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                long end = new Date().getTime();
                System.out.println("scheduleAtFixedRate 执行花费时间=" + (end - start) / 1000 + "m");
                System.out.println("scheduleAtFixedRate 执行完成时间："
                        + DateFormat.getTimeInstance().format(new Date()));
                System.out.println("======================================");
            }
        }, 1000, 5000, TimeUnit.MILLISECONDS);
    }

}
