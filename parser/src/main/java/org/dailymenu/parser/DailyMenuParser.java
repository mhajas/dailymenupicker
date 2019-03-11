package org.dailymenu.parser;

import org.dailymenu.parser.ParsingRestaurantGetter;

import java.util.Calendar;

public abstract class DailyMenuParser extends ParsingRestaurantGetter {

    protected int getWeekNumber() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

}
