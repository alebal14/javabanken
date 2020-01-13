package com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileManager {

    public void write(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.createFile(path);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
    public void write(String filePath, List<String> data) {
        Path path = Paths.get(filePath);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.write(path, data);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
}
// ca 11:35 start
// 11:50 stop