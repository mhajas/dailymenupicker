package org.dailymenu.parser.menicka;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.dailymenu.entity.food.Restaurant;
import org.dailymenu.entity.food.RestaurantWeekData;
import org.dailymenu.entity.food.WeekMenuBuilder;
import org.dailymenu.parser.ParserProvider;
import org.dailymenu.parser.manual.time.TimeUtils;
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
import java.util.Calendar;

public class MenickaCZParser implements ParserProvider {

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

    private final Restaurant restaurant;
    private final String menickaUrl;

    public MenickaCZParser(Restaurant restaurant, String menickaUrl) {
        this.restaurant = restaurant;
        this.menickaUrl = menickaUrl;
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
        Calendar cal = TimeUtils.getCalendar();
        int day = cal.get(Calendar.DAY_OF_WEEK);

        Path cacheFilePAth = Path.of("menu-cache/" + getWeekNumber() + "/" + day +  "/" + restaurant.getGoogleId()).toAbsolutePath();

        if (!Files.exists(cacheFilePAth)) {
            Request request = new Request.Builder()
                    .url(menickaUrl)
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
        Calendar cal = TimeUtils.getCalendar();
        return cal.get(Calendar.WEEK_OF_YEAR) - 1;
    }

    private void parseDay(Element element, WeekMenuBuilder.DailyMenuBuilder dailyMenuBuilder) {
        if (element.text().contains("zavřeno")) return;

        element.select("div[class=nadpis]").stream() // Search for date
                .findFirst()
                .ifPresent(dateElement -> {
                    String dateText = dateElement.text().split(" ", 2)[1];

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
                    LocalDate date = LocalDate.parse(dateText, formatter);

                    dailyMenuBuilder.date(date);
                });

        element.select("li[class=polevka]")
                .stream()
                .forEach(itemElement -> {
                    String foodText = itemElement.select("div[class=polozka]").text();

                    dailyMenuBuilder.addSoup().name(foodText).buildFood();
                });

        element.select("li[class=jidlo]")
                .stream()
                .forEach(itemElement -> {
                    String foodText = itemElement.select("div[class=polozka]").text().replaceAll("^\\d\\. ", "");
                    String priceText = itemElement.select("div[class=cena]").text().replaceAll(" Kč$", "");
                    int price = 0;
                    try {
                        price = Integer.parseInt(priceText);
                    } catch (NumberFormatException ex) {
                        // ignore
                    }

                    dailyMenuBuilder.addMenu().name(foodText).price(price).buildFood();
                });

        if (dailyMenuBuilder.hasFood()) dailyMenuBuilder.buildDay();
    }

    private RestaurantWeekData parseImpl() throws IOException {
        String menu = getStringMenu();

        if (menu == null) return null;

        WeekMenuBuilder weekMenuBuilder = WeekMenuBuilder
                .create(restaurant)
                .weekNumber(getWeekNumber());

        Document document = Jsoup.parse(menu);

        Elements allMatchingElements = document.select("div[class=menicka]");

        allMatchingElements.forEach(p -> parseDay(p, weekMenuBuilder.addDayMenu()));
        return weekMenuBuilder.buildWeek();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new MenickaCZParser(new Restaurant("ChIJRcvQLAaUEkcRvZezuZ_I54M", "U Hrebicku", true), "https://www.menicka.cz/5349-u-hrebicku.html").parseImpl().toFormattedMenu());
    }
}
