package com.space.engine.core.log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileWriter implements LogWriter {

    private static final Path        logDirectory = Path.of("./log");
    private final        PrintWriter writer;

    public FileWriter() throws IOException {
        String name = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        File file = logDirectory.resolve(name + ".log").toFile();

        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) file.createNewFile();

        FileOutputStream   fos = new FileOutputStream(file, true);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter     bw  = new BufferedWriter(osw);

        writer = new PrintWriter(bw, true);
    }

    @Override
    public synchronized void write(String msg) {
        writer.println("[" + currentTime() + "][INFO]: " + msg);
        writer.flush();
    }

    @Override
    public synchronized void write(String msg, Exception e) {
        writer.println("[" + currentTime() + "][EXCEPTION]:" + msg);
        writer.println("[" + currentTime() + "][EXCEPTION]:" + e.getMessage());
        for (StackTraceElement trace : e.getStackTrace()) {
            writer.println("[" + currentTime() + "][EXCEPTION][" + trace.getLineNumber() + "]:" + trace.getClassName() + " @ " + trace.getMethodName());
        }
        writer.flush();
    }

    @Override
    public void write(Level level, String msg) {
        writer.println("[" + currentTime() + "][" + level.getValue() + "]: " + msg);
        writer.flush();
    }

    private String currentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
