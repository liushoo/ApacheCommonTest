package commonsLang;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liush on 17-7-13.
 */
public class DateUtilsTest {
    public static void main(String[] args) throws ParseException {
        System.out.println(getMonthFirstDay());
        System.out.println(getMonthLastDay());
        System.out.println(getPreviousMonthFirst());
        System.out.println(getPreviousMonthEnd());
        System.out.println(getNextMonthFirst());
        System.out.println(getNextMonthEnd());
        System.out.println(getCurrentMonthDays());
        System.out.println(getSpecifiedMonthDays("1900-02"));
        System.out.println(getCurrentDate());
        System.out.println(getOffsetDate(-4));
        System.out.println(isLeapYear(1900));
        System.out.println(getWeekDay(Calendar.getInstance()));
        System.out.println(getDaysListBetweenDates("2012-1-12", "2012-1-21"));
        System.out.println(getMonthsListBetweenDates("2012-1-12", "2012-3-21"));
        System.out.println(getSpecifiedOffsetTime("2012-09-09 12:12:12", 12));
        System.out.println(getOffsetDateTime("2012-09-09", 12));
        System.out.println(getOffsetDateTime("2012-09-09 12:12:12", 12));
        System.out.println(long2Time("1234567890"));
        Date date1 = new Date();
        Date date2 = new Date();
        /**
         * return  boolean
         * 日期是否相等
         */
        say(DateUtils.isSameDay(date1, date2));
        /**
         * 毫秒数是否相等
         * return  boolean
         */
        say(DateUtils.isSameInstant(date1, date2));
        /**
         * 根据第二个参数patter 与第一个参数：字符串时间去匹配，转换为Date类行---Thu Jan 29 00:00:00 CST 2015
         */
        sayDate(DateUtils.parseDateStrictly("2015/1/29", "yyyy/MM/dd"));
        /**
         * 根据第二个参数patter 与第一个参数：字符串时间去匹配，转换为Date类行---Thu Jan 29 00:00:00 CST 2015
         * 上面那个更严格
         */
        sayDate(DateUtils.parseDate("2015/1/29", "yyyy/MM/dd"));
        /**
         * 当前时间+天数2015-02-02 10:23:06
         */
        sayDate(DateUtils.addDays(date1, 4));
        /**
         * 2016-01-29 10:24:01
         * 前时间+年数
         */
        sayDate(DateUtils.addYears(date1, 1));
        /**
         * 2015-03-29 10:24:57
         */
        sayDate(DateUtils.addMonths(date1, 2));
        /**
         * 前时间+周数
         */
        sayDate(DateUtils.addWeeks(date1, 1));
        /**
         * 2015-01-30 10:30:38
         * +天
         */
        sayDate(DateUtils.addDays(date1, 1));
        /**
         * 2015-01-29 11:31:23
         * +小时
         */
        sayDate(DateUtils.addHours(date1, 1));
        /**
         * 2015-01-29 10:31:56
         * 2015-01-29 10:51:56
         */
        sayDate(DateUtils.addMinutes(date1, 20));
        //addSeconds  加秒数-。-不测了 addMilliseconds
        /**
         * 不知道干嘛
         */
        sayDate(DateUtils.setYears(DateUtils.addHours(date1, 1), 2015));
        /**
         * Calendar
         */
        say(DateUtils.toCalendar(date1));
        /**
         * filed =Calender.YEAR,Calendar.SECOND,Calendar.MINUTE,Calendar.HOUR,Calendar.DAY_OF_MONTH,Calendar.MONTH.
         * 时期去整
         * 年就忽略后面的
         * 月就忽略次级的去整
         */
        sayDate(DateUtils.round(date1, Calendar.MINUTE));
        /**
         *
         */
        System.out.println("----truncate");
        sayDate(DateUtils
                .truncate(date1, Calendar.YEAR));
        sayDate(DateUtils.truncate(date1, Calendar.MONTH));
        sayDate(DateUtils.truncate(date1, Calendar.DAY_OF_MONTH));
        sayDate(DateUtils.truncate(date1, Calendar.HOUR_OF_DAY));
        //sayDate(DateUtils.truncate(date1, Calendar.HOUR));常量都是5与上面一样
        sayDate(DateUtils.truncate(date1, Calendar.MINUTE));
        sayDate(DateUtils.truncate(date1, Calendar.SECOND));
        /**
         * 取极限值
         * Calendar.DAY_OF_MONTH  天的最大值2015-01-30 00:00:00
         * 月的终点Calendar.MONTH
         */
        System.out.println("----ceiling");
        sayDate(DateUtils.ceiling(date1, Calendar.MONTH));
        String[] format={"yyyy-MM-dd"};
        System.out.println(DateUtils.parseDate("2009-10-20",format));

        int amount = 2;

        Date date = new Date();

        // System.out.printf("%tF %<tT", date);

        System.out.println(date);

        // 增加amount天

        System.out.println(DateUtils.addDays(date, amount));

        // 增加amount小时

        System.out.println(DateUtils.addHours(date, amount));

        // 增加amount毫秒

        System.out.println(DateUtils.addMilliseconds(date, amount));

        // 增加amount分钟

        System.out.println(DateUtils.addMinutes(date, amount));

        // 增加amount月

        System.out.println(DateUtils.addMonths(date, amount));

        // 增加amount秒

        System.out.println(DateUtils.addSeconds(date, amount));

        // 增加amount星期

        System.out.println(DateUtils.addWeeks(date, amount));

        // 增加amount年

        System.out.println(DateUtils.addYears(date, amount));

        // 比较两个日期对象是否相等，只比较year, month, day

        System.out.println(DateUtils.isSameDay(date, new Date()));

        // 比较两个日期对象是否完全相等，精确到毫秒

        System.out.println(DateUtils.isSameInstant(date, new Date()));
        Date now = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当前时间：" + sd.format(now));
        Date years = org.apache.commons.lang3.time.DateUtils.addYears(now, 2);
        System.out.println("两年以后"+sd.format(years));
        Date year = org.apache.commons.lang3.time.DateUtils.addYears(now, -2);
        System.out.println("两年以前"+sd.format(year));
        Date Months = org.apache.commons.lang3.time.DateUtils.addMonths(now, 2);
        System.out.println("两月以后"+sd.format(Months));
        Date datea = org.apache.commons.lang3.time.DateUtils.addDays(now, 10);
        System.out.println("十天以后的时间"+sd.format(datea));
        Date hours = org.apache.commons.lang3.time.DateUtils.addHours(now, 2);
        System.out.println("2小时以后的时间"+sd.format(hours));
        Date minutes = org.apache.commons.lang3.time.DateUtils.addMinutes(now, 10);
        System.out.println("10分钟以后的时间"+sd.format(minutes));
        Date seconds = org.apache.commons.lang3.time.DateUtils.addSeconds(now, 10);
        System.out.println("十秒之后"+sd.format(seconds));
    }

