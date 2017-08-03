import java.util.concurrent.RecursiveTask;

/**
 * Created by liush on 17-8-3.
 */
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class ForkJoinTest {

    private static double[] d;

    private static class ForkJoinTask extends RecursiveTask<Integer> {

        private static final int THRESHOLD = 10000;

        private int first;

        private int last;

        public ForkJoinTask(int first, int last) {
            this.first = first;
            this.last = last;
        }

        protected Integer compute() {
            int subCount;
            if (last - first < THRESHOLD) {
                subCount = 0;
                for (int i = first; i <= last; i++) {
                    if (d[i] < 0.5)
                        subCount++;
                }
            } else {
                int mid = (first + last) >>> 1;
                ForkJoinTask left = new ForkJoinTask(first, mid);
                left.fork();
                ForkJoinTask right = new ForkJoinTask(mid + 1, last);
                right.fork();
                subCount = left.join();
                subCount += right.join();
            }
            return subCount;
        }
    }

    public static void main(String[] args) {
        int maxSize = 1000000;
        d = createArrayOfRandomDoubles(maxSize);

        long st = System.currentTimeMillis();
        int n = new ForkJoinPool(1).invoke(new ForkJoinTask(0, maxSize - 1));
        long et = System.currentTimeMillis();

        System.out.println("Found " + n + " values, " + (et - st) + "ms");
    }

    private static double[] createArrayOfRandomDoubles(int maxSize) {
        Random r = new Random();
        double[] dArray = new double[maxSize];
        for (int i = 0; i < maxSize; i++) {
            dArray[i] = r.nextDouble();
        }
        return dArray;
    }
}

