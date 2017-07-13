import com.ilotterytech.hadoop.entity.App;
import com.ilotterytech.hadoop.servlet.Requset;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by liush on 2017/6/7.
 */
public class ApplicationInfo {


    public static void main(String[] args) throws ParseException{
        String appURL="http://name-node1:8088/ws/";
        String appId="application_1496205871088_0215";
        Requset kylinClient=new Requset(appURL);
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
        App app=kylinClient.getApplicationInfo(appId).getApp();
        System.out.println("===jobUuid===="+app.getProgress()+"==="+app.getFinalStatus()+"=="+app.getFinishedTime());
        scheduleAtFixedRate(scheduledThreadPool,kylinClient,appId);

    }


    private static void scheduleAtFixedRate(final ScheduledExecutorService service, final Requset client, final String appId) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long start = new Date().getTime();
                System.out.println("scheduleAtFixedRate 开始执行时间:" +
                        DateFormat.getTimeInstance().format(new Date()));
                App app=client.getApplicationInfo(appId).getApp();
                String status= app.getFinalStatus();
                System.out.println("===="+app.getFinalStatus()+"==="+app.getProgress());
                if("FAILED".equals(status)||"KILL".equals(status)||"FINISHED".equals(status)){
                    service.shutdown();
                }
                long end = new Date().getTime();
                System.out.println("scheduleAtFixedRate 执行花费时间=" + (end - start) / 1000 + "m");
                System.out.println("scheduleAtFixedRate 执行完成时间："
                        + DateFormat.getTimeInstance().format(new Date()));
                System.out.println("======================================");
            }
        }, 1000, 5000, TimeUnit.MILLISECONDS);
    }

}
