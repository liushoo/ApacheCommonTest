package commonsIO; /**
 * Created by liush on 17-7-13.
 */

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.*;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.io.output.TeeOutputStream;

/**
 * FileUtils：提供文件操作（移动文件，读取文件，检查文件是否存在等等）的方法。  IOCase：提供字符串操作以及比较的方法。
 FileSystemUtils：提供查看指定目录剩余空间的方法。
 */
public class UtilityExample {
    public static void main(String[] args)  throws IOException {
        UtilityExample.runExample();

    }
        // We are using the file exampleTxt.txt in the folder ExampleFolder,
        // and we need to provide the full path to the Utility classes.
        private static final String EXAMPLE_TXT_PATH =
                "/home/liush/s3/error/fileError.txt";

        private static final String PARENT_DIR =
                "/home/liush/s3/error/";

    public static void runExample() throws IOException {
        System.out.println("Utility Classes example...");

        // FilenameUtils
        System.out.println("获得文件全路径: " +
                FilenameUtils.getFullPath(EXAMPLE_TXT_PATH));

        System.out.println("获得文件名: " +
                FilenameUtils.getName(EXAMPLE_TXT_PATH));

        System.out.println("获得文件名扩展名: " +
                FilenameUtils.getExtension(EXAMPLE_TXT_PATH));

        System.out.println("获得文件不带扩展名: " +
                FilenameUtils.getBaseName(EXAMPLE_TXT_PATH));

        // FileUtils

        // We can create a new File object using FileUtils.getFile(String)
        //创建文件对象
        // and then use this object to get information from the file.
        File exampleFile = FileUtils.getFile(EXAMPLE_TXT_PATH);
        LineIterator iter = FileUtils.lineIterator(exampleFile);

        System.out.println("Contents of exampleTxt...");
        //迭代每行内容
        while (iter.hasNext()) {
            System.out.println("\t" + iter.next());
        }
        iter.close();

        // We can check if a file exists somewhere inside a certain directory.
        //
        File parent = FileUtils.getFile(PARENT_DIR);
        System.out.println("Parent directory contains exampleTxt file: " +
                //判断目录文件包含指定文件
                FileUtils.directoryContains(parent, exampleFile));

        // IOCase

        String str1 = "This is a new String.";
        String str2 = "This is another new String, yes!";

        System.out.println("Ends with string (case sensitive): " +
                IOCase.SENSITIVE.checkEndsWith(str1, "string."));
        System.out.println("Ends with string (case insensitive): " +
                IOCase.INSENSITIVE.checkEndsWith(str1, "string."));

        System.out.println("String equality: " +
                IOCase.SENSITIVE.checkEquals(str1, str2));

        // FileSystemUtils
        //获得磁盘空间
        System.out.println("Free disk space (in KB): " + FileSystemUtils.freeSpaceKb("/home/liush/s3"));
        System.out.println("Free disk space (in MB): " + FileSystemUtils.freeSpaceKb("/home") / 1024 / 1024);
        /* 写文件
         * 1.这里只列出3种方式全参数形式，api提供部分参数的方法重载
         * 2.最后一个布尔参数都是是否是追加模式
         * 3.如果目标文件不存在，FileUtils会自动创建
         * */
        //static void:write(File file, CharSequence data, String encoding, boolean append)
        FileUtils.write(new File("/home/liush/s3/error/cxyapi.txt"), "程序换api", "UTF-8", true);
        //static void:writeLines(File file, Collection<?> lines, boolean append)
        List<String> lines = new ArrayList<String>();
        lines.add("欢迎访问:");
        lines.add("www.cxyapi.com");
        FileUtils.writeLines(new File("/home/liush/s3/error/cxyapi.txt"), lines, true);
        //文本写入指定文件
        String name = "my name is panxiuyan";
        File file =  new File("c:\\name.txt");
        FileUtils.writeStringToFile(file, name);


        //static void:writeStringToFile(File file, String data, String encoding, boolean append)
        //boolean追加模式
        FileUtils.writeStringToFile(new File("/home/liush/s3/error/cxyapi.txt"), "作者：cxy", "UTF-8", true);
        FileUtils.writeLines(new File("/home/liush/s3/error/cxyapi.txt"), "UTF-8", FileUtils.readLines(new File("/home/liush/s3/error/fileError.txt")), "UTF-8", true); // GBK
        //新建模式默
        FileUtils.writeLines(new File("/home/liush/s3/error/cxyapi.txt"), "UTF-8", FileUtils.readLines(new File("/home/liush/s3/error/fileError.txt"))); // GBK
        //读文件
        //static String:readFileToString(File file, String encoding)
        System.out.println(FileUtils.readFileToString(new File("/home/liush/s3/error/cxyapi.txt"), "UTF-8"));

        //static List<String>:readLines(File file, String encoding)
        System.out.println(FileUtils.readLines(new File("/home/liush/s3/error/cxyapi.txt"), "UTF-8")); //返回一个list

        //删除 文件/文件夹
        //删除目录
        //static void:deleteDirectory(File directory)
        ////文件夹不是空任然可以被删除，永远不会抛出异常
        FileUtils.deleteDirectory(new File("/home/liush/bgk/"));
        //删除 文件
        //static boolean:deleteQuietly(File file)
        FileUtils.deleteQuietly(new File("/home/liush/s3/error/cxyapi.txt"));

        //4.移动 文件/文件夹
        //移动文件 或 文件夹
        //static void：moveDirectory(File srcDir, File destDir)
        FileUtils.moveDirectory(new File("/home/liush/sparksh/"), new File("/home/liush/GBK/")); //注意这里 第二个参数文件不存在会引发异常
        //static void:moveDirectoryToDirectory(File src, File destDir, boolean createDestDir)
        FileUtils.moveDirectoryToDirectory(new File("/home/liush/GBK/"), new File("/home/liush/utf/"), true);
        /* 上面两个方法的不同是：
         * moveDirectory：D:/cxyapi2里的内容是D:/cxyapi1的内容。
         * moveDirectoryToDirectory：D:/cxyapi2文件夹移动到到D:/cxyapi3里
         *
         * 下面的3个都比较简单没提供示例，只提供了api
         * 其中moveToDirectory和其他的区别是 它能自动识别操作文件还是文件夹
          */
        //static void:moveFileToDirectory(srcFile, destDir, createDestDir)
        //static void:moveFile(File srcFile, File destFile)
        //static void:moveToDirectory(File src, File destDir, boolean createDestDir)

        //结果是cxyapi和cxyapi1在同一目录
        FileUtils.copyDirectory(new File("D:/cxyapi"), new File("D:/cxyapi1"));
        //结果是将cxyapi拷贝到cxyapi2下
        FileUtils.copyDirectoryToDirectory(new File("D:/cxyapi"), new File("D:/cxyapi2"));

        //拷贝文件
        FileUtils.copyFile(new File("d:/cxyapi.xml"), new File("d:/cxyapi.xml.bak"));
        //拷贝文件到目录中
        FileUtils.copyFileToDirectory(new File("d:/cxyapi.xml"), new File("d:/cxyapi"));
        //拷贝url到文件
        FileUtils.copyURLToFile(new URL("http://www.cxyapi.com/rss/cxyapi.xml"), new File("d:/cxyapi.xml"));
        //判断是否包含文件或者文件夹
        boolean b=FileUtils.directoryContains(new File("D:/cxyapi"), new File("D:/cxyapi/cxyapi.txt"));
        System.out.println(b);

        //获得临时目录 和 用户目录
        System.out.println(FileUtils.getTempDirectoryPath());
        System.out.println(FileUtils.getUserDirectoryPath());

        //打开流，如果不存在创建文件及其目录结构
        //第二个参数表示 文件流是否是追加方式
        FileOutputStream fos=FileUtils.openOutputStream(new File("D:/cxyapi/cxyapi.txt"),true);
        fos.write(new String("欢迎访问：www.cxyapi.com\r\n").getBytes());
        fos.close();

        //文件 或 文件夹大小
        System.out.println(FileUtils.sizeOf(new File("D:/cxyapi")));
        System.out.println(FileUtils.sizeOfDirectory(new File("D:/cxyapi")));

        //文件目录操作
        File dir = new File("c:\\test");
        FileUtils.cleanDirectory(dir);//清空目录下的文件
        FileUtils.deleteDirectory(dir);//删除目录和目录下的文件
        //网络流保存为文件
        URL url = new URL("http://www.163.com");
        File file1 = new File("c:\\163.html");
        FileUtils.copyURLToFile(url, file1);
        //目录大小
        long size = FileUtils.sizeOfDirectory(dir);
        // 输入流复制到 输出流
        Writer write = new FileWriter("c:\\kk.dat");
        InputStream ins = new FileInputStream(new File("c:\\text.txt"));
        IOUtils.copy(ins, write);
        write.close();
        ins.close();

        //目录操作
        File testFile = new File( "testFile.txt" );
        //如果不存在,新建
        // 如果存在,修改文件修改时间
        FileUtils.touch( testFile );
        //记录流的读取写入字节数
        File test = new File( "test.dat" );
        //输出流的统计
        CountingOutputStream countStream = null;
        //输入流的统计
        //CountingInputStream countStream = null;
        try {
            FileOutputStream foss = new FileOutputStream( test );
            countStream = new CountingOutputStream( foss );
            countStream.write( "Hello".getBytes( ) );
        } catch( IOException ioe ) {
            System.out.println( "Error writing bytes to file." );
        } finally {
            IOUtils.closeQuietly( countStream );
        }

        if( countStream != null ) {
            int bytesWritten = countStream.getCount( );
            System.out.println( "Wrote " + bytesWritten + " bytes to test.dat" );
        }


        //相同的内容写入不同的文本
        File test1 = new File("split1.txt");
        File test2 = new File("split2.txt");
        OutputStream outStream = null;
        try {
            FileOutputStream fos1 = new FileOutputStream( test1 );
            FileOutputStream fos2 = new FileOutputStream( test2 );
            //包含不同的文本
            outStream = new TeeOutputStream( fos1, fos2 );
            outStream.write( "One Two Three, Test".getBytes( ) );
            outStream.flush( );
        } catch( IOException ioe ) {
            System.out.println( "Error writing to split output stream" );
        } finally {
            IOUtils.closeQuietly( outStream );
        }


    }


}
