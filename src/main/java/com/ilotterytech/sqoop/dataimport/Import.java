package com.ilotterytech.sqoop.dataimport;



import org.apache.sqoop.Sqoop;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by liush on 2017/06/06.
 */
public class Import {
    /*public static Properties properties=null;

    static {
         properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hosts.properties"));
        } catch (Exception var2) {
            System.out.println(var2.getMessage());
            var2.getStackTrace();
        }


    }*/



    public static int importHiveFromMysql(Map<String, String> param){

        /*String dbConnect = properties.getProperty("jdbc.connect");
        String dbUsername = properties.getProperty("jdbc.username");
        String dbPassword = properties.getProperty("jdbc.password");
        String dbTable = properties.getProperty("jdbc.table");
        String hiveDatabase = properties.getProperty("hive.database");

        String hiveOverwrite = properties.getProperty("hive.overwrite");
        String hiveDeleteDir = properties.getProperty("hive.delete-target-dir");
        String m = properties.getProperty("hive.m");
        String splitBy = properties.getProperty("hive.split-by");*/

        String dbConnect = param.get("jdbcConnect");
        String dbUsername = param.get("jdbcUsername");
        String dbPassword = param.get("jdbcPassword");
        String dbTable = param.get("jdbcTable");
        String hiveDatabase = param.get("hiveDatabase");
        String hiveOverwrite = param.get("hiveOverwrite");
        String hiveDeleteDir = param.get("deleteTargetDir");
        String m = param.get("hiveM");
        String splitBy = param.get("splitBy");

        long start=System.currentTimeMillis();
        int num=0;
        ArrayList<String> list = new ArrayList<String>();

        list.add("import");
        list.add("--m");
        list.add(m);// 定义mapreduce的数量
        list.add("--split-by");
        list.add(splitBy);// 定义mapreduce的数量
        list.add("--connect");
        list.add(dbConnect);
        list.add("--username");
        list.add(dbUsername); // 数据库的用户名
        list.add("--password");
        list.add(dbPassword); // 数据库的密码
        list.add("--table");
        list.add(dbTable); // 数据库中的表。
        list.add("--hive-import");
        list.add("--hive-database");
        list.add(hiveDatabase);


        if(!"".equals(hiveOverwrite)&& "true".equals(hiveOverwrite)){
            list.add("--hive-overwrite");
        }
        if(!"".equals(hiveOverwrite)&& "true".equals(hiveDeleteDir)){
            list.add("--delete-target-dir");
        }

        String[] arg = new String[1];

        arg = list.toArray(new String[0]);
        for (String o : list) {
            System.out.println(o);
        }

      //  System.out.println(arg.toString());

        num=Sqoop.runTool(arg);
        System.out.println("===="+num);
        return num;

    }

}
