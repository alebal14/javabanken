package com;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> readData(String filePath) {
        Path path = Paths.get(filePath);
        List<String> fileData = new ArrayList<>();
        try {
            fileData = Files.readAllLines(path);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return fileData;
    }


    public void delete(String filePath) {
        Path path = Paths.get(filePath);
        if(Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    public List<Path> listFiles(String folderPath) {
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

    public List<String> findFile (String searchTerm, String typeFile){

        List<String> filesList = new ArrayList<>();
        if(typeFile.equals("customer")){
            try {
                Files.list(Paths.get("Javabank/Customer"))
                        .forEach(path -> filesList.add(path.toString()));
            } catch (IOException e) {

            }
        } else if(typeFile.equals("account")) {
            try {
                Files.list(Paths.get("Javabank/Account"))
                        .forEach(path -> filesList.add(path.toString()));
            } catch (IOException e) {

            }
        }

        List<String> newFileList = new ArrayList<>();

        for (String path : filesList) {
            try {
                List<String> propertyList = Files.readAllLines(Paths.get(path));
                for (String line : propertyList) {
                    if (line.contains(searchTerm)) {
                        newFileList.add(path);
                    }
                }
            } catch (IOException e) {

            }
        }
        return newFileList;
    }

    public List<String> getFilesPaths(String folderName) throws IOException {
        List<String> paths = new ArrayList<>();
        Files.walk(Paths.get(folderName)).forEach(path -> paths.add(path.toString()));
        paths.remove(0);
        return paths;
    }
}
