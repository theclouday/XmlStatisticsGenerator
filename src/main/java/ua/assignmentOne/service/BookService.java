package ua.assignmentOne.service;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ua.assignmentOne.model.BookStatistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class BookService {
    private final ExecutorService executor;

    public BookService(int numThreads) {
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    public static List<String> filesProcessing(String filePath) {

        List<String> fileNames = new ArrayList<>();
        File folder = new File(filePath);

        try {
            if(folder.isDirectory()){
                File[] files = folder.listFiles();
                for (File file: files){
                    if(file.isFile()) {
                        fileNames.add(file.getAbsolutePath());
                    } else {
                        System.err.println("File don`t exist.");
                    }
                }
            } else {
                System.err.println("I did not find this path or it was written incorrectly.");
                return null;
            }

        }catch (Exception e) {
            System.err.println("Error:" + e.getMessage());
            return null;
        }
        return fileNames;
    }

    public BookStatistics processingDataFromFile(File file, String targetFieldName) throws IOException {
        System.out.println("Parsing file: " + file.getAbsolutePath());

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(file);

        BookStatistics statistics = new BookStatistics();

        JsonToken token = null;
        while((token = parser.nextToken()) != null){
            if(JsonToken.FIELD_NAME.equals(token)) {
                String fieldName = parser.currentName();
                parser.nextToken();

                if (targetFieldName.equals(fieldName)){
                    String fieldValue = parser.getValueAsString();
                    List<String> valuesList = Arrays.asList(fieldValue.split(", "));

                    for (String value : valuesList) {
                        statistics.incrementValue(value);
                    }
                }
            }
        }
        parser.close();
        return statistics;
    }

    public BookStatistics aggregateStatistics(String filePath, String targetFieldName){
        List<String> jsonFiles = filesProcessing(filePath);

        List<BookStatistics> statisticsList = new ArrayList<>();
        List<Callable<BookStatistics>> tasks = new ArrayList<>();


        for(String fileName : jsonFiles) {
            tasks.add(() -> {
                try {
                    File file = new File(fileName);
                    return processingDataFromFile(file, targetFieldName);
                }catch (Exception e) {
                    System.err.println("Something wrong: " + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            });
        }
        try{
            List<Future<BookStatistics>> futures = executor.invokeAll(tasks);
            for (Future<BookStatistics> future : futures){
                try {
                    BookStatistics statistics = future.get();
                    if (statistics != null) {
                        synchronized (statisticsList){
                            statisticsList.add(statistics);
                        }
                    }
                } catch (ExecutionException e){
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();

        return new BookStatistics(statisticsList);
    }

    public void printStatToXmlFile(String filePath, String targetFieldName) {

        BookStatistics bookStatistics = aggregateStatistics(filePath, targetFieldName);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String fileName = String.format("statistics_by_%s.xml", targetFieldName);

        try{
            xmlMapper.writeValue(new File(".\\src\\main\\out\\" + fileName), bookStatistics);
            System.out.println("\nFile created successfully!");
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
