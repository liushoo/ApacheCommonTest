
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
/**
 * * 多线程数列求和
 * Created by liush on 17-8-3.
 * http://www.infoq.com/cn/articles/fork-join-introduction
 */

public class ForkJoinCountTaskTest extends RecursiveTask<Long> {
    //任务量限定
    private static final int LIMIT = 1000;
    private long start;
    private long end;

    public ForkJoinCountTaskTest(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * compute 看看当前子任务是否需要继续分割成孙任务，如果不需要继续分割，则执行当前子任务并返回结果。
     * 使用join方法会等待子任务执行完并得到其结果
     * @return
     */
    @Override
    protected Long compute() {
        long sum = 0L;
        //任务量小于阈值时，不需要分割任务。
        boolean flag = (end - start) < LIMIT;
        if (flag) {
            for (long i = start; i <= end; ++i) {
                sum += i;
            }
        } else {
            //分成100个小任务
            long step = (start + end) / 100;
            long pos = start;
            ArrayList<ForkJoinCountTaskTest> subTasks = new ArrayList<>();
            //将100个任务进行
            for (int i = 0; i < 100; ++i) {
                //任务量
                long lastOne = pos + step;
                if (lastOne > end) {
                    lastOne = end;
                }
                //构造任务
                ForkJoinCountTaskTest countTask = new ForkJoinCountTaskTest(pos, lastOne);
                pos += step + 1;
                //提交任务到队列
                subTasks.add(countTask);
                //任务分割,执行子任务
                countTask.fork();
            }
            //
            for (ForkJoinCountTaskTest countTask : subTasks) {
                //等待子任务执行完毕join,并得到结果
                sum += countTask.join();
            }
        }

        return sum;
    }
    public static void main(String[] args) {

        ForkJoinPool joinPool = new ForkJoinPool();
        ForkJoinCountTaskTest countTask = new ForkJoinCountTaskTest(0L, 20000L);

        Long startTime = System.currentTimeMillis();
        //ForkJoinTask它提供在任务中执行fork()和join()操作的机制
        //RecursiveAction：用于没有返回结果的任务。
        //RecursiveTask ：用于有返回结果的任务。
        ForkJoinTask<Long> result = joinPool.submit(countTask);
        //ForkJoinTask提供了isCompletedAbnormally()方法来检查任务是否已经抛出异常或已经被取消了
        long sum = 0;
        try {
            sum = result.get();
            Long middleTime = System.currentTimeMillis();
            System.out.println("多线程花费时间==>" +  (middleTime - startTime) +"    sum= " + sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        sum = 0;
        startTime = System.currentTimeMillis();
        for (int  i = 0; i <= 200000; ++ i) {
            sum += i;
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("一般方法花费时间==>" + (endTime - startTime) + "    sum= " + sum);
    }
}
