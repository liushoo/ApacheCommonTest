package commonsIO; /**
 * Created by liush on 17-7-13.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.io.output.TeeOutputStream;

/**
 * org.apache.commons.io.input包中的类相似， org.apache.commons.io.output包中同样有OutputStream类的实现，他们可以在多种情况下使用，
 * 一个非常有意思的类就是 TeeOutputStream，它可以将输出流进行分流，换句话说我们可以用一个输入流将数据分别读入到两个不同的输出流。
 */
public final class OutputExample {

    private static final String INPUT = "This should go to the output.";

    public static void runExample() {
        System.out.println("Output example...");
        TeeInputStream teeIn = null;
        TeeOutputStream teeOut = null;

        try {

            // TeeOutputStream

            ByteArrayInputStream in = new ByteArrayInputStream(INPUT.getBytes("US-ASCII"));
            ByteArrayOutputStream out1 = new ByteArrayOutputStream();
            ByteArrayOutputStream out2 = new ByteArrayOutputStream();

            teeOut = new TeeOutputStream(out1, out2);
            teeIn = new TeeInputStream(in, teeOut, true);
            teeIn.read(new byte[INPUT.length()]);

            System.out.println("Output stream 1: " + out1.toString());
            System.out.println("Output stream 2: " + out2.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // No need to close teeOut. When teeIn closes, it will also close its
            // Output stream (which is teeOut), which will in turn close the 2
            // branches (out1, out2).
            try { teeIn.close(); }
            catch (IOException e) { e.printStackTrace(); }
        }
    }
}