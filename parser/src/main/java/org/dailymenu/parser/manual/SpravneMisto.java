package org.dailymenu.parser.manual;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.dailymenu.entity.food.Restaurant;
import org.dailymenu.entity.food.RestaurantWeekData;
import org.dailymenu.entity.food.WeekMenuBuilder;
import org.dailymenu.parser.ClassParserProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpravneMisto implements ClassParserProvider {

    private enum DayNames {
        MOD("Pondělí"),
        TUE("Úterý"),
        WED("Středa"),
        THU("Čtvrtek"),
        FRI("Pátek")
        ;

        private final String dayName;

        DayNames(String dayName) {
            this.dayName = dayName;
        }

        public String getDayName() {
            return dayName;
        }
    }

    private final OkHttpClient client = new OkHttpClient();
    private final Restaurant restaurant = new Restaurant("ChIJh4L3zw6UEkcR4AKrgexX-5Y", "Spravne misto", true);

    @Override
    public String getGoogleId() {
        return restaurant.getGoogleId();
    }

    @Override
    public RestaurantWeekData parse() {

        try {
            return parseImpl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getStringMenu() {
        Path cacheFilePAth = Path.of("menu-cache/" + getWeekNumber() + "/" + getGoogleId()).toAbsolutePath();

        if (!Files.exists(cacheFilePAth)) {
            Request request = new Request.Builder()
                    .url("https://spravnemisto.cz/denni-menu/")
                    .build();

            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                File cacheFile = new File(String.valueOf(cacheFilePAth));
                cacheFile.getParentFile().mkdirs();
                cacheFile.createNewFile();

                BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile));
                writer.write(response.body().string());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        try {
            return Files.readString(cacheFilePAth, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected int getWeekNumber() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.WEEK_OF_YEAR) - 1;
    }

    private void itemToFood(Element item, WeekMenuBuilder.DailyMenuBuilder.FoodEntityBuilder foodBuilder) {
        String price = item.select("span[class=elementor-price-list-price]").text();
        if (price.toLowerCase(Locale.ROOT).contains("k")) {
            price = price.substring(0, price.length() - 3);
        }

        // Remove beginning from the menu name
        String foodName = item.select("p[class=elementor-price-list-description]").text();
        Matcher m = Pattern.compile("(?:(?:Menu č\\. )?\\d: )?(.*)").matcher(foodName);

        if (!m.find()) {
            return;
        }

        // Remove price from the end if exists
        foodName = m.group(1).replaceAll("\\d{1,4},-$", "").trim();
        if (foodName.isEmpty()) return;

        foodBuilder.name(foodName).price(Integer.valueOf(price)).buildFood();
    }

    private WeekMenuBuilder.DailyMenuBuilder parseDay(Element element, WeekMenuBuilder.DailyMenuBuilder dailyMenuBuilder) {
        element.select("h2[class=elementor-heading-title elementor-size-default]").stream() // Search for date
                .findFirst()
                .ifPresent(dateElement -> {
                    String dateText = dateElement.text().split(" ", 2)[1];

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy");
                    String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

                    dailyMenuBuilder.date(LocalDate.parse(dateText + " " + year, formatter));
                });

        ArrayList<Element> actualFoodElements = new ArrayList<>(element.select("div[data-widget_type=price-list.default]"));

        Element soupElements = actualFoodElements.get(0);
        soupElements.select("p[class=elementor-price-list-description]")
                .stream().forEach(soupElement -> {
                    String soupName = soupElement.text();
                    if (soupName.startsWith("Polévka (v ceně menu): ")) {
                        soupName = soupName.substring("Polévka (v ceně menu): ".length());
                    }

                    dailyMenuBuilder.addSoup().name(soupName).buildFood();
                });

        Element foodElements = actualFoodElements.get(1);

        foodElements.select("li").forEach(item -> itemToFood(item, dailyMenuBuilder.addMenu()));

        return dailyMenuBuilder;
    }

    private WeekMenuBuilder.DailyMenuBuilder addWeekMenu(Element element, WeekMenuBuilder.DailyMenuBuilder dailyMenuBuilder) {
        itemToFood(element, dailyMenuBuilder.addMenu());
        return dailyMenuBuilder;
    }

    private RestaurantWeekData parseImpl() throws IOException {
        String menu = getStringMenu();
        if (menu == null) return null;

        WeekMenuBuilder weekMenuBuilder = WeekMenuBuilder
                .create(restaurant)
                .weekNumber(getWeekNumber());

        Document document = Jsoup.parse(menu);

        Elements allMatchingElements = document.select("div[class=elementor-column-wrap elementor-element-populated]");
        Element weekElement = allMatchingElements.stream().filter(element -> element.text().startsWith("Týdenní menu")).findFirst().orElse(null);

        allMatchingElements
            .stream()
            .filter(element -> Arrays.stream(DayNames.values()).anyMatch(dayName -> element.text().startsWith(dayName.getDayName())))
                    .forEach(element -> {
                        WeekMenuBuilder.DailyMenuBuilder dailyMenuBuilder = parseDay(element, weekMenuBuilder.addDayMenu());
                        addWeekMenu(weekElement, dailyMenuBuilder).buildDay();
                    });

        return weekMenuBuilder.buildWeek();
    }
}
