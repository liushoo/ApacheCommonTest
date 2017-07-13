/**
 * Created by liush on 17-7-3.
 * Thread-B : 当前foo对象的x值= 40
 Thread-A : 当前foo对象的x值= 40
 Thread-B : 当前foo对象的x值= -20
 Thread-A : 当前foo对象的x值= -20
 Thread-B : 当前foo对象的x值= -80
 Thread-A : 当前foo对象的x值= -80
 从结果发现，这样的输出值明显是不合理的。原因是两个线程不加控制的访问Foo对象并修改其数据所致。
 如果要保持结果的合理性，只需要达到一个目的，就是将对Foo的访问加以限制，每次只能有一个线程在访问。这样就能保证Foo对象中数据的合理性了。
 */
public class MyRunnable implements Runnable {
    private Foo foo = new Foo();
    public static void main(String[] args) {
        MyRunnable run = new MyRunnable();
        Thread ta = new Thread(run, "Thread-A");
        Thread tb = new Thread(run, "Thread-B");
        ta.start();
        tb.start();
    }

    public void run() {
        for (int i = 0; i < 3; i++) {
            this.fix(30);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : 当前foo对象的x值= " + foo.getX());
        }
    }

    public int fix(int y) {
        return foo.fix(y);
    }


     class Foo {
        private int x = 100;

        public int getX() {
            return x;
        }

        public int fix(int y) {
            x = x - y;
            return x;
        }
    }
}
