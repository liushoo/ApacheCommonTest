package D1D2;

import com.ilotterytech.spark.D1D2.*;
import com.ilotterytech.spark.D1D2.sile.SiLeGameFactoryFactory;
import junit.framework.TestCase;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.velocity.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Zhang on 2017/7/7.
 */
public class ProcessFileStreamTest extends TestCase {

    public void testProcessLocalFile() throws Exception{
        //构建双色球游戏解析对象
        GameFactory factory = SiLeGameFactoryFactory.getInstance().getFactory("B001");
        GameCategoryFactory cateFactory = factory.getCategoryFactory();
        LotteryConsume consume = new LotteryConsume();

        //读取文件内容
        StringBuffer buf = new StringBuffer();

        String file = "/Users/Zhang/Downloads/LotteryData/B001_2016117.txt";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] ticket = StringUtils.split(line, "\t");
            //System.out.println(line);
            //对每一行进行解析
            String cont = ticket[12];
            int total = NumberUtils.toInt(ticket[13]);
            //System.out.println(cont);
            String[] content = cateFactory.decodeContent(cont);
            int sum = 0;
            for (String s : content){
                //循环处理每一个消费注
                String prefix = cateFactory.decodeCategoryPrefix(s);
                //System.out.println(prefix);
                GameCategoryDecoder decoder = cateFactory.getCategoryDecoder(prefix);
                consume = decoder.decode(s, consume);
                //System.out.println(consume);
                sum += consume.getTotalNum();
            }

            assertEquals(total, sum);
        }

        reader.close();
    }
}
