package com.ilotterytech.spark.submitShell;

/**
 * Created by liush on 17-6-20.
 */

public class BufferedLogger implements Logger {
    private final org.slf4j.Logger wrappedLogger;
    private final StringBuilder buffer = new StringBuilder();

    private static int MAX_BUFFER_SIZE = 10 * 1024 * 1024;

    public BufferedLogger(org.slf4j.Logger wrappedLogger) {
        this.wrappedLogger = wrappedLogger;
    }

    @Override
    public void log(String message) {
        wrappedLogger.info(message);
        if (buffer.length() < MAX_BUFFER_SIZE) {
            buffer.append(message).append("\n");
        }
    }

    public String getBufferedLog() {
        return buffer.toString();
    }

    public void resetBuffer() {
        buffer.setLength(0);
    }
}
