package com.ilotterytech.hadoop.d2Tod3;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by SJ on 2017-6-4.
 */
public class KeyClass implements WritableComparable<KeyClass> {
    private String tag = null;
    private String keyString = null;

    @Override
    public String toString() {
        return keyString;
    }

    @Override
    public int hashCode() {
        return keyString.hashCode();
    }

    public KeyClass() {
        super();
    }

    public KeyClass(String tag, String keyString) {
        super();
        this.tag = tag;
        this.keyString = keyString;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getKeyString() {
        return keyString;
    }

    public void setKeyString(String keyString) {
        this.keyString = keyString;
    }

    public int compareTo(KeyClass o) {
        if (!this.keyString.equals(o.getKeyString())) {
            return this.getKeyString().compareTo(o.getKeyString()); // 字符串的compareTo()方法
        } else {
            if (!this.getTag().equals(o.getTag())) {
                return this.getTag().compareTo(o.getTag());
            } else {
                return 0;
            }
        }
    }

    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(tag);
        dataOutput.writeUTF(keyString);
    }

    public void readFields(DataInput dataInput) throws IOException {
        this.tag = dataInput.readUTF();
        this.keyString = dataInput.readUTF();
    }
}
