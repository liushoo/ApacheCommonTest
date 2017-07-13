package D1D2;

import com.ilotterytech.spark.D1D2.*;
import com.ilotterytech.spark.D1D2.sile.SiLeGameFactoryFactory;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.velocity.util.StringUtils;

import java.io.*;

/**
 * 本地投注信息解析测试类
 * Created by Zhang on 2017/7/7.
 */
public class ProcessTest extends TestCase {

    private void doGameProcessTest(String gameId, String file) throws IOException{
        GameFactory factory = SiLeGameFactoryFactory.getInstance().getFactory(gameId);
        assertNotNull(factory);

        GameCategoryFactory cateFactory = factory.getCategoryFactory();
        assertNotNull(cateFactory);

        LotteryConsume consume = new LotteryConsume();
        //读取文件内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] ticket = StringUtils.split(line, "\t");
            if(ticket.length==0){
                continue;
            }
            System.out.println(line+"==length=="+ticket.length);
            //对每一行进行解析

            String cont = ticket[12];
            int total = NumberUtils.toInt(ticket[13]);
            System.out.println(cont + " = " + total);
            String[] content = cateFactory.decodeContent(cont);
            int sum = 0;
            for (String s : content){
                //循环处理每一个消费注
                String prefix = cateFactory.decodeCategoryPrefix(s);
                //System.out.println(prefix);
                GameCategoryDecoder decoder = cateFactory.getCategoryDecoder(prefix);
                if (decoder == null)
                    System.out.println("can not found " + prefix + " game category");

                assertNotNull(decoder);
                consume = decoder.decode(s, consume);
                //System.out.println(consume);
                sum += consume.getTotalNum();
                //System.out.println(factory.getName() + "-" + decoder.getName() + ":" + sum);
            }
            //
           if(total!=sum) {

               writerLine("/home/liush/s3/error/fileError.txt",line);
           }
           // assertEquals(total, sum);
        }

        reader.close();
    }

    public static void writerLine(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void testB001ProcessLocalFile() throws IOException{
        //构建双色球游戏解析对象
        String file = "/Users/Zhang/Downloads/LotteryData/B001_2016117.txt";
        doGameProcessTest("B001", file);
    }

    public void testQL730ProcessLocalFile() throws IOException{
        //构建双色3D球游戏解析对象
        String file = "/Users/Zhang/Downloads/LotteryData/B001_2016117.txt";
        doGameProcessTest("QL730", file);
    }

    public void testS3ProcessLocalFile() throws IOException{
        //构建双色3D球游戏解析对象
       // String file = "/Users/Zhang/Downloads/LotteryData/S3_2016009.txt";
        String file = "/home/liush/s3/S3_2016002.txt";
        doGameProcessTest("S3", file);
    }

    public void testK520ProcessLocalFile() throws IOException{
        //构建双色球游戏解析对象
        String file = "/Users/Zhang/Downloads/LotteryData/K520_160101.txt";
        doGameProcessTest("K520", file);
    }
}
