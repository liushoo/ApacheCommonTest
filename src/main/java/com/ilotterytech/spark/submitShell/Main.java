package com.ilotterytech.spark.submitShell;

import org.apache.hadoop.yarn.client.api.YarnClient;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liush on 17-6-20.
 */
public class Main {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args){
        SparkExecutable sparkExecutable=new SparkExecutable();
        StringBuilder stringBuilder = new StringBuilder();
        String argss="--class org.apache.spark.examples.SparkPi  " +
                "--master yarn-cluster " +
                "--jars /var/lib/hadoop-hdfs/spark/lib/ilottery1.1.jar,/var/lib/hadoop-hdfs/spark/lib/jackson-databind-2.4.4.jar "+
                " hdfs://name-node1:8020/user/hdfs/liush/sparklib/spark-examples-1.6.0-cdh5.8.2-hadoop2.6.0-cdh5.8.2.jar " +
                " 10";
        stringBuilder.append("%s/bin/spark-submit %s");
        //stringBuilder.append(" --jars %s  %s");
        String cmd = String.format(stringBuilder.toString(),  sparkExecutable.getSparkHome(),argss);
        PatternedLogger patternedLogger = new PatternedLogger(logger);
        YarnClient client= MonitorState.getClient();
        String appIdk="";
        logger.info("cmd: " + cmd);
        try {
            //
            //ClientConfig.getMonitorLogger(client,patternedLogger);
            MonitorState.getMonitorLogger(client,patternedLogger);
           // System.out.println("appIdk========="+appIdk);

          //  ApplicationId appId=ClientConfig.getAppId(appIdk);
          //  ClientConfig.getMonitorApplication(client
          //          ,appId);
           // ApplicationReport report= client.getApplicationReport(appId);
         //   System.out.println("============"+report.getProgress());

            sparkExecutable.execute(cmd, patternedLogger);

         System.out.println(patternedLogger.getInfo().get("yarn_application_id"));
         System.out.println("============"+ patternedLogger.getInfo()+"==========");

        for (Iterator iter = client.getConfig().iterator(); iter.hasNext();) {
            Map.Entry<String,String> str = (Map.Entry<String,String>)iter.next();
            System.out.println(str.getKey()+"======"+str.getValue());
        }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
