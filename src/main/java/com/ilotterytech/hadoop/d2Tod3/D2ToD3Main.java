package com.ilotterytech.hadoop.d2Tod3;


import com.ilotterytech.hadoop.entity.App;
import com.ilotterytech.hadoop.servlet.Requset;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by SJ on 2017-5-30.
 */
public class D2ToD3Main {

    private static final String DELIMETER = "\t";
    private static Job job = null;


    public static void main(String[] args) throws ClassNotFoundException,
            InterruptedException, IOException {
        D2ToD3Main main = new D2ToD3Main();
        String applicationId = main.runMR();
        System.out.println("applicationId:" + applicationId);
    }

    public String runMR() throws InterruptedException {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
        String applicationId = null;

        // new Runnable() {
        //   public void run() {
        String[] args = new String[6];
        args[0] = "/user/hdfs/d2/shuangse/consume/2015/*/*/*";
        args[1] = "/user/hdfs/d2/shuangse/ticket/2015/*/*/*";
        args[2] = "/user/hdfs/d2/shuangse/temp";
        args[3] = "/user/hdfs/d2/shuangse/D.csv";//日期维表
        //       args[4] = "/user/hdfs/d2/shuangse/T.csv";//时间维表
//1.	《销售方维ID》《省》《市》《区县》《站点类型》:单机店、双机店、多机店。《彩票站编号》：彩票站8位数字编号。《公司名称》：管理公司名称。《投注机编号》：投注机属性，8位数字编号。
        args[5] = "hdfs:////user/zhongguoliqi/DB_20170430/S/S2017_0226.csv";

        Configuration conf = new Configuration();
        conf.set("DELIMETER", DELIMETER);

//         conf.set("df.default.name", "hdfs://10.128.7.140:9000");
        //args[6] ="/etc/hadoop/conf/core-site.xml";
        conf.addResource(new Path("/etc/hadoop/conf/core-site.xml"));
        // args[7] ="/etc/hadoop/conf/mapred-site.xml";
        conf.addResource(new Path("/etc/hadoop/conf/mapred-site.xml"));
        //args[8] ="/etc/hadoop/conf/hdfs-site.xml";
        conf.addResource(new Path("/etc/hadoop/conf/hdfs-site.xml"));
        // args[9] ="/etc/hadoop/conf/yarn-site.xml";
        conf.addResource(new Path("/etc/hadoop/conf/yarn-site.xml"));
        System.out.println("mapreduce class path: " + conf.get("mapreduce.application.classpath"));
        try {
            job = Job.getInstance(conf);
            job.setJarByClass(D2ToD3Main.class);
            job.setJobName("D2 to D3 Test");

            FileSystem fs = FileSystem.get(conf);
            System.out.println(fs.exists(new Path(args[2])));
            if (fs.exists(new Path(args[2]))) {
                fs.delete(new Path(args[2]), true);
                System.out.println("deleting output file temp");
            }

            //input and output file paths
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileInputFormat.addInputPath(job, new Path(args[1]));
            FileOutputFormat.setOutputPath(job, new Path(args[2]));

            job.setMapOutputKeyClass(KeyClass.class);
            job.setMapOutputValueClass(Text.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            job.setOutputFormatClass(TextOutputFormat.class);

            //Set the MapClass and ReduceClass in the job
            job.setMapperClass(MyMapper.class);
            job.setReducerClass(MyReducer.class);
            job.setPartitionerClass(MyPartitioner.class);

            job.setGroupingComparatorClass(GroupComparator.class);

            // /user/zhongguoliqi/DB_20170430/D
            // /user/zhongguoliqi/DB_20170430/T
            System.out.println(conf.toString());
            System.out.println(job.getConfiguration().toString());
            //job.addCacheFile(new Path(args[3]).toUri());
            //job.addCacheFile(new Path(args[5]).toUri());

           /* new Thread(new Runnable() {
                volatile boolean exit = false;

                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        while (!job.getStatus().isJobComplete()) {
                            //job.getCounters().findCounter("");
                            long startTime = job.getStartTime();
                            long finishTime = job.getFinishTime();
                            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Date d = new Date();
                            d.setTime(startTime);
                            String s = format1.format(d);
                            d.setTime(finishTime);
                            String f = format1.format(d);
                            //d.setTime(null);
                            System.out.println(job.getStatus());
                            *//**
                             * 取出mapredurce
                             * 	Map-Reduce Framework
                             Map input records=3598358
                             Map output records=3598358
                             Map output bytes=158394135
                             *//*
                            Counters counters = job.getCounters();
                            long inputvlaued = counters.getGroup("org.apache.hadoop.mapreduce.TaskCounter").findCounter("MAP_INPUT_RECORDS").getValue();
                            long outputvlauec = counters.getGroup("org.apache.hadoop.mapreduce.TaskCounter").findCounter("MAP_OUTPUT_RECORDS").getValue();
                            System.out.println("startTime:" + s + "==finishTime:==" + f + "==jobStatus:==" + job.getStatus().getState().getValue() + "==mapProgress===" + job.mapProgress()
                                    + "==reduceProgress:==" + job.reduceProgress() + "==setupProgress:==" + job.setupProgress());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();*/
            //MonitorMapRedurce.getMonitorMapRedurceInfo(job);
            //MonitorMapRedurce.getMonitorMapRedurceCountersInfo(job);
            job.waitForCompletion(true);
            Counters counters = job.getCounters();




            JobID id = job.getJobID();
            System.out.println("job id: " + id.toString() + "\t" + id.getId() + "\t" + id.getJtIdentifier());
            if (job.isSuccessful()) {
                System.out.println("Job was successful");
            } else if (!job.isSuccessful()) {
                System.out.println("Job was not successful");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

        //}
        //  }.run();


        System.out.println(job.getJobID().toString());
        applicationId = job.getJobID().toString().replace("job", "application");

        /*String appURL="http://name-node1:8088/ws/";
        String appId="application_1496205871088_0215";
        Requset kylinClient=new Requset(appURL);
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
        App app=kylinClient.getApplicationInfo(applicationId).getApp();
        System.out.println("===jobUuid===="+app.getProgress()+"==="+app.getFinalStatus()+"=="+app.getFinishedTime());
        scheduleAtFixedRate(scheduledThreadPool,kylinClient,appId);*/

        return applicationId;
    }

    private static void scheduleAtRate(final ScheduledExecutorService service, final Job t) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long start = new Date().getTime();
                System.out.println("scheduleAtFixedRate 开始执行时间:" +
                        DateFormat.getTimeInstance().format(new Date()));
                // App app=client.getApplicationInfo(appId).getApp();
                try {
                    if (t.isComplete() || t.isSuccessful()) {
                        service.shutdown();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //String status=t.getState().name();


                /*try {
                   long finishTime = t.getStartTime();
                   boolean complete = t.isComplete();
                   System.out.println(t.cleanupProgress());

                    System.out.println("st:" + t.getStartTime() + "==ft:==" + finishTime + "==cp:==" + complete + "===mp==" + t.mapProgress()
                            + "==rp:==" + t.reduceProgress() + "==sp:==" + t.setupProgress());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                long end = new Date().getTime();
                System.out.println("scheduleAtFixedRate 执行花费时间=" + (end - start) / 1000 + "m");
                System.out.println("scheduleAtFixedRate 执行完成时间："
                        + DateFormat.getTimeInstance().format(new Date()));
                System.out.println("======================================");
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }


    private static void scheduleAtFixedRate(final ScheduledExecutorService service, final Requset client, final String appId) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long start = new Date().getTime();
                System.out.println("scheduleAtFixedRate 开始执行时间:" +
                        DateFormat.getTimeInstance().format(new Date()));
                App app = client.getApplicationInfo(appId).getApp();
                String status = app.getFinalStatus();
                System.out.println(app.getStartedTime() + "====" + app.getFinalStatus() + "===" + app.getProgress() + "==" + app.getFinishedTime());
                if ("FAILED".equals(status) || "KILL".equals(status) || "FINISHED".equals(status) || "SUCCEEDED".equals(status)) {
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
