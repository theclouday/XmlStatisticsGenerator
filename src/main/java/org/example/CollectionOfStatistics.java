package org.example;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.*;

import static org.example.GettingAllFiles.getFiles;

public class CollectionOfStatistics {
    private final ExecutorService executor;


    public CollectionOfStatistics(int numThreads){
        this.executor = Executors.newFixedThreadPool(numThreads);
    }


    public List<BookStatistics> makeStatistics(){
        List<String> jsonFiles = getFiles();

        List<BookStatistics> statisticsList = new ArrayList<>();
        List<Callable<BookStatistics>> tasks = new ArrayList<>();

        for(String fileName : jsonFiles) {
            tasks.add(() -> {
                try {
                    File file = new File(fileName);
                    return parseFiles(file, "year_published");
                }catch (Exception e) {
                    System.err.println("Something wrong " + e.getMessage());
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
        return statisticsList;
    }

    private BookStatistics parseFiles(File file, String targetFieldName) throws IOException {
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
                    statistics.incrementValue(parser.getValueAsString());
                }
            }
        }
        parser.close();
        return statistics;
    }
}
