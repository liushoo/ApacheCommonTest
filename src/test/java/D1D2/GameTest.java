package D1D2;

import com.ilotterytech.spark.D1D2.sile.SiLeGameFactoryFactory;
import junit.framework.TestCase;

/**
 * Created by Zhang on 2017/7/7.
 */
public class GameTest extends TestCase {
    public void testGameFactory(){
       // GameFactory factory = SiLeGameFactoryFactory.getInstance().getFactory("B001");
        GameFactory factory = SiLeGameFactoryFactory.getInstance().getFactory("S3");
        System.out.println(factory.getCategoryFactory().getAllDecoders());
        System.out.println(SiLeGameFactoryFactory.getInstance().getALlFactory());
    }
}
