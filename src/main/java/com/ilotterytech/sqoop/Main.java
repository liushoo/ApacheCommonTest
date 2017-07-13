package com.ilotterytech.sqoop;


import org.apache.sqoop.Sqoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liush on 2017/06/06.
 */
public class Main {


    public static void main(String[] args){
        Map<String, String> param=new HashMap<String, String>();
        String jdbcConnect=args[0];
        //param.put("jdbcConnect",jdbcConnect);
        String jdbcUsername=args[1];
        //param.put("jdbcUsername",jdbcUsername);
        String jdbcPassword=args[2];
        //param.put("jdbcPassword",jdbcPassword);
        String jdbcTable=args[3];
        //param.put("jdbcTable",jdbcTable);
        String hiveDatabase=args[4];
        //param.put("hiveDatabase",hiveDatabase);
        String hiveOverwrite=args[5];
        //param.put("hiveOverwrite",hiveOverwrite);
        String deleteTargetDir=args[6];
        //param.put("deleteTargetDir",deleteTargetDir);
        String hiveM=args[7];
        //param.put("hiveM",hiveM);
        String splitBy=args[8];
        //param.put("splitBy",splitBy);
        ArrayList<String> list = new ArrayList<String>();
        list.add("import");
        list.add("--m");
        list.add(hiveM);// 定义mapreduce的数量
        list.add("--split-by");
        list.add(splitBy);// 定义mapreduce的数量
        list.add("--connect");
        list.add(jdbcConnect);
        list.add("--username");
        list.add(jdbcUsername); // 数据库的用户名
        list.add("--password");
        list.add(jdbcPassword); // 数据库的密码
        list.add("--table");
        list.add(jdbcTable); // 数据库中的表。
        list.add("--hive-import");
        list.add("--hive-database");
        list.add(hiveDatabase);
        if(!"".equals(hiveOverwrite)&& "true".equals(hiveOverwrite)){
            list.add("--hive-overwrite");
        }
        if(!"".equals(hiveOverwrite)&& "true".equals(deleteTargetDir)){
            list.add("--delete-target-dir");
        }

        String[] sqoopArguments = new String[1];

        sqoopArguments = list.toArray(new String[0]);
        for (String o : list) {
            System.out.println(o);
        }

        final int ret = Sqoop.runTool(sqoopArguments);
        //Import.importHiveFromMysql(param);
        if (ret != 0) {
            throw new RuntimeException("Sqoop failed - return code "
                    + Integer.toString(ret));
        }
    }

}
