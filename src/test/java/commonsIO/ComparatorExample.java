package commonsIO; /**
 * Created by liush on 17-7-13.
 */
import java.io.File;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.comparator.SizeFileComparator;

/**
 * 使用org.apache.commons.io.comparator 包下的类可以让你轻松的对文件或目录进行比较或者排序。
 * 你只需提供一个文件列表，选择不同的类就可以实现不同方式的文件比较。
 */
public final class ComparatorExample {

    private static final String PARENT_DIR =
            "C:UsersLilykosworkspaceApacheCommonsExampleExampleFolder";

    private static final String FILE_1 =
            "C:UsersLilykosworkspaceApacheCommonsExampleExampleFolderexample";

    private static final String FILE_2 =
            "C:UsersLilykosworkspaceApacheCommonsExampleExampleFolderexampleTxt.txt";

    public static void runExample() {
        System.out.println("Comparator example...");

        //NameFileComparator

        // Let's get a directory as a File object
        // and sort all its files.
        File parentDir = FileUtils.getFile(PARENT_DIR);
        //NameFileComparator：通过文件名来比较文件。
        NameFileComparator comparator = new NameFileComparator(IOCase.SENSITIVE);
        File[] sortedFiles = comparator.sort(parentDir.listFiles());

        System.out.println("Sorted by name files in parent directory: ");
        for (File file: sortedFiles) {
            System.out.println("t"+ file.getAbsolutePath());
        }

        // SizeFileComparator

        // We can compare files based on their size.
        // The boolean in the constructor is about the directories.
        //      true: directory's contents count to the size.
        //      false: directory is considered zero size.
        //SizeFileComparator：通过文件大小来比较文件。
        SizeFileComparator sizeComparator = new SizeFileComparator(true);
        File[] sizeFiles = sizeComparator.sort(parentDir.listFiles());

        System.out.println("Sorted by size files in parent directory: ");
        for (File file: sizeFiles) {
            System.out.println("t"+ file.getName() + " with size (kb): " + file.length());
        }

        // LastModifiedFileComparator

        // We can use this class to find which file was more recently modified.
        //LastModifiedFileComparator：通过文件的最新修改时间来比较文件。
        LastModifiedFileComparator lastModified = new LastModifiedFileComparator();
        File[] lastModifiedFiles = lastModified.sort(parentDir.listFiles());

        System.out.println("Sorted by last modified files in parent directory: ");
        for (File file: lastModifiedFiles) {
            Date modified = new Date(file.lastModified());
            System.out.println("t"+ file.getName() + " last modified on: " + modified);
        }

        // Or, we can also compare 2 specific files and find which one was last modified.
        //      returns > 0 if the first file was last modified.
        //      returns  0)
     /*   System.out.println("File " + file1.getName() + " was modified last because...");

        System.out.println("File " + file2.getName() + "was modified last because...");

        System.out.println("t"+ file1.getName() + " last modified on: " +
                new Date(file1.lastModified()));
        System.out.println("t"+ file2.getName() + " last modified on: " +
                new Date(file2.lastModified()));*/
    }
}