    public static void say(Object o ){

        System.out.println(o);
    }
    public static void sayDate(Date d ){

        System.out.println(DateFormatUtils.format(d, "yyyy-MM-dd HH:mm:ss"));
    }
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String DATE_FORMAT_CN = "yyyy年MM月dd日";
    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String TIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm:ss";
    private final static String MONTH_FORMAT = "yyyy-MM";
    private final static String DAY_FORMAT = "yyyyMMdd";

    /**
     * @Title:getMonthFirstDay
     * @Description: 得到当前月的第一天.
     * @return
     * @return String
     */
    public static String getMonthFirstDay() {
        Calendar cal = Calendar.getInstance();
        // 方法一,默认只设置到年和月份.
        // Calendar f = (Calendar) cal.clone();
        // f.clear();
        // f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        // f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        // f.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DATE));
        // return DateFormatUtils.format(f, DATE_FORMAT);

        // 方法二.
        cal.set(Calendar.DATE, 1);
        return DateFormatUtils.format(cal, DATE_FORMAT);

    }

    /**
     * @Title:getMonthLastDay
     * @Description: 得到当前月最后一天
     * @return
     * @return String
     */
    public static String getMonthLastDay() {
        Calendar cal = Calendar.getInstance();
        Calendar f = (Calendar) cal.clone();
        f.clear();
        // 方法一
        // f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        // f.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        // f.set(Calendar.MILLISECOND, -1);
        // return DateFormatUtils.format(f, DATE_FORMAT);

        // 方法二
        // f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        // f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        // f.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        // return DateFormatUtils.format(f, DATE_FORMAT);

        // 方法三(同一)
        cal.set(Calendar.DATE, 1);// 设为当前月的1号
        cal.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        cal.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        return DateFormatUtils.format(cal, DATE_FORMAT);
    }

