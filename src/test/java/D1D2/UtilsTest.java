package D1D2;

import com.ilotterytech.spark.D1D2.sile.utils.TicketUtils;
import com.ilotterytech.spark.D1D2.utils.IdGeneratorUtils;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Zhang on 2017/7/7.
 */
public class UtilsTest extends TestCase{
    public void testDecodeNum(){
        assertEquals(1, TicketUtils.decodeNumWith4Char("0001").intValue());
        assertEquals(10, TicketUtils.decodeNumWith4Char("0010").intValue());
    }

    public void testSplit(){
        String m = "4001111528*1213142223~10^";
        for (String s : StringUtils.split(m, "^")) {
            System.out.println(s);
        }
    }

    public void testSplitNumber(){
        String[] num = TicketUtils.splitNumber("041416182026");

        for (String s : num) {
            System.out.print(s + " ");
        }

        assertEquals("04 14 16 18 20 26", TicketUtils.joinNumber(num));
    }

    public void testSeparateNumber(){
        String[] ret = TicketUtils.separateNumber("0002041416182026~07", 4);
        assertEquals("0002", ret[0]);
        assertEquals("041416182026~07", ret[1]);
    }

    public void testPrefix(){
        assertEquals("00", TicketUtils.getCategoryPrefix("0002041416182026~07"));
        assertEquals("20", TicketUtils.getCategoryPrefix("2001*060708262729~060708^"));
    }

    public void testCombine(){
        assertEquals(7, TicketUtils.combine(7, 6));
        assertEquals(21, TicketUtils.combine(7, 5));
        assertEquals(35, TicketUtils.combine(7, 4));
        assertEquals(30, TicketUtils.combine(30, 1));
    }

    public void testSeparateNumberWithChar(){
        String[] ret = TicketUtils.separateNumber("04141618*2026", "*");
        assertEquals("04141618", ret[0]);
        assertEquals("2026", ret[1]);

        String args = null;
        String region = args == null ? "test" : args;
    }

    public void testGenId(){
        for (int i = 1; i < 5; i++) {
            long id = IdGeneratorUtils.genTimestampBasedId(i);
            System.out.println(Long.toHexString(id));
        }
    }
}
