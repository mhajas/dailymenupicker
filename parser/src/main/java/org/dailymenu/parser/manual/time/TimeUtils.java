package org.dailymenu.parser.manual.time;

import java.util.Calendar;
import java.util.TimeZone;

public class TimeUtils {

    public static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    }
}
