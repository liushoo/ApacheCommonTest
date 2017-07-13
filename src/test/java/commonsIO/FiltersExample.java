package commonsIO;

/**
 * Created by liush on 17-7-13.
 */
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * 过滤器可以以组合的方式使用并且它的用途非常多样。它可以轻松的区分不同的文件并且找到满足我们条件的文件。
 * 我们可以组合不同的过滤器来执行文件的逻辑比较并且精确的获取我们所需要文件，而无需使用冗余的字符串比较来寻找我们的文件。
 */
public final class FiltersExample {

    private static final String PARENT_DIR =
            "C:UsersLilykosworkspaceApacheCommonsExampleExampleFolder";

    public static void runExample() {
        System.out.println("File Filter example...");

        // NameFileFilter
        // Right now, in the parent directory we have 3 files:
        //      directory example
        //      file exampleEntry.txt
        //      file exampleTxt.txt

        // Get all the files in the specified directory
        // that are named "example".
        File dir = FileUtils.getFile(PARENT_DIR);
        String[] acceptedNames = {"example", "exampleTxt.txt"};
        for (String file: dir.list(new NameFileFilter(acceptedNames, IOCase.INSENSITIVE))) {
            System.out.println("File found, named: " + file);
        }

        //WildcardFileFilter
        // We can use wildcards in order to get less specific results
        //      ? used for 1 missing char
        //      * used for multiple missing chars
        for (String file: dir.list(new WildcardFileFilter("*ample*"))) {
            System.out.println("Wildcard file found, named: " + file);
        }

        // PrefixFileFilter
        // We can also use the equivalent of startsWith
        // for filtering files.
        for (String file: dir.list(new PrefixFileFilter("example"))) {
            System.out.println("Prefix file found, named: " + file);
        }

        // SuffixFileFilter
        // We can also use the equivalent of endsWith
        // for filtering files.
        for (String file: dir.list(new SuffixFileFilter(".txt"))) {
            System.out.println("Suffix file found, named: " + file);
        }

        // OrFileFilter
        // We can use some filters of filters.
        // in this case, we use a filter to apply a logical
        // or between our filters.
        for (String file: dir.list(new OrFileFilter(
                new WildcardFileFilter("*ample*"), new SuffixFileFilter(".txt")))) {
            System.out.println("Or file found, named: " + file);
        }

        // And this can become very detailed.
        // Eg, get all the files that have "ample" in their name
        // but they are not text files (so they have no ".txt" extension.
        for (String file: dir.list(new AndFileFilter( // we will match 2 filters...
                new WildcardFileFilter("*ample*"), // ...the 1st is a wildcard...
                new NotFileFilter(new SuffixFileFilter(".txt"))))) { // ...and the 2nd is NOT .txt.
            System.out.println("And/Not file found, named: " + file);
        }
    }
}