    /**
     * @Title:getPreviousMonthFirst
     * @Description: 得到上个月的第一天
     * @return
     * @return String
     */
    public static String getPreviousMonthFirst() {
        Calendar cal = Calendar.getInstance();
        Calendar f = (Calendar) cal.clone();
        f.clear();
        // 方法一
        // f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        // f.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        // f.set(Calendar.DATE, 1);
        // return DateFormatUtils.format(f, DATE_FORMAT);

        // 方法二
        // f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        // f.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        // f.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DATE));
        // return DateFormatUtils.format(f, DATE_FORMAT);

        // 方法三(同一)
        cal.set(Calendar.DATE, 1);// 设为当前月的1号
        cal.add(Calendar.MONTH, -1);
        return DateFormatUtils.format(cal, DATE_FORMAT);
    }

    /**
     * @Title:getPreviousMonthEnd
     * @Description: 得到上个月最后一天
     * @return
     * @return String
     */
    public static String getPreviousMonthEnd() {
        Calendar cal = Calendar.getInstance();
        Calendar f = (Calendar) cal.clone();
        f.clear();
        // 方法一
        // f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        // f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        // f.set(Calendar.MILLISECOND, -1);
        // return DateFormatUtils.format(f, DATE_FORMAT);

        // 方法二
        // f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        // f.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        // f.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        // return DateFormatUtils.format(f, DATE_FORMAT);

        // 方法三(同一)
        cal.set(Calendar.DATE, 1);// 设为当前月的1号
        cal.add(Calendar.MONTH, 0);//
        cal.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        return DateFormatUtils.format(cal, DATE_FORMAT);
    }

    /**
     * @Title:getNextMonthFirst
     * @Description: 得到下个月的第一天
     * @return
     * @return String
     */
    public static String getNextMonthFirst() {
        Calendar cal = Calendar.getInstance();
        Calendar f = (Calendar) cal.clone();
        f.clear();
        // 方法一
        // f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        // f.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        // f.set(Calendar.DATE, 1);
        // or f.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DATE));
        // return DateFormatUtils.format(f, DATE_FORMAT);

        // 方法二
        cal.set(Calendar.DATE, 1);// 设为当前月的1号
        cal.add(Calendar.MONTH, +1);// 加一个月，变为下月的1号
        return DateFormatUtils.format(cal, DATE_FORMAT);
    }

    /**
     * @Title:getNextMonthEnd
     * @Description: 得到下个月最后一天。
     * @return
     * @return String
     */
    public static String getNextMonthEnd() {
        Calendar cal = Calendar.getInstance();
        // cal.set(Calendar.DATE, +1);
        // cal.add(Calendar.MONTH, +2);
        // cal.add(Calendar.DATE, -1);
        // return DateFormatUtils.format(cal, DATE_FORMAT);

        // 方法二
        cal.add(Calendar.MONTH, 1);// 加一个月
        cal.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        cal.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        return DateFormatUtils.format(cal, DATE_FORMAT);
    }

    /**
     * @Title:getCurrentMonthDays
     * @Description: 得到当前月的天数
     * @return
     * @return int
     */
    public static int getCurrentMonthDays() {
        Calendar cal = new GregorianCalendar();// Calendar.getInstance();
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    /**
     * @Title:getSpecifiedMonthDays
     * @Description: 得到指定的月份的天数
     * @param date
     * @return
     * @return int
     */
    public static int getSpecifiedMonthDays(String date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(DateUtils.parseDate(date, MONTH_FORMAT));
            int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            return days;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return 0;
    }

    /**
     * @Title:getCurrentDate
     * @Description: 得到当前日期
     * @return
     * @return String
     */
    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        String currentDate = DateFormatUtils.format(cal, DATE_FORMAT);
        return currentDate;
    }

