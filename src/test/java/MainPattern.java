import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liush on 17-6-14.
 */
public class MainPattern {
    public static void main(String[] args) {

        System.out.println("===");
        String message="17/06/20 18:17:52 INFO impl.YarnClientImpl: Submitted application application_1497844616780_0132";
         Pattern PATTERN_SPARK_APP_ID = Pattern.compile("Submitted application (.*)");
        Matcher matcher = PATTERN_SPARK_APP_ID.matcher(message);
        if (matcher.find()) {
            String app_id = matcher.group(1);
            System.out.println("====message11=="+message+"=======app_id====="+app_id);

        }
    }

}
