package ua.assignmentOne;


public class Script
{
    public static void main(String[] args) {

        CreatingFileWithStatistics cr = new CreatingFileWithStatistics();
        cr.marshallingToXML(args[0], args[1]);

    }
}
