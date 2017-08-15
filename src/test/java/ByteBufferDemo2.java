import java.nio.ByteBuffer;

/**
 * Created by liush on 17-8-15.
 *
 * 缓冲区(Buffer)就是在内存中预留指定大小的存储空间用来对输入/输出(I/O)的数据作临时存储,这部分预留的内存空间就叫做缓冲区
 * 使用缓冲区有这么两个好处：
    1、减少实际的物理读写次数
    2、缓冲区在创建时就被分配内存,这块内存区域一直被重用,可以减少动态分配和回收内存的次数

 在Java NIO中,缓冲区的作用也是用来临时存储数据,可以理解为是I/O操作中数据的中转站。
 缓冲区直接为通道(Channel)服务,写入数据到通道或从通道读取数据,这样的操利用缓冲区数据来传递就可以达到对数据高效处理的目的。
 http://blog.csdn.net/u012345283/article/details/38357851
 */
public class ByteBufferDemo2 {
    public static void main(String args[]){
        System.out.println("----------Test allocate--------");
        System.out.println("before alocate:"
                + Runtime.getRuntime().freeMemory());

        // 如果分配的内存过小，调用Runtime.getRuntime().freeMemory()大小不会变化？
        // 要超过多少内存大小JVM才能感觉到？

        //allocate从堆空间中分配一个容量大小为capacity的byte数组作为缓冲区的byte数据存储器
        ByteBuffer buffer = ByteBuffer.allocate(102400);
        System.out.println("buffer = " + buffer);

        System.out.println("after alocate:"
                + Runtime.getRuntime().freeMemory());

        // 这部分直接用的系统内存,所以对JVM的内存没有影响
        //allocateDirect 是不使用JVM堆栈而是通过操作系统来创建内存块用作缓冲区,它与当前操作系统能够更好的耦合,因此能进一步提高I/O操作速度
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(102400);
        System.out.println("directBuffer = " + directBuffer);
        System.out.println("after direct alocate:"
                + Runtime.getRuntime().freeMemory());

        System.out.println("----------Test wrap--------");
        byte[] bytes = new byte[32];
        //wrap这个缓冲区的数据会存放在byte数组中
        buffer = ByteBuffer.wrap(bytes);
        System.out.println(buffer);

        buffer = ByteBuffer.wrap(bytes, 10, 10);
        System.out.println(buffer);

        System.out.println("--------Test reset----------");
        //position = 0;limit = capacity;mark = -1; 有点初始化的味道
        buffer.clear();
        //位置，下一个要被读或写的元素的索引，每次读写缓冲区数据时都会改变改值，为下次读写作准备
        buffer.position(5);
        //标记，调用mark()来设置mark=position，再调用reset()可以让position恢复到标记的位置
        buffer.mark();
        buffer.position(10);
        //[pos=10 lim=32 cap=32]  pos位置，下一个要被读或写的元素的索引，每次读写缓冲区数据时都会改变改值，为下次读写作准备
        //lim 表示缓冲区的当前终点，不能对缓冲区超过极限的位置进行读写操作
        //cap容量，即可以容纳的最大数据量；在缓冲区创建时被设定并且不能改变
        System.out.println("before reset:" + buffer);
        //把position设置成mark的值，相当于之前做过一个标记，现在要退回到之前标记的地方
        buffer.reset();
        //[pos=5 lim=32 cap=32]
        System.out.println("after reset:" + buffer);

        System.out.println("--------Test rewind--------");
        //position = 0;limit = capacity;mark = -1; 有点初始化的味道
        buffer.clear();
        buffer.position(10);
        buffer.limit(15);
        //[pos=10 lim=15 cap=32]
        System.out.println("before rewind:" + buffer);
        //rewind 把position设为0，mark设为-1，不改变limit的值
        buffer.rewind();
        //[pos=0 lim=15 cap=32]
        System.out.println("before rewind:" + buffer);

        System.out.println("--------Test compact--------");
        buffer.clear();
        buffer.put("abcd".getBytes());
        //[pos=4 lim=32 cap=32]
        System.out.println("before compact:" + buffer);
        System.out.println(new String(buffer.array()));
        //limit = position;position = 0;mark = -1;
        // 翻转，也就是让flip之后的position到limit这块区域变成之前的0到position这块，翻转就是将一个处于存数据状态的缓冲区变为一个处于准备取数据的状态
        buffer.flip();
        //[pos=0 lim=4 cap=32]
        System.out.println("after flip:" + buffer);
        //相对读，从position位置读取一个byte，并将position+1，为下次读写作准备
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println("after three gets:" + buffer);
        System.out.println("\t" + new String(buffer.array()));
        //[pos=1 lim=32 cap=32]
        //把从position到limit中的内容移到0到limit-position的区域内,
        //position和limit的取值也分别变成limit-position、capacity。如果先将positon设置到limit，再compact，那么相当于clear()
        buffer.compact();
        System.out.println("after compact:" + buffer);
        System.out.println("\t" + new String(buffer.array()));

        System.out.println("------Test get-------------");
        buffer = ByteBuffer.allocate(32);
        buffer.put((byte) 'a').put((byte) 'b').put((byte) 'c').put((byte) 'd')
                .put((byte) 'e').put((byte) 'f');
        System.out.println("before flip()" + buffer);
        // 转换为读取模式
        buffer.flip();
        System.out.println("before get():" + buffer);
        System.out.println((char) buffer.get());
        System.out.println("after get():" + buffer);
        // get(index)不影响position的值
        System.out.println((char) buffer.get(2));
        System.out.println("after get(index):" + buffer);
        byte[] dst = new byte[10];
        buffer.get(dst, 0, 2);
        System.out.println("after get(dst, 0, 2):" + buffer);
        System.out.println("\t dst:" + new String(dst));
        System.out.println("buffer now is:" + buffer);
        System.out.println("\t" + new String(buffer.array()));

        System.out.println("--------Test put-------");
        ByteBuffer bb = ByteBuffer.allocate(32);
        System.out.println("before put(byte):" + bb);
        System.out.println("after put(byte):" + bb.put((byte) 'z'));
        //
        System.out.println("\t" + bb.put(2, (byte) 'c'));
        // put(2,(byte) 'c')不改变position的位置
        System.out.println("after put(2,(byte) 'c'):" + bb);
        System.out.println("\t" + new String(bb.array()));
        // 这里的buffer是 abcdef[pos=3 lim=6 cap=32]
        //相对写，向position的位置写入一个byte，并将postion+1，为下次读写作准备
        bb.put(buffer);
        System.out.println("after put(buffer):" + bb);
        System.out.println("\t" + new String(bb.array()));
    }
}
