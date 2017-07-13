package com.ilotterytech.hadoop.d2Tod3;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by SJ on 2017-5-30.
 */
public class MyMapper extends Mapper<LongWritable, Text, KeyClass, Text> {

    private String DELIMETER = "\t";

    private HashMap<String, String> dateMap = new HashMap<String, String>();
    private HashMap<String, String> timeMap = new HashMap<String, String>();

    @Override
    protected void setup(Context context) {

        this.DELIMETER = context.getConfiguration().get("DELIMETER", "\t");
        try {
            Path[] files = DistributedCache.getLocalCacheFiles(context.getConfiguration());
            System.out.println("getting local cacheFiles");
            System.out.println(files==null);
            if(files==null) {
                System.out.println("files are null");
            }

            if(files!=null && files.length!=0) {
                for(Path file: files) {
                    System.out.println("-------------"+ file.toString());
                    readCacheFile(file);
                }
            }
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }

    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        FileSplit split = (FileSplit) context.getInputSplit();
        String filePath = split.getPath().toString();
        System.out.println("path---" + filePath);

        KeyClass outKey = null;
        Text outValue = null;

        if(value.toString() == null || value.toString().trim().equals(""))
            return;

        if(filePath.contains("consume")) {
            String splits[] = value.toString().split(DELIMETER, -1);

            outKey = new KeyClass("1", splits[1]);
            outValue = new Text("1_" + splits[0]+"\t"+ splits[2] + "\t" + splits[3] +"\t" + splits[6] +"\t" +
                    splits[7]);
        } else if(filePath.contains("ticket")) {
            String splits[] = value.toString().split(DELIMETER, -1);
            String date = splits[5].split(" ", -1)[0];
            String dateFK = this.dateMap.get(date);

            String time = splits[5].split(" ", -1)[1].substring(0, 5);

            outKey = new KeyClass("0", splits[0]);
            outValue = new Text("0_"+splits[1] + "\t" + splits[2] + "\t" + splits[3] + "\t" + splits[4] +
                    "\t" + dateFK + "\t" + time + "\t" + splits[6] + "\t" + splits[9]);
        }
        context.write(outKey, outValue);
    }

    public MyMapper() {
        super();

    }

    private void readCacheFile (Path file) {

        try {

            System.out.println(file.toString());
            BufferedReader br = new BufferedReader(new FileReader(file.toString()));
            String line = br.readLine();

            while(line != null) {
                String splits[] = line.split(",", -1);
                if(splits.length<0) {
                    continue;
                }
                dateMap.put(splits[1], splits[0]);
                line = br.readLine();
            }
            br.close();

        } catch (FileNotFoundException fileEx) {
            fileEx.printStackTrace();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }
}
