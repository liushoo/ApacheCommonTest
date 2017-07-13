package com.ilotterytech.hadoop.d2Tod3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by SJ on 2017-5-30.
 */
public class MyReducer extends Reducer<KeyClass, Text, Text, Text> {

    private String DELIMETER = "\t";
//    KeyClass keyClass = ;

    private String LINE="--------------------------------------------";
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        this.DELIMETER = context.getConfiguration().get("DELIMETER", "\t");
    }

    @Override
    protected void reduce(KeyClass key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        String dateFK = null;
        String timeFK = null;
        System.out.println(LINE);

        boolean exits = false;
        for(Text value: values) {
            if(exits==false) {
                System.out.println("00000000----"+ value);

                String[] splits = value.toString().substring(2).split(this.DELIMETER, -1);
                if(dateFK == null) {
                    dateFK = splits[4];
                }
                if(timeFK == null) {
                    timeFK = splits[5];
                }
                exits = true;
            } else {
                System.out.println("11111111---" + value);
                String splits[] = value.toString().substring(2).split(this.DELIMETER, -1);
                StringBuffer buf = new StringBuffer();
                //consumeId,tickitId,dateFK,timeFK,costPerConsume, totalCostConsume, times, Frequency
//                System.out.println(dateFK + ", " + timeFK);

                buf.append(key.getKeyString()).append(this.DELIMETER).append(dateFK)
                        .append(this.DELIMETER).append(timeFK).append(this.DELIMETER).append(Double.parseDouble(
                        splits[4])/Double.parseDouble(splits[3])).append(this.DELIMETER).append(splits[4])
                        .append(this.DELIMETER).append(splits[3]).append(this.DELIMETER).append("1");
                context.write(new Text(splits[0]), new Text(buf.toString()));
            }
        }
    }

    public MyReducer() {
        super();
    }

}
