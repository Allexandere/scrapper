package com.example.scrapper.component;

import javax.annotation.Nullable;
import java.util.Arrays;

public enum RuMonths {
    JANUARY("янв", 1),
    FEBRUARY("фев", 2),
    MARCH("мар", 3),
    APRIL("апр", 4),
    MAY("май", 5),
    JUNE("июн", 6),
    JULY("июл", 7),
    AUGUST("авг", 8),
    SEPTEMBER("сен", 9),
    OCTOBER("окт", 10),
    NOVEMBER("ноя", 11),
    DECEMBER("дек", 12);

    public String title;
    public int monthNumber;

    RuMonths(String title, int monthNumber) {
        this.title = title;
        this.monthNumber = monthNumber;
    }

    public static Integer getMonthNumberByTitle(String title) {
        for (RuMonths month : RuMonths.values()) {
            if (month.title.equals(title)) {
                return month.monthNumber;
            }
        }
        return null;
    }
}
