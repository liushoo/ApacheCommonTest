package Hadoop;

/**
 * Created by liush on 17-7-27.
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class HdfsUtilsTest {

    public static String uri = "hdfs://name-node1:8020";
    public String dir = "/user/liush/lotteryData/home";
    public String parentDir = "/user/liush/lotteryData/";

    @Test
    public void testMkdirNull1() {
        try{
            assertEquals(false, HdfsUtils.mkdir(null));
            assertEquals(false, HdfsUtils.mkdir(" "));
            assertEquals(false, HdfsUtils.mkdir(""));
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testMkdirNormal1() {
        try{
            HdfsUtils.deleteDir(dir);
            boolean result = HdfsUtils.mkdir(dir);
            assertEquals(true, result);

            List<String> listFile = HdfsUtils.listAll(parentDir);
            boolean existFile = false;
            for(String elem : listFile){
                if(elem.equals(uri + dir)){
                    existFile = true;
                    break;
                }
            }
            assertEquals(true, existFile);
        } catch(Exception ex){
            ex.printStackTrace();
            assertEquals(true, false);
        }
    }

    @Test
    public void testDeleteDirNull1() {
        try{
            assertEquals(false, HdfsUtils.deleteDir(null));
            assertEquals(false, HdfsUtils.deleteDir(""));
            assertEquals(false, HdfsUtils.deleteDir(" "));
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testDeleteDir() {
        try{
            assertEquals(true, HdfsUtils.mkdir(dir));
            assertEquals(true, HdfsUtils.deleteDir(dir));
            List<String> listFile = HdfsUtils.listAll(parentDir);
            boolean existFile = false;
            for(String elem : listFile){
                if(uri + dir == elem){
                    existFile = true;
                    break;
                }
            }
            assertEquals(false, existFile);
        } catch(Exception ex){
            ex.printStackTrace();
            assertEquals(true, false);
        }
    }

    @Test
    public void testListAllNull1() {
        try{
            List<String> listFile = new ArrayList<String>();
            assertEquals(listFile.toString(), HdfsUtils.listAll(null).toString());
            assertEquals(listFile.toString(), HdfsUtils.listAll(" ").toString());
            assertEquals(listFile.toString(), HdfsUtils.listAll("").toString());
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testListAllEmptyFolder() {
        try{
            HdfsUtils.deleteDir(dir);
            assertEquals(true, HdfsUtils.mkdir(dir));
            List<String> listFile = HdfsUtils.listAll(dir);
            assertEquals(0, listFile.size());
        } catch(Exception ex){
            ex.printStackTrace();
            assertEquals(true, false);
        }
    }

    @Test
    public void testListAllNotExistFolder() {
        try{
            HdfsUtils.deleteDir(dir);
            List<String> listFile = HdfsUtils.listAll(dir);
            assertEquals(0, listFile.size());
        } catch(Exception ex){
            assertEquals(true, true);
        }
    }

    @Test
    public void testUploadLocalFile2HDFSNull1() {
        try{
            assertEquals(false, HdfsUtils.uploadLocalFile2HDFS(null, null));
            assertEquals(false, HdfsUtils.uploadLocalFile2HDFS("", ""));
            assertEquals(false, HdfsUtils.uploadLocalFile2HDFS(" ", " "));
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testUploadLocalFile2HDFS() {
        String localFile = "/software/idea/workspace/scala-concurrent-examples-book/src/main/scala/org/learningconcurrency/ch2/Volatile.scala";
        String remoteFile = dir + "/Volatile.scala";

        try{
            HdfsUtils.mkdir(dir);
            HdfsUtils.deleteHDFSFile(remoteFile);
            assertEquals(true, HdfsUtils.uploadLocalFile2HDFS(localFile, remoteFile));
        } catch(Exception ex){
            ex.printStackTrace();
            assertEquals(true, false);
        }
    }

    @Test
    public void testUploadLocalFile2HDFSLocalNotExist() {
        String localFile = "/software/idea/workspace/scala-concurrent-examples-book/src/main/scala/org/learningconcurrency/ch2/Threads.scala";
        String remoteFile = dir + "/eclipse.ini";

        try{
            assertEquals(true, HdfsUtils.mkdir(dir));
            HdfsUtils.deleteHDFSFile(remoteFile);
            HdfsUtils.uploadLocalFile2HDFS(localFile, remoteFile);
        } catch(Exception ex){
            assertEquals(true, true);
        }
    }

    @Test
    public void testCreateNewHDFSFileNull1() {
        try{
            assertEquals(false, HdfsUtils.createNewHDFSFile(null, null));
            assertEquals(false, HdfsUtils.createNewHDFSFile(" ", " "));
            assertEquals(false, HdfsUtils.createNewHDFSFile("", ""));
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testCreateNewHDFSFileNormal1() {
        try{
            String newFile = dir + "/file1.txt";
            String content = "hello file1";
            //删除文件及目录的内容
            HdfsUtils.deleteHDFSFile(newFile);
            assertEquals(true, HdfsUtils.createNewHDFSFile(newFile, content));
            //读取文件内容
            String result = new String(HdfsUtils.readHDFSFile(newFile));
            assertEquals(content, result);
        } catch(Exception ex){
            ex.printStackTrace();
            assertEquals(true, false);
        }
    }

    @Test
    public void testCreateNewHDFSFileFoldNotexist1() {
        try{
            String newFile = dir + "/file1.txt";
            String content = "hello file1";
            //删除文件及目录的内容
            assertEquals(true, HdfsUtils.deleteDir(dir));
            assertEquals(true, HdfsUtils.createNewHDFSFile(newFile, content));
        } catch(Exception ex){
            ex.printStackTrace();
            assertEquals(true, false);
        }
    }

    @Test
    public void testDeleteHDFSFileNull1() {
        try{
            assertEquals(false, HdfsUtils.deleteHDFSFile(null));
            assertEquals(false, HdfsUtils.deleteHDFSFile(" "));
            assertEquals(false, HdfsUtils.deleteHDFSFile(""));
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testDeleteHDFSFile() {
        this.testUploadLocalFile2HDFS();
        try{
            String remoteFile = dir + "/file2.txt";
            assertEquals(true, HdfsUtils.deleteHDFSFile(remoteFile));
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testDeleteHDFSFileNotexist1() {
        try{
            String remoteFile = dir + "/eclipse2.ini";
            assertEquals(false, HdfsUtils.deleteHDFSFile(remoteFile));
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testReadHDFSFileNull1() {
        try{
            assertEquals(null, HdfsUtils.readHDFSFile(null));
            assertEquals(null, HdfsUtils.readHDFSFile(" "));
            assertEquals(null, HdfsUtils.readHDFSFile(""));
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testReadHDFSFile() {
        this.testUploadLocalFile2HDFS();
        try{
            String remoteFile = dir + "/eclipse.ini";
            String result = new String(HdfsUtils.readHDFSFile(remoteFile));
            assertEquals(true, result.length() > 0);
        } catch(Exception ex){
            ex.printStackTrace();
            assertEquals(true, false);
        }
    }

    @Test
    public void testAppendNull1() {
        try{
            assertEquals(false, HdfsUtils.append(null, null));
            assertEquals(false, HdfsUtils.append(" ", " "));
            assertEquals(false, HdfsUtils.append("", ""));
        } catch(Exception ex){
            assertEquals(true, false);
        }
    }

    @Test
    public void testAppend() {
        try{
            String newFile = dir + "/file1.txt";
            String content1 = "hello append1\r\n";
            String content2 = "hello append2\r\n";

            HdfsUtils.deleteHDFSFile(newFile);
            //追加文件内容
            assertEquals(true, HdfsUtils.createNewHDFSFile(newFile, ""));
            assertEquals(true, HdfsUtils.append(newFile, content1));
            assertEquals(content1, new String(HdfsUtils.readHDFSFile(newFile)));
            assertEquals(true, HdfsUtils.append(newFile, content2));
            assertEquals(content1 + content2, new String(HdfsUtils.readHDFSFile(newFile)));
        } catch(Exception ex){
            ex.printStackTrace();
            assertEquals(true, false);
        }
    }
    public static void main(String[] args) throws ParseException, IOException, URISyntaxException {

    FileSystem fs=FileSystem.get(new URI(uri), new Configuration());

        //QL730目录一起删除
     Path f = new Path("/user/liush/dbunix/QL730/ticket/2016127");
        if (fs.exists(f))
            fs.delete(f, true);

   /*    String localSrc = "/home/liush/gbk/HiveFromSpark.scala";//本地文件
         String dir = "/user/liush/lotteryData/home2/HiveFromSpark.scala";
        String dst =uri+dir; //"hdfs://localhost:9000/user/ganliang/hadoop_in/test_fileCopyWithProgress.txt";//复制到hdfs目录下
        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
        Configuration conf = new Configuration();
        Path orga = new Path("/user/liush/lotteryData//selldata/QL730/ticket/2016127");
        Path  desa = new Path("/user/liush/dbunix/QL730/ticket/2016127");
        //支持目录复制,如果目标目录不存在,则创建新的目录
        FileUtil.copy(fs, orga, fs, desa, false, conf);*/
 /*
        OutputStream out = fs.create(new Path(dst), new Progressable() {//进度条信息
            public void progress() {
                System.out.print(".");
            }
        });


        IOUtils.copyBytes(in, out, 4096, true);//复制

        //文件属性以FileStatus对象进行封装,使用FileSystem对象的getFileStatus()方法,可以获取到文件的FileStatus对象。
        //获取FileSystem对象。
        FileStatus status = fs.getFileStatus(new Path(dir));
        System.out.println(status.getOwner()+" "+status.getModificationTime());

        //使用FileSystem的ListStatus方法，可以获取到某个目录下所有文件的FileStatus对象
        FileStatus[] stats =  fs.listStatus(new Path(dir));

        Path[] paths = FileUtil.stat2Paths(stats);
        for(Path path : paths){
            System.out.println(path);
        }
        String localFile = "/software/idea/workspace/scala-concurrent-examples-book/src/main/scala/org/learningconcurrency/ch2/Volatile.scala";
        String destFile = "/user/liush/lotteryData/home8/Volatile.scala";
        InputStream ino = null;
        OutputStream outo = null;
        try {
            //1､准备输入流
            ino = new BufferedInputStream(new FileInputStream(localFile));
            //2､准备输出流
            outo = fs.create(new Path(destFile));
            //3､复制
            IOUtils.copyBytes(ino, outo, 4096, false);
        } finally {
            ino.close();
            outo.close();
        }


        Path pattern = new Path("/user/liush/lotteryData/home2/" + "Volatile*");
        Path sdir = new Path("/user/liush/lotteryData/test");
        if (!fs.exists(sdir)) {      // 目标目录
            fs.mkdirs(sdir);
        }
        FileStatus[] statuss = fs.globStatus(pattern);   //  匹配正则表达式的文件集合
        Path[] pathsp = FileUtil.stat2Paths(statuss);      //  转换为Path数组
        for (Path i : pathsp) {                                  //   复制每个文件
            FileUtil.copy(fs, i, fs, sdir, false, conf);   // false 表示移动的时候不删除原路径文件
            System.out.println("FileUtil.copy:"+i);
        }

        Path  dird = new Path("/user/liush/lotteryData/home2/"); // Path1 对应的目录不存在时，需要创建

        if(!fs.exists(dird))
            fs.mkdirs(dird);
        String Path2="/user/liush/lotteryData/test/";
        FileStatus[]   statusa = fs.globStatus(new Path(Path2+"Volatile*"));
        Path[]  pathss = FileUtil.stat2Paths(statusa);
        for (Path i : pathss) {
            System.out.println("==="+i.getName());
            FileUtil.copy(fs, i, fs, dird, false, conf);
        }


        //目录重命名
       // fs.rename(new Path("/user/liush/lotteryData/home/"), new Path("/user/liush/lotteryData/home2/"));
        //支持文件重命名
        fs.rename(new Path("/user/liush/lotteryData/home2/HiveFromSpark.scala"), new Path("/user/liush/lotteryData/home2/Spark.scala"));
        Path org = new Path("/user/liush/lotteryData/test");
        Path  des = new Path("/user/liush/lotteryData/test2/");
        //支持目录复制,如果目标目录不存在,则创建新的目录
        FileUtil.copy(fs, org, fs, des, false, conf);
        Path  dess = new Path("/user/liush/lotteryData/test2/");
        Path  des2 = new Path("/user/liush/lotteryData/test3/");
        //支持目录复制,如果目标目录不存在,则创建新的目录,true删除原目录
        FileUtil.copy(fs, dess, fs, des2, true, conf);*/

    }

}