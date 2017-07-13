package com.ilotterytech.spark.yarnClient;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * Created by liush on 17-6-21.
 */
public class ConfigurationManager {

    private static final Logger THE_LOGGER = Logger.getLogger(ConfigurationManager.class);
    //default location
    static String HADOOP_HOME = "/opt/cloudera/parcels/CDH/lib/hadoop/";

    static String HADOOP_CONF_DIR = "/etc/hadoop/conf/";
    static String HIVE_CONF_DIR = "/etc/hive/conf/";
    // identify the cluster: note that we may define many clusters
    // and submit it to different clusters based on parameters and conditions
    // URL url = Thread.currentThread().getContextClassLoader().getResource("core-site.xml");
    public static void setHadoopHomeDir(String dir) {
        HADOOP_HOME = dir;
    }

    public static String getHadoopHomeDir() {
        return HADOOP_HOME;
    }

    public static void setHiveConfDir(String dir) {
        HIVE_CONF_DIR = dir;
    }

    public static String getHiveConfDir() {
        return HIVE_CONF_DIR;
    }

    //
    public static void setHadoopConfDir(String dir) {
        HADOOP_CONF_DIR = dir;

    }

    public static String getHadoopConfDir() {
        return HADOOP_CONF_DIR;
    }

    static Configuration createConfiguration() throws IOException {
         final String HADOOP_CONF_CORE_SITE = ConfigurationManager.getHadoopConfDir() + "/core-site.xml";
         final String HADOOP_CONF_HDFS_SITE = ConfigurationManager.getHadoopConfDir() + "/hdfs-site.xml";
         final String HADOOP_CONF_MAPRED_SITE = ConfigurationManager.getHadoopConfDir() + "/mapred-site.xml";
         final String HADOOP_CONF_YARN_SITE = ConfigurationManager.getHadoopConfDir() + "/yarn-site.xml";
          //HIVE /opt/cloudera/parcels/CDH/lib/hive/conf
        ///etc/hive/conf/hive-site.xml
        final String HADOOP_CONF_HIVE_SITE = ConfigurationManager.getHiveConfDir() + "/hive-site.xml";
         //
        THE_LOGGER.info("createConfiguration() started.");
        THE_LOGGER.info("createConfiguration() HADOOP_HOME=" + ConfigurationManager.getHadoopHomeDir());
        THE_LOGGER.info("createConfiguration() HADOOP_CONF_DIR=" + ConfigurationManager.getHadoopConfDir());
        //这客户端连接
        THE_LOGGER.info("HADOOP_CONF_CORE_SITE=" + new File(HADOOP_CONF_CORE_SITE).getAbsoluteFile().toURI().toURL());
        THE_LOGGER.info("HADOOP_CONF_HDFS_SITE=" + new File(HADOOP_CONF_HDFS_SITE).getAbsoluteFile().toURI().toURL());
        THE_LOGGER.info("HADOOP_CONF_MAPRED_SITE=" + new File(HADOOP_CONF_MAPRED_SITE).getAbsoluteFile().toURI().toURL());
       //
        THE_LOGGER.info("HADOOP_CONF_YARN_SITE=" + new File(HADOOP_CONF_YARN_SITE).getAbsoluteFile().toURI().toURL());
        THE_LOGGER.info("HADOOP_CONF_HIVE_SITE=" + new File(HADOOP_CONF_HIVE_SITE).getAbsoluteFile().toURI().toURL());
        Configuration config = new Configuration();
        //toURI此方法不会自动转义 URL 中的非法字符,
        //将抽象路径名转换为 URL：首先通过 toURI 方法将其转换为 URI，然后通过 URI.toURL 方法将 URI 装换为 URL
        config.addResource(new File(HADOOP_CONF_CORE_SITE).getAbsoluteFile().toURI().toURL());   // WORKED
        config.addResource(new File(HADOOP_CONF_HDFS_SITE).getAbsoluteFile().toURI().toURL());   // WORKED
        config.addResource(new File(HADOOP_CONF_MAPRED_SITE).getAbsoluteFile().toURI().toURL()); // WORKED
        config.addResource(new File(HADOOP_CONF_YARN_SITE).getAbsoluteFile().toURI().toURL());   // WORKED
        config.addResource(new File(HADOOP_CONF_HIVE_SITE).getAbsoluteFile().toURI().toURL());   // WORKED
       // fillProperties(config, getPropXmlAsMap(HADOOP_CONF_HIVE_SITE));
       // fillProperties(config, getPropXmlAsMap("config/yarn-site.xml"))
        //
        //config.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()); // WORKED
        //config.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());         // WORKED
        config.set("hadoop.home.dir", ConfigurationManager.getHadoopHomeDir());
        config.set("hadoop.conf.dir", ConfigurationManager.getHadoopConfDir());
        config.set("yarn.conf.dir", ConfigurationManager.getHadoopConfDir());
        config.set("hive.conf.dir", ConfigurationManager.getHadoopConfDir());

        //
        config.reloadConfiguration();
        //
        THE_LOGGER.info("createConfiguration(): Configuration created.");

        return config;
    }
    /**
     * 获取Client ,用于获取Spark任务的状态
     * @return
     */
    private static YarnClient client = null;

    public static YarnClient getClient() {
        if(client == null){
            client = YarnClient.createYarnClient();
            try {
                client.init(createConfiguration());
            } catch (IOException e) {
                e.printStackTrace();
            }
            client.start();
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


    public static Map getPropXmlAsMap(String path){
        File file = new File(path);
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(file);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();
        List props = root.getChildren("property");
        Map result = new HashMap();
        for(Iterator iter = props.iterator(); iter.hasNext();){
            Element element = (Element) iter.next();
            Element nameele = element.getChild("name");
            Element valueele = element.getChild("value");
            String name = nameele.getText();
            String value = valueele.getText();
            result.put(name, value);
        }
        return result;

    }
    public static void fillProperties(Configuration conf, Map map){
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            conf.set(key, value);
        }
    }
}
