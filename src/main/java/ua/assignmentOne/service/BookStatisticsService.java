package ua.assignmentOne.service;

import ua.assignmentOne.model.BookStatistics;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class "BookStatisticsService" contains methods for interacting with files (reading them, writing values to the file) and counting statistics.
 */

public class BookStatisticsService {
    private final ExecutorService executor;
    private static final int NUM_THREADS = 4;

    public BookStatisticsService() {
        this.executor = Executors.newFixedThreadPool(NUM_THREADS);
    }

    /**
     * Processes files in the specified directory and returns a list of their paths.
     *
     * @param filePath The path to the directory with the files.
     * @return fileNames as a list of file paths.
     */

    public static List<String> getFilesForProcessing(String filePath) {

        List<String> fileNames = new ArrayList<>();
        File folder = new File(filePath);

        try {
            if (folder.isDirectory()) {
                File[] files = folder.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        fileNames.add(file.getAbsolutePath());
                    } else {
                        System.err.println("File does not exist.");
                    }
                }
            } else {
                System.err.println("I did not find this path or it was written incorrectly.");
                return new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("Error:" + e.getMessage());
        }
        return fileNames;
    }

    /**
     * Processes data from a JSON file, collecting statistics on the given argument when the program starts.
     *
     * @param file            the JSON file we need for processing.
     * @param targetFieldName The name of the argument for which statistics are collected.
     * @return A BookStatistics object with statistics data.
     * @throws IOException Appears when a file read error occurs.
     */

    public BookStatistics processingDataFromFile(File file, String targetFieldName) throws IOException {
        System.out.println("Parsing file: " + file.getAbsolutePath());

        JsonFactory factory = new JsonFactory();
        BookStatistics statistics = new BookStatistics();

        try (JsonParser parser = factory.createParser(file)) {
            JsonToken token = null;
            while ((token = parser.nextToken()) != null) {
                if (JsonToken.FIELD_NAME.equals(token)) {
                    String fieldName = parser.currentName();
                    parser.nextToken();

                    if (targetFieldName.equals(fieldName)) {
                        String fieldValue = parser.getValueAsString();
                        List<String> valuesList = Arrays.asList(fieldValue.split(", "));

                        for (String value : valuesList) {
                            statistics.incrementValue(value);
                        }
                    }
                }
            }
        }
        return statistics;
    }

    /**
     * Aggregates statistics from several JSON files in the specified directory.
     *
     * @param filePath        Path to the directory with JSON files.
     * @param targetFieldName The name of the argument for which statistics are collected.
     * @return A BookStatistics object with aggregated statistics.
     */

    public BookStatistics aggregateStatistics(String filePath, String targetFieldName) {
        List<String> jsonFiles = getFilesForProcessing(filePath);

        List<BookStatistics> statisticsList = new ArrayList<>();
        List<Callable<BookStatistics>> tasks = new ArrayList<>();

        for (String fileName : jsonFiles) {
            tasks.add(() -> {
                try {
                    File file = new File(fileName);
                    return processingDataFromFile(file, targetFieldName);
                } catch (Exception e) {
                    System.err.println("Something wrong: " + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            });
        }
        try {
            List<Future<BookStatistics>> futures = executor.invokeAll(tasks);
            for (Future<BookStatistics> future : futures) {
                try {
                    BookStatistics statistics = future.get();
                    if (statistics != null) {
                        synchronized (statisticsList) {
                            statisticsList.add(statistics);
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new BookStatistics(statisticsList);
    }

    /**
     * Generates XML file with statistics data based on aggregated data from JSON files.
     *
     * @param filePath        Path to the directory with JSON files. Received when the user enters arguments when starting the program.
     * @param targetFieldName The name of the argument whose data will be aggregated and saved to an XML file. Received when the user enters arguments when starting the program.
     * @param fileOutputPath  Path to the directory where the html file containing statistics will be generated. Received when the user enters arguments when starting the program.
     */

    public void generateStatisticsFromFiles(String filePath, String targetFieldName, String fileOutputPath) {

        BookStatistics bookStatistics = aggregateStatistics(filePath, targetFieldName);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String fileName = String.format("statistics_by_%s.xml", targetFieldName);
        String resultOut = (fileOutputPath != null && !fileOutputPath.isEmpty() ? fileOutputPath : filePath);

        System.out.println(resultOut);
        try {
            xmlMapper.writeValue(new File(resultOut, fileName), bookStatistics);
            System.out.println("\nFile created successfully!");
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }
}
