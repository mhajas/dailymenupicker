package org.dailymenu.parser.manual;


import org.dailymenu.entity.food.FoodEntity;
import org.dailymenu.entity.food.Restaurant;
import org.dailymenu.entity.food.RestaurantDailyData;
import org.dailymenu.entity.food.RestaurantWeekData;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Servlet implementation class Purkynka
 */
public class Purkynka extends DailyMenuParser {

    private static Pattern FOOD_PATTERN = Pattern.compile("(.*) (\\d+)");

    private Restaurant restaurant = new Restaurant("ChIJD55B_ASUEkcRgoFJeL_VJjE", "Purkyňka", true);

    protected String getUrl() {
        return "http://www.napurkynce.cz/denni-menu/";
    }

    @Override
    protected boolean includingDayName() {
        return true;
    }

    @Override
    protected String getDayOpeningTag() {
        return "";
    }

    @Override
    protected String getDayClosingTag() {
        return "";
    }

    @Override
    protected String[] getDays() {
        return new String[]{"Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek", "</pre>", "Neděle"};
    }

    @Override
    public String getGoogleId() {
        return restaurant.getGoogleId();
    }

    @Override
    public RestaurantWeekData parse() {
        String menuString = parseString();

        RestaurantWeekData restaurantWeekData = new RestaurantWeekData();
        restaurantWeekData.setRestaurant(restaurant);
        //restaurantWeekData.setSoupIncludedInPrice(restaurant.isSoupIncludedInPrice());
        restaurantWeekData.setWeekNumber(getWeekNumber());

        List<String> split = Arrays.asList(menuString.split("</div>"));

        split = split.stream()
                .map(s -> s.replaceAll("&nbsp;", ""))
                .map(s -> s.replaceAll("<div>", ""))
                .map(s -> s.replaceAll("^.*[:\\.] ", ""))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        FoodEntity soup = new FoodEntity();
        soup.setName(split.get(0));
        split.remove(0);

        RestaurantDailyData dailyData = new RestaurantDailyData();

        dailyData.addSoupToDay(soup);

        split.forEach(s -> {
                    Matcher m = FOOD_PATTERN.matcher(s);
                    if (m.find() && m.groupCount() == 2) {
                        FoodEntity f = new FoodEntity();
                        f.setName(m.group(1));
                        f.setPrice(Integer.valueOf(m.group(2)));

                        dailyData.addMenuToDay(f);
                    }
                });

        for (int i = 0; i < 5; i++) {
            if (i == Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2) {
                restaurantWeekData.addMenuForDay(dailyData);
            } else {
                restaurantWeekData.addMenuForDay(null);
            }
        }

        return restaurantWeekData;
    }
}