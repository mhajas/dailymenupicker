package org.dailymenu.parser.manual;

import org.dailymenu.parser.manual.time.TimeUtils;

import java.util.Calendar;

public abstract class DailyMenuParser extends ParsingRestaurantGetter {

    protected int getWeekNumber() {
        Calendar cal = TimeUtils.getCalendar();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

}
