/**
 * Created by liush on 17-7-3.
 */
import java.util.concurrent.TimeUnit;

/**
 * 当使用同步块时，如果方法下的同步块都同步到一个对象上的锁，则所有的任务（线程）只能互斥的进入这些同步块。
 Resource1.Java演示了三个线程（包括main线程）试图进入某个类的三个不同的方法的同步块中，虽然这些同步块处在不同的方法中，
 但由于是同步到同一个对象（当前对象 synchronized (this)），所以对它们的方法依然是互斥的。
 */
public class Resource1 {

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
        synchronized (this) {
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
        synchronized (this) {
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
        final Resource1 rs = new Resource1();

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
