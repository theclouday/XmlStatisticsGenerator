package ua.assignmentOne;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
@JacksonXmlRootElement(localName = "statistics")
@JsonPropertyOrder({"value", "count"})
public class BookStatistics {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    List<BookStatisticsItem> statisticsItemList = new ArrayList<>();

    public BookStatistics() {

    }

    public BookStatistics(List<BookStatistics> bookStatisticsList) {
        mergeStatistics(bookStatisticsList);
    }

    public List<BookStatisticsItem> getStatisticsItemList() {
        return statisticsItemList;
    }

    public void setStatisticsItemList(List<BookStatisticsItem> statisticsItemList) {
        this.statisticsItemList = statisticsItemList;
    }

    public void incrementValue(String value) {
        incrementValue(value, 1);
    }
    public void incrementValue(String value, Integer count) {
        for (BookStatisticsItem item : statisticsItemList) {
            if (item.getValue().equals(value)) {
                item.setCount(item.getCount() + count);
                return;
            }
        }
        statisticsItemList.add(new BookStatisticsItem(value));
    }

    public void mergeStatistics(List<BookStatistics> statsList) {
        for (BookStatistics stats : statsList) {
            for (BookStatisticsItem statsItem : stats.getStatisticsItemList()) {
                incrementValue(statsItem.getValue(), statsItem.getCount());
            }
        }
    }

}
