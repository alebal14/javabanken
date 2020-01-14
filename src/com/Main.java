package com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        JavaBank javaBank = new JavaBank();
    }

    public void delete(String filePath) {
        Path path = Paths.get(filePath);

        try {
            Files.delete(path);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
