package D1D2.decoder;

import com.ilotterytech.spark.D1D2.sile.k520.impl.*;
import junit.framework.TestCase;

/**
 * 快乐十分
 * Created by Zhang on 2017/7/11.
 */
public class K520DecoderTest extends TestCase {
    public void testSelectSingle(){
        GameCategoryDecoder decoder = new SingleSelectDecoder();
        String content = "0000010103";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals("01|03", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(1, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "000001020717";
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals("02|07 17", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(2, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "00000203111920";
        decoder.decode(content, cons);
        assertEquals(2, cons.getTotalNum().intValue());
        assertEquals("03|11 19 20", cons.getPrefixNumber());
        assertEquals(2, cons.getTimes().intValue());
        assertEquals(3, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "0000010408121317";
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals("04|08 12 13 17", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(4, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "000001051213161719";
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals("05|12 13 16 17 19", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(5, cons.getCategoryId().intValue());
        System.out.println(cons);
    }

    public void testSelect3Combine(){
        GameCategoryDecoder decoder = new Select3CombineDecoder();
        String content = "01000103050916";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals("03|05 09 16", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(10, cons.getCategoryId().intValue());
        System.out.println(cons);
    }

    public void testSelect3Arrage(){
        GameCategoryDecoder decoder = new Select3ArrangeDecoder();
        String content = "02000103031206";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals("03|03 12 06", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(11, cons.getCategoryId().intValue());
        System.out.println(cons);
    }

    public void testFullCompound(){
        GameCategoryDecoder decoder = new FullCompoundDecoder();
        String content = "1000010106*030609121319";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(6, cons.getTotalNum().intValue());
        assertEquals("01|03 06 09 12 13 19", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(21, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "1000010206*010304060713";
        decoder.decode(content, cons);
        assertEquals(15, cons.getTotalNum().intValue());
        assertEquals("02|01 03 04 06 07 13", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(22, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "1000010305*0306071719";
        decoder.decode(content, cons);
        assertEquals(10, cons.getTotalNum().intValue());
        assertEquals("03|03 06 07 17 19", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(23, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "1000010405*0306091112";
        decoder.decode(content, cons);
        assertEquals(5, cons.getTotalNum().intValue());
        assertEquals("04|03 06 09 11 12", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(24, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "1000010508*0306071014161920";
        decoder.decode(content, cons);
        assertEquals(56, cons.getTotalNum().intValue());
        assertEquals("05|03 06 07 10 14 16 19 20", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(25, cons.getCategoryId().intValue());
        System.out.println(cons);
    }

    public void testDanTuo(){
        GameCategoryDecoder decoder = new DanTuoCompoundDecoder();
        String content = "200001020116*050102030607";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(5, cons.getTotalNum().intValue());
        assertEquals("02|16*01 02 03 06 07", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(42, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "20000103021718*050809101113";
        decoder.decode(content, cons);
        assertEquals(5, cons.getTotalNum().intValue());
        assertEquals("03|17 18*08 09 10 11 13", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(43, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "20000104021415*06040607101117";
        decoder.decode(content, cons);
        assertEquals(15, cons.getTotalNum().intValue());
        assertEquals("04|14 15*04 06 07 10 11 17", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(44, cons.getCategoryId().intValue());
        System.out.println(cons);

        content = "20000105021217*1001020304050607080915";
        decoder.decode(content, cons);
        assertEquals(120, cons.getTotalNum().intValue());
        assertEquals("05|12 17*01 02 03 04 05 06 07 08 09 15", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(45, cons.getCategoryId().intValue());
        System.out.println(cons);
    }

    public void testSelect3CombineCompound(){
        GameCategoryDecoder decoder = new Select3CombineCompoundDecoder();
        String content = "1100010307*04070809121518";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(35, cons.getTotalNum().intValue());
        assertEquals("03|04 07 08 09 12 15 18", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(30, cons.getCategoryId().intValue());
        System.out.println(cons);
    }

    public void testSelect3ArrageCompound(){
        GameCategoryDecoder decoder = new Select3ArrangeCompoundDecoder();
        String content = "30000103020708^021213^021518";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(8, cons.getTotalNum().intValue());
        assertEquals("03|07 08^12 13^15 18", cons.getPrefixNumber());
        assertEquals(1, cons.getTimes().intValue());
        assertEquals(31, cons.getCategoryId().intValue());
        System.out.println(cons);
    }
}
