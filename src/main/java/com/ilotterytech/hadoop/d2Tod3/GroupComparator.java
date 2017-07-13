package com.ilotterytech.hadoop.d2Tod3;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by SJ on 2017-6-4.
 */
/*
 * 实现分组
 */
public class GroupComparator extends WritableComparator {

    protected GroupComparator() {
        super(KeyClass.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        KeyClass a1 = (KeyClass) a;
        KeyClass b1 = (KeyClass) b;
        // 只需要比较a1,b1的first字段即认为他们是否属于同组
        return a1.getKeyString().compareTo(b1.getKeyString());
    }

}