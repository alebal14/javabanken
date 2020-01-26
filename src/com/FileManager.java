package com;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract public class FileManager {

    public static void write(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.createFile(path);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public static void write(String filePath, List<String> data) {
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

    public static List<String> readData(String filePath) {
        Path path = Paths.get(filePath);
        List<String> fileData = new ArrayList<>();
        try {
            fileData = Files.readAllLines(path);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return fileData;
    }


    public static void delete(String filePath) {
        Path path = Paths.get(filePath);
        if(Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    public static List<Path> listFiles(String folderPath) {
        List<Path> filesList = new ArrayList<>();
        try {
            for(Path p:Files.list(Paths.get(folderPath)).collect(Collectors.toList())) {
                filesList.add(p);
            }

        } catch (IOException e) {
            //e.printStackTrace();
        }
        return filesList;
    }


    public static List<String> searchFiles(String searchFolder, String searchQuery, String key) {
        List<String> searchResults = new ArrayList<>();

        for (Path filePath : listFiles(searchFolder)) {
            for (String line : readData(filePath.toString())) {
                if(line.split(":")[0].equals(key)) {
                    if(line.split(":")[1].toLowerCase().contains(searchQuery.toLowerCase())) {
                        searchResults.add(filePath.toString());
                    }
                }
            }
        }
        return searchResults;
    }


    public static List<String> getFilesPaths(String folderName) throws IOException {
        List<String> paths = new ArrayList<>();
        Files.walk(Paths.get(folderName)).forEach(path -> paths.add(path.toString()));
        paths.remove(0);
        return paths;
    }
}
