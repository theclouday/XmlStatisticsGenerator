package ua.assignmentOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@JsonAutoDetect
public class BookStatisticsItem {
    private String value;
    private int count = 1;

    public BookStatisticsItem(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
