package com.ilotterytech.spark.submitShell;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by liush on 17-6-20.
 */
public class SparkExecutable {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SparkExecutable.class);
    private static final String CLASS_NAME = "className";
    private static final String JARS = "jars";
    private Map<String, String> params = Maps.newHashMap();

    public final String getParam(String key) {
        return this.params.get(key);
    }

    public final Map<String, String> getParams() {
        return this.params;
    }


    protected void doWork()throws Exception {
        //hbase-site.xml
        String hbaseConf = ClassLoader.getSystemClassLoader().getResource("hbase-site.xml").getFile().toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("export HADOOP_CONF_DIR=%s && %s/bin/spark-submit --class org.apache.kylin.common.util.SparkEntry ");


        try {
            String cmd = String.format(stringBuilder.toString());
            logger.info("cmd: " + cmd);

            PatternedLogger patternedLogger = new PatternedLogger(logger);
            execute(cmd, patternedLogger);
           System.out.println( patternedLogger.getInfo());
          } catch (Exception e) {
            logger.error("error run spark job:", e);
            throw  new Exception(e.getMessage());
        }
    }

    public static String getSparkHome() {
        String sparkHome = System.getenv("SPARK_HOME")==null?System.getenv("SPARKHOME"):null;
        if (StringUtils.isNotEmpty(sparkHome)) {
            logger.info("SPARK_HOME was set to " + sparkHome);
            return sparkHome;
        }
        return  sparkHome+File.separator;
    }
    public Map<Integer, String> execute(String command) throws IOException {
        return execute(command, new SoutLogger());
    }

    public Map<Integer, String> execute(String command, Logger logAppender) throws IOException {

        Map<Integer, String> r= runNativeCommand(command, logAppender);
        //System.out.println("=========="+r.get(0) );

        //0 Success 1 error
        if (r.get(0)==null) {
            throw new IOException("OS command error exit with " );
        }

        return r;
    }
    private Map<Integer, String> runNativeCommand(String command, Logger logAppender) throws IOException {
        String[] cmd = new String[3];
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            cmd[0] = "cmd.exe";
            cmd[1] = "/C";
        } else {
            cmd[0] = "/bin/bash";
            cmd[1] = "-c";
        }
        cmd[2] = command;

        ProcessBuilder builder = new ProcessBuilder(cmd);
        builder.redirectErrorStream(true);
        Process proc = builder.start();
        Map map=new HashMap<Integer, String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            result.append(line).append('\n');
            if (logAppender != null) {
                logAppender.log(line);
            }
        }

        try {
            //0 Success 1 error
            int exitCode = proc.waitFor();
            map.put(exitCode, result.toString());
            return map;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }
}




