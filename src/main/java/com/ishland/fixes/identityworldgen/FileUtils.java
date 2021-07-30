package com.ishland.fixes.identityworldgen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtils {

    public static void writeClassFile(String name, byte[] bytes) {
        final File file = new File("./transformerOutputs/" + name + ".class");
        file.getParentFile().mkdirs();
        try {
            Files.write(file.toPath(), bytes, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
