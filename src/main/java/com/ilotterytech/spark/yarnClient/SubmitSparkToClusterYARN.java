package com.ilotterytech.spark.yarnClient;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.deploy.yarn.Client;
import org.apache.spark.deploy.yarn.ClientArguments;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Usage: org.apache.spark.deploy.yarn.Client [options]
 |Options:
 |  --jar JAR_PATH           Path to your application's JAR file (required in yarn-cluster mode)
 |  --class CLASS_NAME       Name of your application's main class (required)
 |  --primary-py-file        A main Python file
 |  --primary-r-file         A main R file
 |  --arg ARG                Argument to be passed to your application's main class.
 |                           Multiple invocations are possible, each will be passed in order.
 * Created by liush on 17-6-22.
 */

public class SubmitSparkToClusterYARN {
  static final Logger logger = Logger.getLogger(SubmitSparkToClusterYARN.class);
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        // HDFS
        //String sparkExamplesJar = "hdfs://name-node1:8020/user/hdfs/liush/sparklib/spark-examples-1.6.0-cdh5.8.2-hadoop2.6.0-cdh5.8.2.jar";
        //String sparkExamplesJar = "hdfs://name-node1:8020/user/hdfs/liush/sparklib/ilottery-1.0-SNAPSHOT.jar";
        //local
        //String sparkExamplesJar = "/var/lib/hadoop-hdfs/spark/lib/spark-examples-1.6.0-cdh5.8.2-hadoop2.6.0-cdh5.8.2.jar";
        final String[] argsb = new String[]{
                "--jar",
                //sparkExamplesJar,
               "/home/liush/ilottery/build/libs/ilottery-1.0-SNAPSHOT.jar",
                "--class",
               //"org.apache.spark.examples.SparkPi"
                 "com.ilotterytech.spark.sql.jdbc.RDDRelationBySQL",
                         "--files",
                "/etc/hive/conf/hive-site.xml"
               // "--addJars",,
               // "/home/liush/ilottery/hivelib/hive-common-1.1.0-cdh5.8.2.jar,/home/liush/ilottery/hivelib/hive-exec-1.1.0-cdh5.8.2.jar,/home/liush/ilottery/hivelib/hive-metastore-1.1.0-cdh5.8.2.jar"
               // "/home/liush/ilottery/ilottery1.1.jar"
                //"org.apache.spark.examples.SparkPi",
                //"--arg",
                //"10"
        };
        System.out.println("===="+getSparkHome());
        String sparkHome = System.getenv("SPARK_HOME");
        System.out.println("==="+sparkHome);
        executorJob(getSparkHome(),argsb); //
        long elapsedTime = System.currentTimeMillis() - startTime;
        logger.info("elapsedTime (millis)=" + elapsedTime);

    }

    public static String getSparkHome() {
        String sparkHome = System.getenv("SPARK_HOME");
        logger.info("SPARK_HOME:" + sparkHome);
        if (StringUtils.isEmpty(sparkHome)) {
            sparkHome=System.getenv("SPARKHOME");
           logger.info("SPARK_HOME was set to " + sparkHome);
            return sparkHome;
        }
        return  sparkHome+ File.separator;
    }
    static void executorJob(String SPARK_HOME,String[] args) throws Exception {
        //ConfigurationManager.setHadoopConfDir(ThreadLocal.getClass().get);
        //set hadoop config and HadoopDir
        //String ==null?.toURI().getPath();
       URL path=Thread.currentThread().getContextClassLoader().getResource("hive-site.xml");
        logger.info("hadoop was set to " + path);
        if (path!=null) {
            logger.info("hadoop was set to " + path);
            String hadoopConfig=new File(path.toURI().getPath()).getParent();
            ConfigurationManager.setHadoopConfDir(hadoopConfig);
            ConfigurationManager.setHadoopHomeDir(hadoopConfig);
            ConfigurationManager.setHiveConfDir(hadoopConfig);
        }
        Configuration hadoopConf =ConfigurationManager.createConfiguration();
        //error
        //hadoopConf.set("user.name","hive");
        Iterator hadoopConfIt=hadoopConf.iterator();
        while(hadoopConfIt.hasNext()){
            Map.Entry strMap=(Map.Entry)hadoopConfIt.next();
            System.out.println("==key= " + strMap.getKey() + " ==value= " + strMap.getValue());
        }


       // Configuration hadoopConf=new Configuration();
        System.setProperty("SPARK_YARN_MODE", "true");
        //System.setProperty("user.name", "hive");
        SparkConf sparkConf = new SparkConf();
        //sparkConf.set("spark.user.name","hive");
        //sparkConf.set("spark.user.name","hive");
        //String hadoopConfig=new File(path.toURI().getPath()).getParent();
        //sparkConf.set("files",hadoopConfig+"/hive-site.xml");
       // THE_LOGGER.info("HADOOP_CONF_HIVE_SITE=" + new File(HADOOP_CONF_HIVE_SITE).getAbsoluteFile().toURI().toURL());
        //OK
        //sparkConf.set("spark.hadoop.user.name","hive");

        //sparkConf.setSparkHome(SPARK_HOME);
        sparkConf.setMaster("yarn");
        sparkConf.setAppName("spark-yarn");
        sparkConf.set("master", "yarn");
                //sparkConf.set()

        sparkConf.set("spark.submit.deployMode", "cluster"); // worked
        ClientArguments clientArguments = new ClientArguments(args, sparkConf);    // spark-1.6.1
       // ClientArguments clientArguments = new ClientArguments(args);                 // spark-2.0.0
        Client client = new Client(clientArguments, hadoopConf, sparkConf);
       // ApplicationId appId= client.submitApplication();
        client.run();
      //  getMonitorApplication(client,appId);
    }


    public  static void getMonitorApplication(final Client client, final ApplicationId appId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApplicationReport report  =null;
                    report= client.getApplicationReport(appId);
                    String user = report.getUser();
                    String host = report.getHost();
                    Long startTime = report.getStartTime();
                    Long finishTime = report.getFinishTime();
                    float progress = report.getProgress();
                    String appType = report.getApplicationType();
                    YarnApplicationState state = report.getYarnApplicationState();
                    boolean finsh=(state == YarnApplicationState.FINISHED ||
                            state == YarnApplicationState.FAILED ||
                            state == YarnApplicationState.KILLED);
                    while (!finsh) {
                        Thread.sleep(5000);
                        report= client.getApplicationReport(appId);
                        state = report.getYarnApplicationState();
                        //report.getProgress();
                       logger.info("==="+report.getProgress()+"===="+state);
                        if (state == YarnApplicationState.FINISHED ||
                                state == YarnApplicationState.FAILED ||
                                state == YarnApplicationState.KILLED) {
                            logger.info("==FINISHED===="+state);
                            return;
                        }

                        if (state == YarnApplicationState.RUNNING) {
                            //  return (state, report.getFinalApplicationStatus,report)
                           logger.info("==="+report.getProgress()+"===="+state);
                           // return;
                        }
                    }
                }  catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
