package D1D2.decoder;

import com.ilotterytech.spark.D1D2.sile.s3.impl.*;
import junit.framework.TestCase;

/**
 * Created by Zhang on 2017/7/7.
 */
public class S3DDecoderTest extends TestCase {
    public void testSingle(){
        //4.1.3.1 直选单式
        GameCategoryDecoder decoder = new Single3DDecoder();
        String content = "0001070108";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        ////System.out.println(cons.getTotalNum().intValue());
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(1, cons.getCategoryId().intValue());
         content = "0001060407^0201040607^0001080603^0201030608^0001050309";
         cons = new LotteryConsume();
        decoder.decode(content, cons);
        ////System.out.println(cons.getTotalNum().intValue());
        assertEquals(1, cons.getTotalNum().intValue());

        //System.out.println(cons);
    }

    public void testRedComp(){
        //4.1.3.2 组三直选单式
        GameCategoryDecoder decoder = new SingleCompound3Decoder();
        String content = "0101040407";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(2, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testSingleCompound6Decoder(){
        //4.1.3.3 组六直选单式（02）组六直选单式
        GameCategoryDecoder decoder = new SingleCompound6Decoder();
        String content = "0201070809";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(3, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }
    public void testSingle1DHundredDecoder(){
        GameCategoryDecoder decoder = new Single1DHundredDecoder();
        //4.1.3.4 1D（百位）（03）百位
        String content = "030105";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(4, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testSingle1DTenDecoder(){
        /***
         * 4.1.3.5 1D（十位）（04）
         **/
        GameCategoryDecoder decoder = new Single1DTenDecoder();
        String content = "040106";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(5, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testSingle1DOneDecoder(){
        /***
         * 4.1.3.6 1D（个位）（05）
         **/
        GameCategoryDecoder decoder = new Single1DOneDecoder();
        String content = "040106";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(6, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }



    public void testSingle2DHundredDecoder(){
        GameCategoryDecoder decoder = new Single2DHundredDecoder();
        //4.1.3.7 2D（百位-十位）（06）
        String content = "06010307";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(7, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testSingle2DTenDecoder(){
        /***
         *4.1.3.8 2D（百位-个位）（07）
         **/
        GameCategoryDecoder decoder = new Single2DTenDecoder();
        String content = "07010809";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(8, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testSingle2DOneDecoder(){
        /***
         * 4.1.3.9 2D（十位-个位）（08）
         **/
        GameCategoryDecoder decoder = new Single2DOneDecoder();
        String content = "08020409";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(2, cons.getTotalNum().intValue());
        assertEquals(9, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }


    public void testThroughSelection(){
        //4.1.3.10 通选投注（09）
        GameCategoryDecoder decoder = new ThroughSelectionDeconder();
        String content = "0901010409";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(10, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGuess1D0Decoder(){
        //4.1.3.11 猜1D（0:）
        GameCategoryDecoder decoder = new Guess1D0Decoder();
        String content = "0:0109";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(11, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGuess2D0Decoder(){
        //4.1.3.12 猜2D（0<）
        GameCategoryDecoder decoder = new Guess2D0Decoder();
        String content = "0<100509";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(10, cons.getTotalNum().intValue());
        assertEquals(12, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

      public void testGuessDoubleCommonDecoder(){
        //4.1.3.13 猜两同号（0;）
        GameCategoryDecoder decoder = new GuessDoubleCommonDecoder();
        String content = "0;010808";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
          assertEquals(13, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }
    public void testDirectlyElectedDecoder(){
        //4.1.3.14 直选和值（10）
        GameCategoryDecoder decoder = new DirectlyElectedDecoder();
        String content = "100123";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(15, cons.getTotalNum().intValue());
        assertEquals(20, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGroupThreeSumDecoder(){
          //4.1.3.15 组三和值（11）
        GameCategoryDecoder decoder = new GroupThreeSumDecoder();
        String content = "110123";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(3, cons.getTotalNum().intValue());
        assertEquals(21, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGroupSixSumDecoder(){
        //4.1.3.16 组六和值
        GameCategoryDecoder decoder = new GroupSixSumDecoder();
        String content = "120123";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(22, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGroupThreeSumValueDecoder(){
        //4.1.3.17 组三和值组选?主数怎么算,不确定
        GameCategoryDecoder decoder = new GroupThreeSumValueDecoder();
        String content = "160113";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        //assertEquals(15, cons.getTotalNum().intValue());
        assertEquals(23, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGroupSixSumValeDecoder(){
        //4.1.3.18 组六和值组选（17）组六和值组选?主数怎么算,不确定
        GameCategoryDecoder decoder = new GroupSixSumValeDecoder();
        String content = "170115";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(24, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testDirectlyElectedCompoundDecoder(){
        //4.1.3.19 直选复式
        GameCategoryDecoder decoder = new DirectlyElectedCompoundDecoder();
        //200103000507^050005060709^0108^
        String content = "20010101^0109^1000010203040506070809";
       // //System.out.println("==="+content);
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(10, cons.getTotalNum().intValue());
        assertEquals(30, cons.getCategoryId().intValue());
        content = "2001020507^0104^020006";
        cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(4, cons.getTotalNum().intValue());

    }


    public void testCompound1DHundredDecoder(){
        //4.1.3.20 1D（百位）复式（21）
        GameCategoryDecoder decoder = new Compound1DHundredDecoder();
        String content = "2101020008";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(2, cons.getTotalNum().intValue());
        assertEquals(31, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }


    public void testCompound1DTenDecoder(){
        //4.1.3.21 1D（十位）复式（22）
        GameCategoryDecoder decoder = new Compound1DTenDecoder();
        String content = "2201020305";
      /*  String num1=StringUtils.mid(content,4,2);
        String num2=StringUtils.substring(content,8);
       String num= StringUtils.mid(content,4,2);
        String number = StringUtils.substring("020305",2);
        //System.out.println("====="+num1+"==="+num2);
        ////System.out.println("===="+total);*/
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(3, cons.getTotalNum().intValue());
        assertEquals(32, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testCompound1DOneDecoder(){
        //4.1.3.22 1D（个位）复式（23）
        GameCategoryDecoder decoder = new Compound1DOneDecoder();
        String content = "2301020204";
      /*  String num1=StringUtils.mid(content,4,2);
        String num2=StringUtils.substring(content,8);
       String num= StringUtils.mid(content,4,2);
        String number = StringUtils.substring("020305",2);
        //System.out.println("====="+num1+"==="+num2);
        ////System.out.println("===="+total);*/
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(2, cons.getTotalNum().intValue());
        assertEquals(33, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }
    public void testCompound2DHundredDecoder(){
        //4.1.3.23 2D（百位-十位）复式（24）
        GameCategoryDecoder decoder = new Compound2DHundredDecoder();
        String content = "2401020006^03020508^";
      /*  String num1=StringUtils.mid(content,4,2);
        String num2=StringUtils.substring(content,8);
        String num= StringUtils.mid(content,4,2);
        String number = StringUtils.substring("020305",2);
        //System.out.println("====="+num1+"==="+num2);*/
        ////System.out.println("===="+total);
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(6, cons.getTotalNum().intValue());
        assertEquals(34, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }
    public void testCompound2DTenDecoder(){
        //4.1.3.24 2D（百位-个位）复式（25）
        GameCategoryDecoder decoder = new Compound2DTenDecoder();
        String content = "2501020006^03020508^";
      /*  String num1=StringUtils.mid(content,4,2);
        String num2=StringUtils.substring(content,8);
        String num= StringUtils.mid(content,4,2);
        String number = StringUtils.substring("020305",2);
        //System.out.println("====="+num1+"==="+num2);*/
        ////System.out.println("===="+total);
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(6, cons.getTotalNum().intValue());
        assertEquals(35, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testCompound2DOneDecoder(){
        //4.1.3.25 2D（十位-个位）复式（26）
        GameCategoryDecoder decoder = new Compound2DOneDecoder();
        String content = "2601020006^03020508^";
      /*  String num1=StringUtils.mid(content,4,2);
        String num2=StringUtils.substring(content,8);
        String num= StringUtils.mid(content,4,2);
        String number = StringUtils.substring("020305",2);
        //System.out.println("====="+num1+"==="+num2);*/
        ////System.out.println("===="+total);
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(6, cons.getTotalNum().intValue());
        assertEquals(36, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }


    public void testCompoundThroughSelectionDecoder(){
        //4.1.3.26 通选复式（28）
        GameCategoryDecoder decoder = new CompoundThroughSelectionDecoder();
        String content = "2801020005^020407^020109^";
      /*  String num1=StringUtils.mid(content,4,2);
        String num2=StringUtils.substring(content,8);
        String num= StringUtils.mid(content,4,2);
        String number = StringUtils.substring("020305",2);
        //System.out.println("====="+num1+"==="+num2);*/
        ////System.out.println("===="+total);
        LotteryConsume cons = new LotteryConsume();

        decoder.decode(content, cons);
        assertEquals(8, cons.getTotalNum().intValue());
        assertEquals(37, cons.getCategoryId().intValue());
        content = "28050100^1000010203040506070809^0105^";
        cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(50, cons.getTotalNum().intValue());
        //System.out.println(cons);
    }

    public void testCompoundSumValueDecoder(){
        //4.1.3.27 和值复式（:0）
        GameCategoryDecoder decoder = new CompoundSumValueDecoder();
        String content = ":0010119^";
      /*  String playmode=StringUtils.mid(content,0,2);
        //
        String times=StringUtils.mid(content,2,2);
        //
        String total=StringUtils.mid(content,4,2);
        //借取020006^03020508^
        String conta=StringUtils.substring(content,6);

        //System.out.print("==playmode="+playmode+"times:"+times+"===total:"+total+"==conta=="+conta);
*/
      /*  String num1=StringUtils.mid(content,4,2);
        String num2=StringUtils.substring(content,8);
        String num= StringUtils.mid(content,4,2);
        String number = StringUtils.substring("020305",2);
        //System.out.println("====="+num1+"==="+num2);*/
        ////System.out.println("===="+total);
       LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(38, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGroupThree3Decoder(){
        //4.1.3.28 组三包号（31）
        GameCategoryDecoder decoder = new GroupThree3Decoder();
        String content = "3101040408^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(12, cons.getTotalNum().intValue());
        assertEquals(40, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGroupSixDecoder(){
        //4.1.3.29 组六包号（32）
        GameCategoryDecoder decoder = new GroupSixDecoder();
        String content = "32010404060709^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(4, cons.getTotalNum().intValue());
        assertEquals(41, cons.getCategoryId().intValue());
         content = "32010504060709^";
         cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(10, cons.getTotalNum().intValue());

        //System.out.println(cons);
    }


    public void testGroupSixDirectlyElectedDecoder(){
        //4.1.3.30 组六直选复式（34）
        GameCategoryDecoder decoder = new GroupSixDirectlyElectedDecoder();
        String content = "3401050001060809^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(120, cons.getTotalNum().intValue());
        assertEquals(42, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }
    public void testGroupThreeDirectlyElectedDecoder(){
        //4.1.3.31 组三直选复式
        GameCategoryDecoder decoder = new GroupThreeDirectlyElectedDecoder();
        String content = "36010400050709^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(12, cons.getTotalNum().intValue());
        assertEquals(43, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testDirectlyElected3CompoundDecoder(){
        //4.1.3.32 直选复式（3:）
        GameCategoryDecoder decoder = new DirectlyElected3CompoundDecoder();
        String content = "3:01050405060709^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(10, cons.getTotalNum().intValue());
        assertEquals(44, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }
    public void testCompoundGuessoddEvenDecoder(){
        // 4.1.3.33 猜奇偶复式
        GameCategoryDecoder decoder = new CompoundGuessoddEvenDecoder();
        String content = "37010404050208^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(4, cons.getTotalNum().intValue());
        assertEquals(45, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }
    public void testCompoundTractorAscDecoder(){
        // 4.1.3.34 拖拉机升序复式（完全猜想。可能不对）
        GameCategoryDecoder decoder = new CompoundTractorAscDecoder();
        String content = "380103000409^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(3, cons.getTotalNum().intValue());
        assertEquals(46, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }
    public void testCompoundTractorDescDecoder(){
        // 4.1.3.35 拖拉机降序复式（完全猜想。可能不对）
        GameCategoryDecoder decoder = new CompoundTractorDescDecoder();
        String content = "390103020306^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(3, cons.getTotalNum().intValue());
        assertEquals(47, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGuess2DCommonDecoder(){
        //4.1.3.36 猜2D两同号（41）
        GameCategoryDecoder decoder = new Guess2DCommonDecoder();
        String content = "4101020102^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(50, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }
    public void testGuessOddEvenDecoder(){
        // 4.1.3.37 猜奇偶（42）
        GameCategoryDecoder decoder = new GuessOddEvenDecoder();
        String content = "4201060304^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(1, cons.getTotalNum().intValue());
        assertEquals(51, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testSingleDanTuoCompoundDecoder(){
        //4.1.3.38 单选全胆拖（50）
        GameCategoryDecoder decoder = new SingleDanTuoCompoundDecoder();
        String content = "500107*0104^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(19, cons.getTotalNum().intValue());
        //System.out.println(cons);

         content = "50010809*01^";

        decoder.decode(content, cons);
        assertEquals(12, cons.getTotalNum().intValue());
        //System.out.println(cons);

        content = "50010404*0105^";
        decoder.decode(content, cons);
        assertEquals(7, cons.getTotalNum().intValue());
        //System.out.println(cons);
        content = "50010407*00010203050608^";
        decoder.decode(content, cons);
        assertEquals(48, cons.getTotalNum().intValue());

        content = "50010404*00020608";
        decoder.decode(content, cons);
        assertEquals(13, cons.getTotalNum().intValue());
        content = "50010202*00010709";
        decoder.decode(content, cons);
        assertEquals(13, cons.getTotalNum().intValue());
    }

    public void testGroupThreeDanTuoCompoundDecoder(){
        //4.1.3.39 组三胆拖（51）
        GameCategoryDecoder decoder = new GroupThreeDanTuoCompoundDecoder();
        String content = "510107*02050809^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(8, cons.getTotalNum().intValue());
        //System.out.println(cons);

        content = "51010505*0001020304060708^";

        decoder.decode(content, cons);
        assertEquals(8, cons.getTotalNum().intValue());
        ////System.out.println(cons);
        assertEquals(61, cons.getCategoryId().intValue());
    }


    public void testGroupSixDanTuoCompoundDecoder(){
        //4.1.3.40 组六胆拖（52）
        GameCategoryDecoder decoder = new GroupSixDanTuoCompoundDecoder();
        String content = "520107*01030405^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(6, cons.getTotalNum().intValue());
       // //System.out.println(cons);

        content = "520109*02040608^";

        decoder.decode(content, cons);
        assertEquals(7, cons.getTotalNum().intValue());
        assertEquals(62, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testGroupDanTuoCompoundDecoder(){
        //4.1.3.41 组合胆拖（53）
        GameCategoryDecoder decoder = new GroupDanTuoCompoundDecoder();
        String content = "530108*02030406^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(15, cons.getTotalNum().intValue());
        //System.out.println(cons);

        content = "53010607*0001020304050809^";

        decoder.decode(content, cons);
        assertEquals(10, cons.getTotalNum().intValue());
        //System.out.println(cons);

        content = "53010505*000102030406070809^";

        decoder.decode(content, cons);
        assertEquals(10, cons.getTotalNum().intValue());
        assertEquals(63, cons.getCategoryId().intValue());
        //System.out.println(cons);
    }

    public void testSingleElectedDoubleCompoundDecoder(){
        //4.1.3.42 单选单胆拖（54）
        GameCategoryDecoder decoder = new SingleElectedSingleCompoundDecoder();
        String content = "540106*0102030407^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(60, cons.getTotalNum().intValue());
        //System.out.println(cons);

        content = "54010008*01020304^";

        decoder.decode(content, cons);
        assertEquals(24, cons.getTotalNum().intValue());
        //System.out.println(cons);
        assertEquals(64, cons.getCategoryId().intValue());

    }

    public void testSingleElectedSingleCompoundDecoder(){
        //4.1.3.43 单选双胆拖（55）
        GameCategoryDecoder decoder = new SingleElectedDoubleCompoundDecoder();
        String content = "550104*000208^";
        LotteryConsume cons = new LotteryConsume();
        decoder.decode(content, cons);
        assertEquals(18, cons.getTotalNum().intValue());
        //System.out.println(cons);

        content = "55010000*010203040506070809^";

        decoder.decode(content, cons);
        assertEquals(27, cons.getTotalNum().intValue());
        assertEquals(65, cons.getCategoryId().intValue());
        //System.out.println(cons);


    }



}
