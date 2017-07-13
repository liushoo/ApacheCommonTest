package commonsLang;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by liush on 17-7-13.
 */
public class DateFormatUtilsTest {
    public static void main(String[] args) {


       // 常用日期格式的格式化操作：
        //例1: 以 yyyy-MM-dd 格式化:
        DateFormatUtils.ISO_DATE_FORMAT.format(new Date());//: 2009-03-20

        //例2: 以 yyyy-MM-ddZZ 格式化:
        DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT.format(new Date());//: 2009-03-20+08:00

       // 例3: 以 yyyy-MM-dd'T'HH:mm:ss 格式化:
        DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date());//: 2009-03-20T22:07:01

        //例4: 以 yyyy-MM-dd'T'HH:mm:ssZZ 格式化:
        DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.format(new Date());//: 2009-03-20T22:07:01+08:00

        //例5: 以 'T'HH:mm:ss 格式化:
        DateFormatUtils.ISO_TIME_FORMAT.format(new Date());//: T22:07:01

        //例6: 以 HH:mm:ss 格式化:
        DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(new Date());//: 22:07:01

        //例7: 以 HH:mm:ssZZ 格式化:
        DateFormatUtils.ISO_TIME_NO_T_TIME_ZONE_FORMAT.format(new Date());//: 22:07:01+08:00

        //例8: 以 'T'HH:mm:ssZZ 格式化:
        DateFormatUtils.ISO_TIME_TIME_ZONE_FORMAT.format(new Date());//: T22:07:01+08:00



        //自定义日期格式的格式化操作：
       // 例1: 以 yyyy-MM-dd HH:mm:ss 格式化Date对象:
        DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");//: 2009-03-20 22:24:30

        //例2: 以 yyyy-MM-dd HH:mm:ss 格式化Calendar对象:
        DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss");//: 2009-03-20 22:24:30

        //例3: 以 yyyy-MM-dd HH:mm:ss 格式化TimeInMillis:
        DateFormatUtils.format(Calendar.getInstance().getTimeInMillis(), "yyyy-MM-dd HH:mm:ss");//: 2009-03-20 22:24:30


    }
}
