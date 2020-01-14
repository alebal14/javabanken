package com;

import javax.crypto.spec.PSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("Javabank/Account");

        try (Stream<Path> paths = Files.walk(Paths.get("Javabank/Account"))) {
            paths.forEach(paths.);
        }


    }
    public static void readALl() throws IOException {
        Path path = Paths.get("Javabank/Account");
        List<String> paths = new ArrayList<>();
        Files.lines(path);
        try {
            paths = Files.readAllLines(path);
        } catch(IOException e) {
            //e.printStackTrace();
        }
        System.out.println(paths.size());
    }
}
