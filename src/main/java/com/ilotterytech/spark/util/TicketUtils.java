package com.ilotterytech.spark.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 跟投注票解析相关的工具类
 * Created by Zhang on 2017/7/7.
 */
public final class TicketUtils {

    /**
     * 将投注信息按照位数信息，拆分成两部分
     * @param exp
     * @return
     */
    public static String[] separateNumber(String exp, int len){
        if (exp == null)
            return null;

        return new String[]{StringUtils.mid(exp, 0, len), StringUtils.substring(exp, len)};
    }

    /**
     * 按照默认的前四位代表玩法和倍投数量拆分投注信息
     * @param exp
     * @return
     */
    public static String[] separateNumber(String exp){
        return separateNumber(exp, 4);
    }

    /**
     * 根据制定的字符，将字符串拆分成前后两部分，特定的字符串将不包含在内，主要用于拆分胆拖之类的投注信息，中间有*号
     * chars字符串，只能在拆分字符串出现一次
     * @param exp
     * @param chars
     * @return
     */
    public static String[] separateNumber(String exp, String chars){
        if (exp == null)
            return null;

        int pos = exp.indexOf(chars);
        if (pos < 0)
            return new String[]{exp, null};

        String s = StringUtils.remove(exp, chars);
        return separateNumber(s, pos);
    }

    /**
     * 根据传入的玩法类型（前四位），获取投注倍数
     * 例如0001，则返回1
     * @param exp
     * @return
     */
    public static Integer decodeNumWith4Char(String exp){
        if (exp == null || exp.length() < 4)
            throw new IllegalArgumentException("exp must be not null or must less than 4 chars");

        return NumberUtils.toInt(StringUtils.substring(exp, 2, 4));
    }

    /**
     * 根据传入的玩法类型（前六位），获取投注倍数
     * 例如000001，则返回1
     * @param exp
     * @return
     */
    public static Integer decodeNumWith6Char(String exp){
        if (exp == null || exp.length() < 6)
            throw new IllegalArgumentException("exp must be not null or must less than 4 chars");

        return NumberUtils.toInt(StringUtils.substring(exp, 3, 6));
    }

    /**
     * 将投注的号码（每个号码都是两位），切分成字符串数组
     * @param exp
     * @return
     */
    public static String[] splitNumber(String exp){
        if (exp == null || (exp.length() % 2 != 0))
            throw new IllegalArgumentException("exp must be not null or number length is even");

        int size = exp.length() / 2;
        String[] ret = new String[size];
        for (int i = 0; i < size; i++){
            ret[i] = StringUtils.mid(exp, i * 2, 2);
        }

        return ret;
    }

    /**
     * 获取拆分投注号码后的数量，投注号码每两个数字为一个投注号
     * @param exp
     * @return
     */
    public static int splitNumberCount(String exp){
        String[] s = splitNumber(exp);
        return s.length;
    }

    /**
     * 将投注号码进行拆分，同时重新按照中间空格的方式合并起来
     * @param exp
     * @return
     */
    public static String splitNumberAndRejoin(String exp){
        if (exp == null)
            return null;

        String[] s = splitNumber(exp);
        return joinNumber(s);
    }

    /**
     * 将投注号码拼接成单一字符串
     * @param num
     * @return
     */
    public static String joinNumber(String[] num, String c){
        if (num == null)
            throw new IllegalArgumentException("num must be not null");

        return StringUtils.join(num, c);
    }

    /**
     * 将投注号码拼接成单一字符串，分隔符为一个空格
     * @param num
     * @return
     */
    public static String joinNumber(String[] num){
        return joinNumber(num, " ");
    }

    /**
     * 获取投注信息的玩法信息，一般都是前2个字符为玩法信息，也可指定长度
     * @param exp
     * @param len
     * @return
     */
    public static String getCategoryPrefix(String exp, int len){
        if (exp == null)
            return null;

        return StringUtils.substring(exp, 0, len);
    }

    /**
     * 获取投注信息的玩法信息，一般都是前2个字符为玩法信息，也可指定长度
     * @param exp
     * @return
     */
    public static String getCategoryPrefix(String exp){
        return getCategoryPrefix(exp, 2);
    }

    /**
     * 根据传入的flag5只，判断购买模式
     * 自选：0，机选：1
     * @param flag
     * @return
     */
    public static int getSelectMode(int flag){
        int ret = 1;
        if (flag == 0 || flag == 1)
            ret = 0;

        return ret;
    }

    /**
     * 排列组合算法
     * C(n, m) = C(n, n-m) = n! / ((n - m)! * m!) = (n - m + 1) ** n/m!
     * @param n
     * @param m
     * @return
     */
    public static int combine(int n, int m){
        if (n - m < 0)
            throw new IllegalArgumentException("n must large than m");

        //判断m和n-m哪个大，用小的来计算，可以避免溢出问题
        int mm = m < (n-m) ? m : n - m;
        return (int)(factorial((n - mm + 1), n) / factorial(mm));
    }

    /**
     * 计算a阶乘
     * @param a
     * @return
     */
    public static long factorial(int a){
        return factorial(1, a);
    }

    /**
     * 计算变形后的阶乘n * n+1 * ... m
     * @param n
     * @param m
     * @return
     */
    private static long factorial(int n, int m){
        long sum = 1;
        for(int i = n; i <= m; i++){
            sum *= i;
        }

        return sum;
    }
}
