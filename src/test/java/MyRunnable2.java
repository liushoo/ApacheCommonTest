/**
 * Created by liush on 17-7-3.
 *
 如果要保持结果的合理性，只需要达到一个目的，就是将对Foo的访问加以限制，每次只能有一个线程在访问。这样就能保证Foo对象中数据的合理性了。
 在具体的Java代码中需要完成一下两个操作：
 把竞争访问的资源类Foo变量x标识为private；
 同步哪些修改变量的代码，使用synchronized关键字同步方法或代码。
 */
public class MyRunnable2 {
    public static void main(String[] args) {
        MyRunnable2 run = new MyRunnable2();
        Foo2 foo2=new Foo2();

        MyThread t1 = run.new MyThread("线程A", foo2, 10);
        MyThread t2 = run.new MyThread("线程B", foo2, -2);
        MyThread t3 = run.new MyThread("线程C", foo2, -3);
        MyThread t4 = run.new MyThread("线程D", foo2, 5);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }

    class MyThread extends Thread {
        private Foo2 foo2;
        /**当前值*/
        private int y = 0;

        MyThread(String name, Foo2 foo2, int y) {
            super(name);
            this.foo2 = foo2;
            this.y = y;
        }

        public void run() {
            foo2.fix(y);
            foo2.fixSyn(y);
        }
    }

}
class Foo2 {
    private int x = 100;

    public int getX() {
        return x;
    }

    //同步方法
    public synchronized int fix(int y) {
        x = x - y;
        System.out.println("线程"+Thread.currentThread().getName() + "运行结束，减少“" + y
                + "”，当前值为：" + x);
        return x;
    }

    //同步代码块
    public int fixSyn(int y) {
       synchronized (this) {
            x = x - y;
           System.out.println("线程"+Thread.currentThread().getName() + "运行结束，减少“" + y
                   + "”，当前值为：" + x);
        }

       return x;
  }

}