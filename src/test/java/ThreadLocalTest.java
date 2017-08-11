/**
 * Created by liush on 17-8-5.
 */
public class ThreadLocalTest {
    //创建一个Integer型的线程本地变量
    public static final ThreadLocal<Integer> local = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[5];
        for (int j = 0; j < 5; j++) {
            threads[j] = new Thread(new Runnable() {
                @Override
                public void run() {
                    //获取当前线程的本地变量，然后累加5次
                    int num = local.get();
                    for (int i = 0; i < 5; i++) {
                        num++;
                    }
                    //重新设置累加后的本地变量
                    local.set(num);
                    System.out.println(Thread.currentThread().getName() + " : "+ local.get());

                }
            }, "Thread-" + j);
        }

        for (Thread thread : threads) {
            thread.start();
        }
        /**
         Thread-0 : 5
         Thread-1 : 5
         Thread-3 : 5
         Thread-2 : 5
         Thread-4 : 5
         运行后结果： 每个线程累加后的结果都是5，各个线程处理自己的本地变量值，线程之间互不影响。
         */

    }

}
