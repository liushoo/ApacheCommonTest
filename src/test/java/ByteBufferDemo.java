import java.nio.ByteBuffer;

/**
 * Created by liush on 17-8-15.
 */
public class ByteBufferDemo {
    public static void main(String[] args){
        String str = "helloWorld";
        //ByteBuffer是最常用的缓冲区,它提供了读写其他数据类型的方法
        //wrap通过包装的方法创建的缓冲区保留了被包装数组内保存的数据.
        ByteBuffer buff  = ByteBuffer.wrap(str.getBytes());
        //position代表对缓冲区进行读写时,当前游标的位置,下一个要被读或写的元素的索引
        //limit所有对Buffer读写操作都会以limit变量的值作为上限
        System.out.println("position:"+buff.position()+"\t limit:"+buff.limit()+"\t remaining():"+buff.remaining());
        //读取两个字节
        byte[] abytes = new byte[1];
        //相对读,从position位置读取一个byte,并将position+1,为下次读写作准备
        buff.get(abytes);
        System.out.println("get one byte to string:" + new String(abytes)+"\t remaining():"+buff.remaining());
        //Reads the byte at this buffer's current position, and then increments the position.
        buff.get();
        System.out.println("获取两个字节（两次get()方法调用）后"+"\t remaining():"+buff.remaining());
        System.out.println("position:"+buff.position()+"\t limit:"+buff.limit()+"\t remaining():"+buff.remaining());
        //Sets this buffer's mark at its position. like ByteBuffer.this.mark=position
        //一个备忘位置
        buff.mark();
        System.out.println("mark()...");
        System.out.println("position:"+buff.position()+"\t limit:"+buff.limit()+"\t remaining():"+buff.remaining());

        //当读取到码流后，进行解码。首先对ByteBuffer进行flip操作，
        //它的作用是将缓冲区当前的limit设置为position,position设置为0
        //flip方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成标记之前position的值。
        // 这里的flip()方法，在详细的描述一下,其事这里是理解position和limit这两个属性的关键。
        //用于后续对缓冲区的读取操作。然后根据缓冲区可读的字节个数创建字节数组，
        //调用ByteBuffer的get操作将缓冲区可读的字节(获取position到limit的字节)
        //数组复制到新创建的字节数组中，最后调用字符串的构造函数创建请求消息体并打印。
        buff.flip();
        System.out.println("flip()...");
        System.out.println("position:"+buff.position()+"\t limit:"+buff.limit()+"\t remaining():"+buff.remaining());

        byte[] tbyte = new byte[1];
        buff.get(tbyte);
        System.out.println("get one byte to string:" + new String(tbyte)+"\t remaining():"+buff.remaining());
        System.out.println("position:"+buff.position()+"\t limit:"+buff.limit()+"\t remaining():"+buff.remaining());

        //BufferUnderflowException 测试
//        byte[] trbyte = new byte[2];
//        buff.get(trbyte);

    }
}
