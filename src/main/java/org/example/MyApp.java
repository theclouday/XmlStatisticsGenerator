package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;

public class MyApp
{
    private static final ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    public static void main(String[] args) throws IOException {
        GettingAllFiles wwl = new GettingAllFiles("D:\\Java Projects\\tesate\\statistik\\src\\main\\resources");

        CollectionOfStatistics collection = new CollectionOfStatistics(2);

        List<BookStatistics> result = collection.makeStatistics();

        for (BookStatistics statistics : result){
            System.out.println(objectMapper.writeValueAsString(statistics));
        }
    }
}
