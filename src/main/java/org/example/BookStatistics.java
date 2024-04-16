package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect

public class BookStatistics {

    @JsonProperty("items")
    List<BookStatisticsItem> statisticsItemList = new ArrayList<>();

    public BookStatistics() {

    }

    public void incrementValue(String value) {
        for (BookStatisticsItem item : statisticsItemList) {
            if (item.getValue().equals(value)) {
                item.setCount(item.getCount()+1);
                return;
            }
        }

        statisticsItemList.add(new BookStatisticsItem(value));
    }

}
