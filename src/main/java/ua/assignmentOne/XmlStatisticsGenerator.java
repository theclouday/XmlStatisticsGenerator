package ua.assignmentOne;

import ua.assignmentOne.service.BookStatisticsService;

/**
 * Generator of statistics in XML format based on data from JSON files.
 *
 * @author Maksym Filonov
 */

public class XmlStatisticsGenerator {
    /**
     * The entry point to the application for generating statistics in XML format.
     *
     * @param args Command line arguments. Two arguments are expected to be passed: the path to the directory with JSON files and the name of the field for statistics aggregation.
     */
    public static void main(String[] args) {

        BookStatisticsService bookStatisticsService = new BookStatisticsService(5);
        bookStatisticsService.generateStatisticsFromFiles(args[0], args[1]);

    }
}
