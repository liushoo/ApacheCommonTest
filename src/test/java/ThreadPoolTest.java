import java.util.Random;

/**
 * Created by liush on 17-8-3.
 */
import java.util.concurrent.*;

/**
 * @author guojf
 * @version 1.0.0.2011-12-24
 */
public class ThreadPoolTest {

    private static double[] d;
    private static class ThreadPoolExecutorTask implements Callable<Integer> {
        private int first;
        private int last;
        public ThreadPoolExecutorTask(int first, int last) {
            this.first = first;
            this.last = last;
        }
        public Integer call() {
            int subCount = 0;
            for (int i = first; i <= last; i++) {
                if (d[i] < 0.5) {
                    subCount++;
                }
            }
            return subCount;
        }
    }
    private static double[] createArrayOfRandomDoubles(int maxSize) {
        Random r = new Random();
        double[] dArray = new double[maxSize];
        for (int i = 0; i < maxSize; i++) {
            dArray[i] = r.nextDouble();
        }
        return dArray;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        d = createArrayOfRandomDoubles(1000);
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(4, 4, Long.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue());
        Future[] f = new Future[4];
        int size = d.length / 4;
        for (int i = 0; i < 3; i++) {
            f[i] = tpe.submit(new ThreadPoolExecutorTask(i * size, (i + 1) * size - 1));
        }
        f[3] = tpe.submit(new ThreadPoolExecutorTask(3 * size, d.length - 1));
        int n = 0;
        for (int i = 0; i < 4; i++) {
            n += (Integer) f[i].get();
        }
        System.out.println("Found " + n + " values");
    }

}
