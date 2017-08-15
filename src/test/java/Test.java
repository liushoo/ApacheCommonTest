import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by liush on 17-7-4.
 */
public class Test {
    private static char[] is = new char[] { '1', '2', '4', '5', '6', '7', '8', '9'};
    private static int total;
    private static int m = 6;

    public static void main(String[] args) {
        final Calendar caled = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //20140731160000
        //取前一天
        caled.add(Calendar.DATE, -1);
        caled.set(Calendar.HOUR,10);
        //caled.set(Calendar.HOUR,21);
       // caled.set(caled.get(Calendar.YEAR),caled.get(Calendar.MONTH),caled.get(Calendar.DATE),caled.get(Calendar.HOUR));
        System.out.println("AM_PM:"+caled.get(Calendar.AM_PM));
        System.out.println("HOUR:"+(caled.get(Calendar.AM_PM)==0?"上午":"下午"));
        System.out.println("HOUR:"+caled.get(Calendar.HOUR));
        System.out.println("MINUTE:"+caled.get(Calendar.MINUTE));
        System.out.println("SECOND:"+caled.get(Calendar.SECOND));
        System.out.println("getTimeInMillis:"+caled.getTimeInMillis());

        Date   d=null;
        SimpleDateFormat sf   =   new   SimpleDateFormat("yyyy-MM-dd");

        try {
           // sf.format(caled.getTime())
            d= sf.parse("2007-11-27 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //毫秒
        long val = caled.getTimeInMillis();
        System.out.println("Time:"+caled.getTimeInMillis()+"==TimeInMillis=="+d.getTime());
    }

}
