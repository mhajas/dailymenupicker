package org.dailymenu.parser.manual;

import java.util.Calendar;

public abstract class DailyMenuParser extends ParsingRestaurantGetter {

    protected int getWeekNumber() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

}
