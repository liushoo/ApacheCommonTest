package D1D2.decoder;

import com.ilotterytech.spark.D1D2.sile.ql730.impl.*;
import junit.framework.TestCase;

/**
 * Created by Zhang on 2017/7/7.
 */
public class QL730DecoderTest extends TestCase {
    public void testSingle(){
        GameCategoryDecoder decoder = new SingleDecoder();
        String content = "000104060811172425";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals("04 06 08 11 17 24 25", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        System.out.println(cons);
    }

    public void testFullComp(){
        GameCategoryDecoder decoder = new CompoundDecoder();
        String content = "1001*0104061215182330";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(8, cons.getTotalNum().intValue());
        assertEquals("01 04 06 12 15 18 23 30", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        System.out.println(cons);
    }

    public void testDanTuoComp(){
        GameCategoryDecoder decoder = new DanTuoCompoundDecoder();
        String content = "2001050815202530*010203040607091011121314161718192122232426272829";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(24, cons.getTotalNum().intValue());
        //assertEquals("09 16 17 27*04 05 07 08 15 22 25 28", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        System.out.println(cons);
    }
}
