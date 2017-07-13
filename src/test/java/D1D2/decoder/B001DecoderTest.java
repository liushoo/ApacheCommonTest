package D1D2.decoder;

import com.ilotterytech.spark.D1D2.sile.b001.impl.*;
import junit.framework.TestCase;

/**
 * Created by Zhang on 2017/7/7.
 */
public class B001DecoderTest extends TestCase {
    public void testSingle(){
        GameCategoryDecoder decoder = new SingleDecoder();
        String content = "0002041416182026~07";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(2, cons.getTotalNum().intValue());
        assertEquals("04 14 16 18 20 26", cons.getPrefixNumber());
        assertEquals("07", cons.getSuffixNumber());
        assertEquals(2, cons.getTimes().intValue());
        System.out.println(cons);
    }

    public void testRedComp(){
        GameCategoryDecoder decoder = new RedCompoundDecoder();
        String content = "1001*04051112152531~05";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(7, cons.getTotalNum().intValue());
        assertEquals("04 05 11 12 15 25 31", cons.getPrefixNumber());
        assertEquals("05", cons.getSuffixNumber());
        assertEquals(1, cons.getTimes().intValue());
        System.out.println(cons);
    }

    public void testBlueComp(){
        GameCategoryDecoder decoder = new BlueCompoundDecoder();
        String content = "2001*060708262729~060708";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(3, cons.getTotalNum().intValue());
        assertEquals("06 07 08 26 27 29", cons.getPrefixNumber());
        assertEquals("06 07 08", cons.getSuffixNumber());
        assertEquals(1, cons.getTimes().intValue());
        System.out.println(cons);
    }

    public void testFullComp(){
        GameCategoryDecoder decoder = new FullCompoundDecoder();
        String content = "3001*03040810212729~070813";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(21, cons.getTotalNum().intValue());
        assertEquals("03 04 08 10 21 27 29", cons.getPrefixNumber());
        assertEquals("07 08 13", cons.getSuffixNumber());
        assertEquals(1, cons.getTimes().intValue());
        System.out.println(cons);
    }

    public void testDanTuoComp(){
        GameCategoryDecoder decoder = new DanTuoCompoundDecoder();
        String content = "4001111528*1213142223~10";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(10, cons.getTotalNum().intValue());
        assertEquals("11 15 28*12 13 14 22 23", cons.getPrefixNumber());
        assertEquals("10", cons.getSuffixNumber());
        assertEquals(1, cons.getTimes().intValue());
        System.out.println(cons);
    }

    public void testDanTuoBlueComp(){
        GameCategoryDecoder decoder = new DanTuoBlueCompoundDecoder();
        String content = "500104131824*0726303133~0511";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(20, cons.getTotalNum().intValue());
        assertEquals("04 13 18 24*07 26 30 31 33", cons.getPrefixNumber());
        assertEquals("05 11", cons.getSuffixNumber());
        assertEquals(1, cons.getTimes().intValue());
        System.out.println(cons);
    }
}
