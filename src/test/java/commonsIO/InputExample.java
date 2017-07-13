package commonsIO; /**
 * Created by liush on 17-7-13.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.io.input.XmlStreamReader;

/**
 * 在org.apache.commons.io.input包下有许多InputStrem类的实现，我们来测试一个最实用的类，TeeInputStream，
 * 将InputStream以及OutputStream作为参数传入其中，自动实现将输入流的数据读取到输出流中。
 * 而且，通过传入第三个参数，一个boolean类型参数，可以在数据读取完毕之后自动关闭输入流和输出流。
 */
public final class InputExample {

    private static final String XML_PATH =
            "C:UsersLilykosworkspaceApacheCommonsExampleInputOutputExampleFolderweb.xml";

    private static final String INPUT = "This should go to the output.";

    public static void runExample() {
        System.out.println("Input example...");
        XmlStreamReader xmlReader = null;
        TeeInputStream tee = null;

        try {

            // XmlStreamReader

            // We can read an xml file and get its encoding.
            File xml = FileUtils.getFile(XML_PATH);

            xmlReader = new XmlStreamReader(xml);
            System.out.println("XML encoding: " + xmlReader.getEncoding());

            // TeeInputStream

            // This very useful class copies an input stream to an output stream
            // and closes both using only one close() method (by defining the 3rd
            // constructor parameter as true).
            ByteArrayInputStream in = new ByteArrayInputStream(INPUT.getBytes("US-ASCII"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            tee = new TeeInputStream(in, out, true);
            tee.read(new byte[INPUT.length()]);

            System.out.println("Output stream: " + out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { xmlReader.close(); }
            catch (IOException e) { e.printStackTrace(); }

            try { tee.close(); }
            catch (IOException e) { e.printStackTrace(); }
        }
    }
}