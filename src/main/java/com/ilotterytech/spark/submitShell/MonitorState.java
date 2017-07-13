package com.ilotterytech.spark.submitShell;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by liush on 17-6-20.
 */
public class MonitorState {
    /**
     * 获取Client ,用于获取Spark任务的状态
     * @return
     */
    private static YarnClient client = null;
    public static YarnClient getClient() {
        if(client == null){
            client = YarnClient.createYarnClient();
            client.init(getConf());
            client.start(); // TODO start
        }
        return client;
    }
    private static Configuration hadoopConf = null;
    public static Configuration getConf() {

        if (hadoopConf == null) {
        // 设置 提交任务系统版本，需要在，这种设置不行，提示UninLoginModel少包
            hadoopConf = new Configuration();
            // get configuration from db or file
            hadoopConf.setBoolean("mapreduce.app-submission.cross-platform", true);// 配置使用跨平台提交任务
            // 指定resourcemanager
            hadoopConf.set("yarn.resourcemanager.address", "name-node1:8032");
            hadoopConf.set("yarn.resourcemanager.scheduler.address", "name-node1:8030");// 指定资源分配器
            hadoopConf.set("fs.defaultFS", "hdfs://name-node1:8020");
            hadoopConf.set("yarn.application.classpath","/opt/cloudera/parcels/CDH/lib/spark/lib/spark-assembly.jar:/opt/cloudera/parcels/CDH/lib/hadoop/lib/*:/opt/cloudera/parcels/CDH/lib/hadoop/*:/opt/cloudera/parcels/CDH/lib/hadoop-hdfs/lib/*:/opt/cloudera/parcels/CDH/lib/hadoop-hdfs/*:/opt/cloudera/parcels/CDH/lib/hadoop-mapreduce/lib/*:/opt/cloudera/parcels/CDH/lib/hadoop-mapreduce/*:/opt/cloudera/parcels/CDH/lib/hadoop-yarn/lib/*:/opt/cloudera/parcels/CDH/lib/hadoop-yarn/*:/opt/cloudera/parcels/CDH/lib/hive/lib/*:/opt/cloudera/parcels/CDH/lib/flume-ng/lib/*:/opt/cloudera/parcels/CDH/lib/paquet/lib/*:/opt/cloudera/parcels/CDH/lib/avro/lib/*:file:///var/lib/hadoop-hdfs/spark/lib/*");
        }

        return hadoopConf;
    }
    /**
     * 根据JobId构造ApplicationId
     * @param jobId
     * @return
     */
    public static ApplicationId getAppId(String jobId){
        return ConverterUtils
                .toApplicationId(jobId);
    }

    public  static void getMonitorApplication(final YarnClient client, final ApplicationId appId){
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Thread.sleep(5000);
                    YarnApplicationState lastState= null;
                    ApplicationReport report  =null;
                    report= client.getApplicationReport(appId);
                    YarnApplicationState state = report.getYarnApplicationState();
                    boolean finsh=(state == YarnApplicationState.FINISHED ||
                            state == YarnApplicationState.FAILED ||
                            state == YarnApplicationState.KILLED);
                    while (!finsh) {
                        Thread.sleep(5000);
                        report= client.getApplicationReport(appId);
                        state = report.getYarnApplicationState();
                        report.getProgress();
                        if (state == YarnApplicationState.FINISHED ||
                                state == YarnApplicationState.FAILED ||
                                state == YarnApplicationState.KILLED) {
                            System.out.println("==FINISHED===="+state);
                            return;
                        }

                        if (state == YarnApplicationState.RUNNING) {
                            //  return (state, report.getFinalApplicationStatus,report)
                            System.out.println("==="+report.getProgress()+"===="+state);
                            continue;
                        }
                    }
                } catch (YarnException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public  static void getMonitorLogger(final YarnClient client, final PatternedLogger appId){
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    YarnApplicationState lastState= null;
                    ApplicationReport report  =null;
                   String  appIdk=appId.getInfo().get("yarn_application_id");
                    while (true) {
                        if (appIdk !=null&& !appIdk.isEmpty()) {
                            ApplicationId  appID= getAppId(appIdk);
                            getMonitorApplication(client,appID);
                            System.out.println("==="+appIdk);
                          //  YarnApplicationState state = report.getYarnApplicationState();
                          //  System.out.println("==FINISHED===="+state);
                            return;
                        }else{
                            continue;
                        }


                    }
                }  catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
