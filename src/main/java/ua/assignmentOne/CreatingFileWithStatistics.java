package ua.assignmentOne;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;

public class CreatingFileWithStatistics {


    public void marshallingToXML(String filePath, String targetFieldName) {

        CollectionOfStatistics collectionOfStatistics = new CollectionOfStatistics(5);

        BookStatistics bookStatistics = collectionOfStatistics.makeStatistics(filePath, targetFieldName);

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try{
            xmlMapper.writeValue(new File("D:\\Java Projects\\tesate\\statistik\\src\\main\\out\\library.xml"), bookStatistics);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        System.out.println("Completed");
    }
}
