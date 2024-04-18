package ua.assignmentOne.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to store statistical data of books.
 */

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

    /**
     * An overloaded method in which we increase the value for the specified statistic element by 1.
     *
     * @param value Value for magnification.
     */

    public void incrementValue(String value) {
        incrementValue(value, 1);
    }

    /**
     * Increase the value for the specified statistic item by the specified amount.
     *
     * @param value Value for magnification.
     * @param count The amount by which to increase the value.
     */

    public void incrementValue(String value, Integer count) {
        for (BookStatisticsItem item : statisticsItemList) {
            if (item.getValue().equals(value)) {
                item.setCount(item.getCount() + count);
                return;
            }
        }
        statisticsItemList.add(new BookStatisticsItem(value));
    }

    /**
     * Merge statistics from multiple lists of BookStatistics objects.
     *
     * @param statsList A list of BookStatistics objects for aggregating statistics.
     */

    public void mergeStatistics(List<BookStatistics> statsList) {
        for (BookStatistics stats : statsList) {
            for (BookStatisticsItem statsItem : stats.getStatisticsItemList()) {
                incrementValue(statsItem.getValue(), statsItem.getCount());
            }
        }
    }

}
