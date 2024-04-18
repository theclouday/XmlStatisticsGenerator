package ua.assignmentOne;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.assignmentOne.model.BookStatistics;
import ua.assignmentOne.model.BookStatisticsItem;
import ua.assignmentOne.service.BookStatisticsService;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class BookStatisticsServiceTest {
    private BookStatisticsService bookStatisticsService;

    @BeforeEach
    void prepare() {
        bookStatisticsService = new BookStatisticsService();
    }

    @Test
    void processingDataFromFileTest() throws IOException {
        File file = new File(".\\src\\test\\resources\\file_1.json");
        String targetFieldName = "author";

        BookStatistics bookStatistics = bookStatisticsService.processingDataFromFile(file, targetFieldName);
        List<BookStatisticsItem> itemList = bookStatistics.getStatisticsItemList();

        assertNotNull(itemList);
        assertEquals("Lewis Carroll", itemList.get(0).getValue());
        assertEquals(1, itemList.get(0).getCount());

        assertEquals("David Foster Wallace", itemList.get(1).getValue());
        assertEquals(1, itemList.get(1).getCount());

        assertEquals("Dylan Thomas", itemList.get(2).getValue());
        assertEquals(2, itemList.get(2).getCount());

        assertEquals("Geoffrey Chaucer", itemList.get(3).getValue());
        assertEquals(1, itemList.get(3).getCount());

        assertEquals("Tom Wolfe", itemList.get(4).getValue());
        assertEquals(1, itemList.get(4).getCount());

        assertEquals("Mark Twain", itemList.get(5).getValue());
        assertEquals(1, itemList.get(5).getCount());

        assertEquals("D.H. Lawrence", itemList.get(6).getValue());
        assertEquals(1, itemList.get(6).getCount());

        assertEquals("George Eliot", itemList.get(7).getValue());
        assertEquals(2, itemList.get(7).getCount());

        assertEquals("J.D. Vance", itemList.get(8).getValue());
        assertEquals(1, itemList.get(8).getCount());

        assertEquals("H.G. Wells", itemList.get(9).getValue());
        assertEquals(1, itemList.get(9).getCount());
    }

    @Test
    void getFilesForProcessingInvalidPathTest() {
        String invalidPath = ".\\src\\test\\resources\\file_2.json";

        List<String> fileNames = BookStatisticsService.getFilesForProcessing(invalidPath);

        assertNotNull(fileNames);
    }

}
