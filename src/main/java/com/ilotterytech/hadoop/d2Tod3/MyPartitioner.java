package com.ilotterytech.hadoop.d2Tod3;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by SJ on 2017-6-4.
 */
public class MyPartitioner extends Partitioner<KeyClass, Text> {

    public MyPartitioner() {

    }

    @Override
    public int getPartition(KeyClass keyClass, Text text, int i) {
        return Math.abs(keyClass.getKeyString().hashCode()) % i;
    }

}
