package com.ilotterytech.spark.submitShell;


import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by liush on 17-6-20.
 */
public class PatternedLogger extends BufferedLogger {
    private final Map<String, String> info = Maps.newHashMap();
    private static final Pattern PATTERN_APP_ID = Pattern.compile("Submitted application (.*?) to ResourceManager");
     // spark
    private static final Pattern PATTERN_SPARK_APP_ID = Pattern.compile("Submitted application (.*)");
    private static final Pattern PATTERN_SPARK_APP_URL = Pattern.compile("tracking URL: (.*)");
    public PatternedLogger(Logger wrappedLogger) {
        super(wrappedLogger);
    }

    @Override
    public void log(String message) {
        super.log(message);
        Matcher matcher = PATTERN_APP_ID.matcher(message);
        // spark
        if (matcher.find()) {
            String appId = matcher.group(1);
            System.out.println("====message000=="+message+"=======app_id====="+appId);
            info.put("yarn_application_id ResourceManager", appId);
        }
        // spark
        matcher = PATTERN_SPARK_APP_ID.matcher(message);
        if (matcher.find()) {
            String app_id = matcher.group(1);
            System.out.println("====message11=="+message+"=======app_id====="+app_id);
            info.put("yarn_application_id", app_id);
        }
        matcher = PATTERN_SPARK_APP_URL.matcher(message);
        if (matcher.find()) {
            String trackingUrl = matcher.group(1);
            info.put("yarn_application_tracking_url", trackingUrl);
        }
    }

    public Map<String, String> getInfo() {
        return info;
    }
}
