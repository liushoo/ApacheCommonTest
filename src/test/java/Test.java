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
        System.out.println(String.format("%s=-Dfoo=ShouldBeOverriddenBelow", "spark.driver.extraJavaOptions"));
    }

    private void plzh(String s, List<Integer> iL, int m) {
        if(m == 0) {

            System.out.println(s);
            total++;
            return;
        }
        List<Integer> iL2;
        for(int i = 0; i < is.length; i++) {
            iL2 = new ArrayList<Integer>();
            iL2.addAll(iL);
            if(!iL.contains(i)) {
                String str = s + is[i];
                iL2.add(i);
                plzh(str, iL2, m-1);
            }
        }
    }
}
