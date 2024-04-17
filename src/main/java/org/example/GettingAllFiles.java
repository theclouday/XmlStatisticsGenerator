package org.example;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.example.MyApp.getArgs;

public class GettingAllFiles {


    public static List<String> getFiles() {
        String pathToFile = getArgs()[0];

        List<String> fileNames = new ArrayList<>();
        File folder = new File(pathToFile);
        File[] files = folder.listFiles();
        try {
            for (File file: files){
                if(file.isFile()) {
                    fileNames.add(file.getAbsolutePath());
                } else {
                    System.err.println("File don`t exist");
                }
            }
        }catch (Exception e) {
            System.err.println("Error:" + e.getMessage());
            return null;
        }
        return fileNames;
    }

}
