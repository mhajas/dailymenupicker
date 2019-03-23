package org.dailymenu.parser.manual;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;


/*
 * [beginIndex]<dayOpeningTag>
 *   <p>[todayIndex]pondeli</p>
 *   <ul>
 *     <li>1st meal</li>
 *     <li>2nd meal</li>
 *   </ul>
 * </dayClosingTag>[endIndex]
 * <div>
 *   <p>[tomorrowIndex]pondeli</p>
 *   ...
 */
public abstract class ParsingRestaurantGetter extends RestaurantGetter {

    public ParsingRestaurantGetter() {
        super();
    }

    protected String parseHTML(String freshMenuHTML) throws ParseException {
        try {
            String result = freshMenuHTML;
            int beginIndex = getBeginIndex(result);
            System.out.println("Begin index: " + beginIndex);
            int endIndex = getEndIndex(beginIndex, result);
            System.out.println("end index: " + endIndex);
            result = result.substring(beginIndex, endIndex);
            return result;
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException(e.getMessage(), 0);
        }
    }

    protected int getEndIndex(int beginIndex, String html) {
        int tomorrowIndex = getTomorrowIndex(beginIndex, html);
        return html.lastIndexOf(getDayClosingTag(), tomorrowIndex) + getDayClosingTag().length();
    }

    protected int getBeginIndex(String result) {
        if (includingDayName()) {
            System.out.println("Getting last index of " + getDayOpeningTag() + " from index " + getTodayIndex(result) );
            return result.lastIndexOf(getDayOpeningTag(), getTodayIndex(result));
        } else {
            System.out.println("Getting index of " + getDayOpeningTag() + " from index " + getTodayIndex(result) );
            return result.indexOf(getDayOpeningTag(), getTodayIndex(result));
        }
    }

    protected boolean includingDayName() {
        return false;
    }

    protected String getDayOpeningTag() {
        return "<div";
    }

    protected String getDayClosingTag() {
        return "</div>";
    }

    protected String[] getDays() {
        return new String[]{"Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek", "Sobota", "Neděle"};
    }

    protected int getDayOfWeek() {
        return new Date().getDay() - 1;
    }

    protected String getToday() {
        int dayOfWeek = getDayOfWeek();
        return getDays()[dayOfWeek > 4 ? 4 : dayOfWeek];
    }

    protected int getTodayIndex(String html) {
        return getLastIndex(html, getToday());
    }

    protected String getTomorrow() {
        int dayOfWeek = getDayOfWeek() + 1;
        return getDays()[dayOfWeek > 5 ? 5 : dayOfWeek];
    }

    protected int getTomorrowIndex(int beginIndex, String html) {
        return getFirstIndex(html.substring(beginIndex), getTomorrow()) + beginIndex;
    }

    private int getFirstIndex(String html, String substring) {
        return html.toLowerCase().indexOf(substring.toLowerCase());
    }

    private int getLastIndex(String html, String substring) {
        System.out.println("Getting last index of: " + substring.toLowerCase() + " from text");

        System.out.println("----------------------");
        System.out.println(html.toLowerCase());
        System.out.println("----------------------");
        return html.toLowerCase().lastIndexOf(substring.toLowerCase());
    }

    @Override
    protected String getFreshMenuHTML() throws IOException, ParseException {
        return parseHTML(super.getFreshMenuHTML());
    }
}