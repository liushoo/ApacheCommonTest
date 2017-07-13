import java.math.BigInteger;
import java.util.Scanner;
/**
 * Created by liush on 17-7-4.
 */
public class jiecheng {
    /**
     * 排列组合算法
     * @param a
     * @param b
     * @return
     */
    public static BigInteger C(int a,int b){
        BigInteger temp=new BigInteger("0");
        BigInteger sum=new BigInteger("1");
        for(int i=0;i<b;i++){
            String tem=a+"";
            BigInteger temp1=new BigInteger(tem);
            BigInteger t2=temp1.subtract(temp);
            BigInteger t1=sum.multiply(t2);
            sum = t1.divide(temp.add(new BigInteger("1")));
            temp=temp.add(new BigInteger("1"));
        }
        return sum;
    }
    public static void main(String args[]){
        Scanner in=new Scanner(System.in);
        BigInteger res1=C(5,2);
        System.out.println(res1);

    }

}
