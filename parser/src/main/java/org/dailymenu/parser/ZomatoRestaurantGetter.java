package org.dailymenu.parser;

import org.dailymenu.entity.food.FoodEntity;
import org.dailymenu.entity.food.Restaurant;
import org.dailymenu.entity.food.RestaurantDailyData;
import org.dailymenu.entity.food.RestaurantWeekData;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class ZomatoRestaurantGetter extends RestaurantGetter {

    private static Pattern FOOD_PATTERN = Pattern.compile("<td>\\d?\\.?[abc]?\\)?(.*)</td>.*<td>(\\d+)");

    @Override
    protected String getUrl() {
        return "https://developers.zomato.com/api/v2.1/dailymenu?res_id=" + getZomatoId();
    }

    protected abstract String getZomatoId();

    @Override
    protected URLConnection getConnection() throws IOException {
        URLConnection connection = super.getConnection();
        connection.addRequestProperty("accept-language", "cs");
        connection.addRequestProperty("accept-encoding", "br");
        connection.addRequestProperty("user-key", getZomatoAPIKey());
        return connection;
    }

    protected String getZomatoAPIKey() {
        return System.getenv("ZOMATO_API_KEY");
    }

    @Override
    protected String getFreshMenuHTML() throws IOException, ParseException {
        StringBuffer sb = new StringBuffer();
        URLConnection connection = getConnection();
        JsonReader rdr = Json.createReader((InputStream) connection.getContent());
        JsonObject obj = rdr.readObject();
        List<JsonObject> menus = obj.getJsonArray("daily_menus").getValuesAs(JsonObject.class);
        if (!menus.isEmpty()) {
            JsonArray dishes = menus.get(0).getJsonObject("daily_menu").getJsonArray("dishes");
            if (dishes != null && !dishes.isEmpty()) {
                sb.append("<table>");
                for (JsonObject result : dishes.getValuesAs(JsonObject.class)) {
                    JsonObject dish = result.getJsonObject("dish");
                    sb.append("<tr><td>");
                    sb.append(dish.getString("name"));
                    sb.append("</td><td>");
                    sb.append(dish.getString("price"));
                    sb.append("</td></tr>");
                }
                sb.append("</table>");
            }
        }
        return stripImages() ? stripImages(sb.toString()) : sb.toString();
    }

    private int getWeekNumber() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    protected abstract Restaurant getRestaurant();

    private String compileAndGet(String regex, String text) {
        Matcher m = Pattern.compile(regex).matcher(text);

        if (m.find()) {
            return m.group(1);
        }

        return null;
    }

    @Override
    public RestaurantWeekData parse() {
        String menuString = parseString();

        RestaurantWeekData restaurantWeekData = new RestaurantWeekData();
        restaurantWeekData.setRestaurant(getRestaurant());
        restaurantWeekData.setSoupIncludedInPrice(getRestaurant().isSoupIncludedInPrice());
        restaurantWeekData.setWeekNumber(getWeekNumber());

        List<String> split = Arrays.asList(menuString.split("</tr>"));

        String soupName = compileAndGet("<td>([^<.]*)</td>", split.get(0));
        FoodEntity soup = new FoodEntity();
        soup.setName(soupName.trim());

        RestaurantDailyData dailyData = new RestaurantDailyData();
        dailyData.addSoupToDay(soup);

        split.forEach(s -> {
            Matcher m = FOOD_PATTERN.matcher(s);
            if (m.find() && m.groupCount() == 2) {
                FoodEntity f = new FoodEntity();
                f.setName(m.group(1).trim());
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
