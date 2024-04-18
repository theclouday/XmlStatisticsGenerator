package ua.assignmentOne;

import ua.assignmentOne.service.BookService;


public class Script
{
    public static void main(String[] args) {

        BookService bookService = new BookService(5);
        bookService.printStatToXmlFile(args[0], args[1]);

    }
}
