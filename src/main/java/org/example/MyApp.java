package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MyApp
{
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static String[] args;

    public static void setArgs(String[] args) {
        MyApp.args = args;
    }

    public static String[] getArgs() {
        return args;
    }

    {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static void main(String[] args) throws IOException {


        String[] path = new String[]{"D:\\Java Projects\\tesate\\statistik\\src\\main\\resources"};
        setArgs(path);

        Arrays.asList(args).forEach(System.out::println);
        
        CollectionOfStatistics collection = new CollectionOfStatistics(5);

        List<BookStatistics> result = collection.makeStatistics();

        for (BookStatistics statistics : result){
            System.out.println(objectMapper.writeValueAsString(statistics));
        }
    }
}
