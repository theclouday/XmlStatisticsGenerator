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

/**
 *This class "BookService" contains methods for interacting with files (reading them, writing values to the file) and counting statistics.
 *
 * @class BookService
 */

public class BookService {
    private final ExecutorService executor;

    /**
     * Class constructor specifies the number of threads for working with files.
     * @param numThreads   The number of threads in the pool.
     */

    public BookService(int numThreads) {
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    /**
     * Processes files in the specified directory and returns a list of their paths.
     * @param filePath The path to the directory with the files.
     * @return fileNames as a list of file paths.
     */

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

    /**
     * Processes data from a JSON file, collecting statistics on the given argument when the program starts.
     * @param file the JSON file we need for processing.
     * @param targetFieldName  The name of the argument for which statistics are collected.
     * @return A BookStatistics object with statistics data.
     * @throws IOException Appears when a file read error occurs.
     */

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

    /**
     * Aggregates statistics from several JSON files in the specified directory.
     * @param filePath Path to the directory with JSON files.
     * @param targetFieldName The name of the argument for which statistics are collected.
     * @return A BookStatistics object with aggregated statistics.
     */

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

    /**
     * Generates XML file with statistics data based on aggregated data from JSON files.
     * @param filePath Path to the directory with JSON files. Received when the user enters arguments when starting the program.
     * @param targetFieldName The name of the argument whose data will be aggregated and saved to an XML file. Received when the user enters arguments when starting the program.
     */

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
