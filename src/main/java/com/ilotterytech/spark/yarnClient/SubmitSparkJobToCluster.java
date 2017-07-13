package com.ilotterytech.spark.yarnClient;

import org.apache.log4j.Logger;
import org.apache.spark.launcher.SparkLauncher;



public class SubmitSparkJobToCluster {

    static final Logger THE_LOGGER = Logger.getLogger(SubmitSparkJobToCluster.class);

    public static void main(String[] arguments) throws Exception {
        long startTime = System.currentTimeMillis();
        test(arguments); // ... the code being measured ...    
        long estimatedTime = System.currentTimeMillis() - startTime;
        THE_LOGGER.info("estimatedTime (millis)=" + estimatedTime);
    }

    static void test(String[] arguments) throws Exception {
        //
        final String javaHome = "/software/Java/jdk1.8.0_72.jdk/";
        final String sparkHome = "/software/spark2.1/";
        final String appResource = "/software/spark2.1/examples/jars/spark-examples_2.11-2.1.1.jar";
        final String mainClass = "org.apache.spark.examples.SparkPi";
        //
        // parameters passed to the  SparkFriendRecommendation
        final String[] appArgs = new String[]{
            //"--arg",,
            "3"
            
            //"--arg",
           // "/friends/input",
            
            //"--arg",
           // "/friends/output"
        };
        //
        //
        SparkLauncher spark = new SparkLauncher()
                .setVerbose(true)
               // .setJavaHome(javaHome)
                .setSparkHome(sparkHome)
                .setAppResource(appResource)    // "/my/app.jar"
                .setMainClass(mainClass)        // "my.spark.app.Main"
                .setMaster("local")
                .setConf(SparkLauncher.DRIVER_MEMORY, "1g")
                .addAppArgs(appArgs);
        //
        // Launches a sub-process that will start the configured Spark application.
       Process proc = spark.launch();
        //
        InputStreamReaderRunnable inputStreamReaderRunnable = new InputStreamReaderRunnable(proc.getInputStream(), "input");
        Thread inputThread = new Thread(inputStreamReaderRunnable, "LogStreamReader input");
        inputThread.start();
        //
        InputStreamReaderRunnable errorStreamReaderRunnable = new InputStreamReaderRunnable(proc.getErrorStream(), "error");
        Thread errorThread = new Thread(errorStreamReaderRunnable, "LogStreamReader error");
        errorThread.start();
        //
        THE_LOGGER.info("Waiting for finish...");
        int exitCode = proc.waitFor();
        THE_LOGGER.info("Finished! Exit code:" + exitCode);
    }
}
