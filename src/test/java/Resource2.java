/**
 * Created by liush on 17-7-3.
 */
import java.util.concurrent.TimeUnit;

/**
 *  Resource1.java演示了三个线程（包括main线程）试图进入某个类的三个不同的方法的同步块中，这些同步块处在不同的方法中，
 *  并且是同步到三个不同的对象（synchronized (this)，synchronized(syncObject1)，synchronized (syncObject2)），
 *  所以对它们的方法中的临界资源访问是独立的。
 */
public class Resource2 {
    private Object syncObject1 = new Object();
    private Object syncObject2 = new Object();

    public void f() {
        // other operations should not be locked...
        System.out.println(Thread.currentThread().getName()
                + ":not synchronized in f()");
        synchronized (this) {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName()
                        + ":synchronized in f()");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void g() {
        // other operations should not be locked...
        System.out.println(Thread.currentThread().getName()
                + ":not synchronized in g()");
        synchronized (syncObject1) {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName()
                        + ":synchronized in g()");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void h() {
        // other operations should not be locked...
        System.out.println(Thread.currentThread().getName()
                + ":not synchronized in h()");
        synchronized (syncObject2) {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName()
                        + ":synchronized in h()");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        final Resource2 rs = new Resource2();

        new Thread() {
            public void run() {
                rs.f();
            }
        }.start();

        new Thread() {
            public void run() {
                rs.g();
            }
        }.start();

        rs.h();
    }
}
