/**
 * Created by liush on 17-8-5.
 */
public class ThreadLocalTestVar {
    private static Index num = new Index();
    //创建一个Index类型的本地变量
    private static ThreadLocal<Index> local = new ThreadLocal<Index>() {
        @Override
        protected Index initialValue() {
            //如果我们想给每一个线程都保存一个Index对象应该怎么办呢？那就是创建对象的副本而不是对象引用的副本：
            //new Index()
            return num;
        }
    };

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[5];
        for (int j = 0; j < 5; j++) {
            threads[j] = new Thread(new Runnable() {
                @Override
                public void run() {
                    //取出当前线程的本地变量，并累加1000次
                    Index index = local.get();
                    for (int i = 0; i < 1000; i++) {
                        index.increase();
                    }
                    System.out.println(Thread.currentThread().getName() + " : "+ index.num);

                }
            }, "Thread-" + j);
        }
        for (Thread thread : threads) {
            thread.start();
        }
        /**
         Thread-0 : 1000
         Thread-1 : 2000
         Thread-2 : 3000
         Thread-3 : 4000
         Thread-4 : 5000
         运行后结果： 覆盖initialValue函数来给我们的ThreadLocal提供初始值,每个线程都会获取这个初始值的一个副本。
         而现在我们的初始值是一个定义好的一个对象,num是这个对象的引用.换句话说我们的初始值是一个引用。
         引用的副本和引用指向的不就是同一个对象吗？
         */
    }

    static class Index {
        int num;

        public void increase() {
            num++;
        }
    }
}
