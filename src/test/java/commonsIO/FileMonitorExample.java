package commonsIO; /**
 * Created by liush on 17-7-13.
 */
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.io.monitor.FileEntry;

/**
 * 文件监控器
 *monitor包下的类包含的方法可以获取文件的指定信息，不过更重要的是，它可以创建处理器（handler）
 * 来跟踪指定文件或目录的变化并且可以在文件或目录发生变化的时候进行一些操作
 */
public final class FileMonitorExample {

    private static final String EXAMPLE_PATH =
            "/home/liush/s3/error/fileError.txt";

    private static final String PARENT_DIR =
            "/home/liush/s3/error/";

    private static final String NEW_DIR =
            "/home/liush/s3/correct/";

    private static final String NEW_FILE =
            "/home/liush/s3/correct/correct.txt";

    public static void runExample() {
        System.out.println("File Monitor example...");

        // FileEntry

        // We can monitor changes and get information about files
        // using the methods of this class.
        FileEntry entry = new FileEntry(FileUtils.getFile(EXAMPLE_PATH));
        //获得文件实体
        System.out.println("File monitored: " + entry.getFile());
        //获得文件名
        System.out.println("File name: " + entry.getName());
        //判断是或目录
        System.out.println("Is the file a directory?: " + entry.isDirectory());

        // File Monitoring

        // Create a new observer for the folder and add a listener
        // that will handle the events in a specific directory and take action.
        //
        File parentDir = FileUtils.getFile(PARENT_DIR);

        FileAlterationObserver observer = new FileAlterationObserver(parentDir);
        observer.addListener(new FileAlterationListenerAdaptor() {

            @Override
            public void onFileCreate(File file) {
                System.out.println("File created: " + file.getName());
            }

            @Override
            public void onFileDelete(File file) {
                System.out.println("File deleted: " + file.getName());
            }

            @Override
            public void onDirectoryCreate(File dir) {
                System.out.println("Directory created: " + dir.getName());
            }

            @Override
            public void onDirectoryDelete(File dir) {
                System.out.println("Directory deleted: " + dir.getName());
            }
        });

        // Add a monior that will check for events every x ms,
        // and attach all the different observers that we want.
        FileAlterationMonitor monitor = new FileAlterationMonitor(500, observer);
        try {
            monitor.start();

            // After we attached the monitor, we can create some files and directories
            // and see what happens!
            File newDir = new File(NEW_DIR);
            File newFile = new File(NEW_FILE);

            newDir.mkdirs();
            newFile.createNewFile();

            Thread.sleep(1000);

            FileDeleteStrategy.NORMAL.delete(newDir);
            FileDeleteStrategy.NORMAL.delete(newFile);

            Thread.sleep(1000);

            monitor.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}