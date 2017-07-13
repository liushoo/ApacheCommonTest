package com.ilotterytech.hadoop.d2Tod3;

/**
 * Created by liush on 17-6-11.
 */
public class MonitorMapRedurce {
 /*
    public  static void getMonitorMapRedurceInfo(Job job ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                  while (!job.getStatus().isJobComplete()) {
                        long startTime = job.getStartTime();
                        long finishTime = job.getFinishTime();
                        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date d = new Date();
                        d.setTime(startTime);
                        String s = format1.format(d);
                        d.setTime(finishTime);
                        String f = format1.format(d);
                        System.out.println(job.getStatus());
                        *//**
                         * 取出mapredurce
                         * 	Map-Reduce Framework
                                Map input records=3598358
                                Map output records=3598358
                                Map output bytes=158394135
                        Counters counters = job.getCounters();
                        long inputValue = counters.getGroup("org.apache.hadoop.mapreduce.TaskCounter").findCounter("MAP_INPUT_RECORDS").getValue();
                        long outputValue = counters.getGroup("org.apache.hadoop.mapreduce.TaskCounter").findCounter("MAP_OUTPUT_RECORDS").getValue();
                        System.out.println("inputValue:"+inputValue+"==outputValue=="+outputValue);*//*
                        System.out.println("startTime:" + s + "==finishTime:==" + f + "==jobStatus:==" + job.getStatus().getState().getValue() + "==mapProgress===" + job.mapProgress()
                                + "==reduceProgress:==" + job.reduceProgress() + "==setupProgress:==" + job.setupProgress());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }**/

    /**

     * 取出mapredurce
     * 	Map-Reduce Framework
     *    Map input records=3598358
     *    Map output records=3598358
     *    Map output bytes=158394135
     * @param job
     */
    /**
    public  static void getMonitorMapRedurceCountersInfo(Job job ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    if(job.getStatus().isJobComplete()){
                        long startTime = job.getStartTime();
                        long finishTime = job.getFinishTime();
                        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date d = new Date();
                        d.setTime(startTime);
                        String s = format1.format(d);
                        d.setTime(finishTime);
                        String f = format1.format(d);
                        System.out.println(job.getStatus());

                          取出mapredurce
                                Map-Reduce Framework
                                 Map input records=3598358
                                 Map output records=3598358
                                 Map output bytes=158394135

                        Counters counters = job.getCounters();
                        long inputValue = counters.getGroup("org.apache.hadoop.mapreduce.TaskCounter").findCounter("MAP_INPUT_RECORDS").getValue();
                        long outputValue = counters.getGroup("org.apache.hadoop.mapreduce.TaskCounter").findCounter("MAP_OUTPUT_RECORDS").getValue();
                        System.out.println("inputValue:"+inputValue+"==outputValue=="+outputValue);
                       System.out.println("startTime:" + s + "==finishTime:==" + f + "==jobStatus:==" + job.getStatus().getState().getValue() + "==mapProgress===" + job.mapProgress()
                                + "==reduceProgress:==" + job.reduceProgress() + "==setupProgress:==" + job.setupProgress());
                    System.out.println("===============");
                    Iterator iterator = counters.iterator();
                    for (Iterator iter = counters.iterator(); iter.hasNext(); ) {
                        CounterGroup counter = (CounterGroup) iter.next();
                        System.out.println("name:" + counter.getName() + "\t displayName:" + counter.getDisplayName());
                    }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }**/

}
