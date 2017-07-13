package com.ilotterytech.sqoop.util;

/**
 * Created by liush on 17-6-5.
 */
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import java.io.IOException;
import org.apache.hadoop.fs.FileSystem;
import java.net.URI;
public class HDFSOperatingFiles {

    private static String HDFS_URI ="hdfs://name-node1:8020/";
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();

        HDFSOperatingFiles ofs = new HDFSOperatingFiles();
        System.out.println("\n=======create dir=======");
        String dir = HDFS_URI+"/user/sqoop2/test";
        ofs.createDir(dir);
        ofs.deleteDir(dir);
    }

    public static boolean deleteDir(String dir) throws IOException {

        if (dir==null ||dir.isEmpty()|| "".equals(dir)) {
            return false;
        }
        dir = HDFS_URI + dir;
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        fs.delete(new Path(dir), true);
        fs.close();
        return true;
    }


    public void createDir(String dir) throws IOException {
        Configuration conf = new Configuration();
        dir = HDFS_URI + dir;
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        if (!fs.exists(new Path(dir))) {
            fs.mkdirs(new Path(dir));
        }
        fs.close();
    }
}