    /**
     * @Title:getCurrentTime
     * @Description: 得到当前的时间
     * @return
     * @return String
     */
    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        String currentDate = DateFormatUtils.format(cal, TIME_FORMAT);
        return currentDate;
    }

    /**
     * @Title:getOffsetDate
     * @Description: 得到与当前日期偏移量为X的日期。
     * @param offset
     * @return
     * @return String
     */
    public static String getOffsetDate(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, offset);
        String currentDate = DateFormatUtils.format(cal, DATE_FORMAT);
        return currentDate;
    }

    /**
     * @Title:getSpecifiedOffsetDate
     * @Description: 得到与指定日期偏移量为X的日期。
     * @param:    specifiedDate指定的日期
     *            ,格式为YYYY-MM-DD
     * @param offset
     * @return 返回yyyy-MM-dd格式的字符串日期
     * @return String
     * @throws ParseException
     */
    public static String getSpecifiedOffsetDate(String specifiedDate, int offset) throws ParseException {
        Date date = DateUtils.parseDate(specifiedDate, DATE_FORMAT);
        Calendar cal = DateUtils.toCalendar(date);
        cal.add(Calendar.DAY_OF_MONTH, offset);
        String returnDate = DateFormatUtils.format(cal, DATE_FORMAT);
        return returnDate;
    }

    /**
     * @Title:getSpecifiedOffsetTime
     * @Description: 得到与指定日期时间偏移量为X的时间。
     * @param specifiedTime
     *            指定的时间,格式为yyyy-MM-dd HH:mm:ss
     * @param offset
     *            偏移天数
     * @return 返回yyyy-MM-dd HH:mm:ss格式的字符串时间
     * @throws ParseException
     * @return String
     */
    public static String getSpecifiedOffsetTime(String specifiedTime, int offset) throws ParseException {
        Date date = DateUtils.parseDate(specifiedTime, TIME_FORMAT);
        Calendar cal = DateUtils.toCalendar(date);
        cal.add(Calendar.DAY_OF_MONTH, offset);
        String returnDate = DateFormatUtils.format(cal, TIME_FORMAT);
        return returnDate;
    }

    /**
     * @Title:getOffsetDateTime
     * @Description: 得到与指定日期时间偏移量为X的时间。
     * @param specifiedDateTime
     *            指定的时间,格式为yyyy-MM-dd HH:mm:ss/yyyy-MM-dd
     * @param offset
     *            偏移天数
     * @return
     * @throws ParseException
     * @return String
     */
    public static String getOffsetDateTime(String specifiedDateTime, int offset) throws ParseException {
        String regexStr = "\\d{4}-\\d{2}-\\d{2}";
        if (specifiedDateTime.matches(regexStr)) {
            return getSpecifiedOffsetDate(specifiedDateTime, offset);
        } else {
            return getSpecifiedOffsetTime(specifiedDateTime, offset);
        }
    }

    /**
     * 判断是否为润年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * @Title:getWeekDay
     * @Description: 判断是星期几.
     * @param c
     * @return
     * @return String
     */
    public static String getWeekDay(Calendar c) {
        if (c == null) {
            return "星期一";
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            default:
                return "星期日";
        }
    }

    /**
     * @Title:getDaysListBetweenDates
     * @Description: 获得两个日期之间的连续日期.
     * @param begin
     *            开始日期 .
     * @param end
     *            结束日期 .
     * @return
     * @return List<String>
     */
    public static List<String> getDaysListBetweenDates(String begin, String end) {
        List<String> dateList = new ArrayList<String>();
        Date d1;
        Date d2;
        try {
            d1 = DateUtils.parseDate(begin, DATE_FORMAT);
            d2 = DateUtils.parseDate(end, DATE_FORMAT);
            if (d1.compareTo(d2) > 0) {
                return dateList;
            }
            do {
                dateList.add(DateFormatUtils.format(d1, DATE_FORMAT));
                d1 = DateUtils.addDays(d1, 1);
            } while (d1.compareTo(d2) <= 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateList;
    }

    /**
     * @Title:getMonthsListBetweenDates
     * @Description: 获得连续的月份
     * @param begin
     * @param end
     * @return
     * @return List<String>
     */
    public static List<String> getMonthsListBetweenDates(String begin, String end) {
        List<String> dateList = new ArrayList<String>();
        Date d1;
        Date d2;
        try {
            d1 = DateUtils.parseDate(begin, DATE_FORMAT);
            d2 = DateUtils.parseDate(end, DATE_FORMAT);
            if (d1.compareTo(d2) > 0) {
                return dateList;
            }
            do {
                dateList.add(DateFormatUtils.format(d1, MONTH_FORMAT));
                d1 = DateUtils.addMonths(d1, 1);
            } while (d1.compareTo(d2) <= 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateList;
    }

    /**
     * @Title:long2Time
     * @Description: 将long类型的时间值转换成标准格式的时间（yyyy-MM-dd HH:mm:ss）
     * @param createTime
     * @return
     * @return String
     */
    public static String long2Time(String createTime) {
        long fooTime = Long.parseLong(createTime) * 1000L;
        return DateFormatUtils.format(fooTime, TIME_FORMAT);
    }
}
