import java.io.*;

/**
 * Created by liush on 17-8-13.
 */
public class FileDescriptorSync {
    /**
     * 通过一个程序我们就可以看出sync的一个特点：如果正通过应用程序(例如,通过一个 BufferedOutputStream 对象)实现内存缓冲,
     * 那么必须在数据受 sync 影响之前将这些缓冲区刷新，并转到 FileDescriptor 中(例如,通过调用 OutputStream.flush)
     */
    public  static void  main(String[] args) throws IOException {
        FileOutputStream fo=new FileOutputStream("2.txt");
        BufferedOutputStream out=new BufferedOutputStream(fo);
        FileDescriptor fd=fo.getFD();
        byte[] b="abc".getBytes();
        out.write(b);

        long start=System.nanoTime();
        //flush方法是强制将缓冲区中的内容写入到文件中,防止因缓冲区不满而带来的问题
        out.flush();
        //如果我去掉fd.sync(),那么这个值就会小的多了，我测试的是118053  。。。。
        //所以很明显，这个函数的效果，sync是在flush掉所有的数据才返回，这将会占用很长的一段时间。。。
        //sync方法强制所有系统缓冲区与基础设备同步
        //1.close()时会自动flush
        //2.在不调用close()的情况下，缓冲区不满，又需要把缓冲区的内容写入到文件或通过网络发送到别的机器时，才需要调用flush
        fd.sync();//41565346
        long end=System.nanoTime();
        System.out.println(end-start);                    //结果为63306299
    }
}
