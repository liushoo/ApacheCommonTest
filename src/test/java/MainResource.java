import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by liush on 17-6-22.
 */
public class MainResource {
    public static void main(String[] asrgs) throws URISyntaxException, MalformedURLException {
        MainResource test=new MainResource();
       System.out.println( MainResource.class.getClass());
        System.out.println("==null=="+ MainResource.class.getClass().getClassLoader());
       System.out.println( test.getClass().getClassLoader());
        final URL resource = MainResource.class.getClassLoader().getResource("core-site.xml");
        System.out.println("=========="+resource);
        final URL testResource =test.getClass().getClassLoader().getResource("core-site.xml");
        System.out.println("=========="+testResource);
        URL url = Thread.currentThread().getContextClassLoader().getResource("core-site.xml");
       // final URL testLocalResource =ThreadLocal.class.getClassLoader().getResource("core-site.xml");
        System.out.println("=========="+url);
         String HADOOP_HOME = "/opt/cloudera/parcels/CDH/lib/hadoop/";
         String HADOOP_CONF_DIR = "/etc/hadoop/conf/";

        //String path=Thread.currentThread().getContextClassLoader().getResource("core-site.xml").toURI().getPath();
        String path=Thread.currentThread().getContextClassLoader().getResource("core-site.xml").toURI().getPath();
         //final String HADOOP_CONF_HDFS_SITE = ConfigurationManager.getHadoopConfDir() + "/hdfs-site.xml";
        //toURI此方法不会自动转义 URL 中的非法字符,
        //将抽象路径名转换为 URL：首先通过 toURI 方法将其转换为 URI，然后通过 URI.toURL 方法将 URI 装换为 URL
        System.out.println("==="+new File(path).getParent());
        System.out.println(path+"==="+new File(path).getAbsoluteFile().toURI().toURL()+"==="+new File(path).getAbsoluteFile().toURI());
    }
}
