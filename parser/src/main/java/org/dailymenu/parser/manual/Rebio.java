package org.dailymenu.parser.manual;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.dailymenu.entity.food.FoodEntity;
import org.dailymenu.entity.food.Restaurant;
import org.dailymenu.entity.food.RestaurantDailyData;
import org.dailymenu.entity.food.RestaurantWeekData;
import org.dailymenu.parser.manual.time.TimeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet implementation class Rebio
 */
public class Rebio extends ParsingRestaurantGetter {

    private Restaurant restaurant = new Restaurant("ChIJvexza6qWEkcR3_4XtfpanIU", "Rebio", true);

    protected String getUrl() {
        String pdfUrl = "";
        try {
            URL pageUrl = new URL("http://www.rebio.cz/Rebio-Park/Nase-nabidka/gn-ha.folder.aspx");
            BufferedReader is = new BufferedReader(new InputStreamReader((InputStream) pageUrl.getContent(), getCharset()));
            String previousLine = "";
            String line;
            while ((line = is.readLine()) != null) {
                if (line.contains("Jídelní lístek Rebio Park")) {
                    String search = previousLine + line;
                    pdfUrl = "http://www.rebio.cz/Rebio-Park/Nase-nabidka/" + search.replaceAll(".*href=\"([^\"]*)\".*", "$1");
                    break;
                }
                previousLine = line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return pdfUrl;
    }

    protected String[] getDays() {
        return new String[]{"Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek", "Informace o alergenech", "Neděle"};
    }

    protected String getDayOpeningTag() {
        return "";
    }

    protected String getDayClosingTag() {
        return "";
    }

    protected String getFreshMenuHTML() throws ParseException {
        try {
            StringBuffer result = new StringBuffer();
            URL url = new URL(getUrl());
            PDFParser parser = new PDFParser(new RandomAccessBuffer((InputStream) url.getContent()));
            parser.parse();
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            try (PDDocument pdDoc = new PDDocument(parser.getDocument())) {
                String parsedText = stripper.getText(pdDoc);
                boolean wasEmptyLine = true;
                for (String line : parseHTML(parsedText).split("\n")) {
                    if (!line.matches("^ *(Saláty, dezerty|Obsahuje Basic menu|Informace o alergenech).*")) {
                        line = line.trim();
                        if (line.length() > 0 || !wasEmptyLine) {
                            result.append(line.trim() + "\n");
                        }
                        wasEmptyLine = (line.length() == 0);
                    }
                }
            }
            return "<pre>" + result.toString() + "</pre>";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getGoogleId() {
        return restaurant.getGoogleId();
    }

    private int getWeekNumber() {
        Calendar cal = TimeUtils.getCalendar();
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

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
        restaurantWeekData.setRestaurant(restaurant);
        //restaurantWeekData.setSoupIncludedInPrice(restaurant.isSoupIncludedInPrice());
        restaurantWeekData.setWeekNumber(getWeekNumber());

        List<String> split = Arrays.asList(menuString.split("\n"));
        RestaurantDailyData dailyData = new RestaurantDailyData();

        String soupName = compileAndGet("Polévka: (.*)", split.get(1));
        FoodEntity soup = new FoodEntity();
        soup.setName(soupName);
        dailyData.addSoupToDay(soup);

        String basicMenuPrice = compileAndGet("Basic menu (\\d+)", split.get(0));
        String salad = compileAndGet("^Salát: (.*)", split.get(4));
        String menu1 = compileAndGet("^Menu I: (.*)", split.get(2)) + ", Salát: " + salad;
        String menu2 = compileAndGet("^Menu II: (.*)", split.get(3)) + ", Salát: " + salad;

        if (basicMenuPrice != null) {
            FoodEntity menu1Basic = new FoodEntity();
            menu1Basic.setPrice(Integer.valueOf(basicMenuPrice));
            menu1Basic.setName(menu1);
            dailyData.addMenuToDay(menu1Basic);

            FoodEntity menu2Basic = new FoodEntity();
            menu2Basic.setPrice(Integer.valueOf(basicMenuPrice));
            menu2Basic.setName(menu2);
            dailyData.addMenuToDay(menu2Basic);
        }

        String rebioMenuPrice = compileAndGet("Rebio menu (\\d+)", split.get(5));
        if (rebioMenuPrice != null) {
            String speciality = compileAndGet("Specialita: (.*)", split.get(6));
            String desert = compileAndGet("Dezert: (.*)", split.get(7));

            FoodEntity menu1Rebio = new FoodEntity();
            String rebioMenu1 = menu1 + ", Specialita: " + speciality + ", Dezert: " + desert;
            menu1Rebio.setPrice(Integer.valueOf(rebioMenuPrice));
            menu1Rebio.setName(rebioMenu1);
            dailyData.addMenuToDay(menu1Rebio);

            FoodEntity menu2Rebio = new FoodEntity();
            String rebioMenu2 = menu2 + ", Specialita: " + speciality + ", Dezert: " + desert;
            menu2Rebio.setPrice(Integer.valueOf(rebioMenuPrice));
            menu2Rebio.setName(rebioMenu2);
            dailyData.addMenuToDay(menu2Rebio);
        }

        for (int i = 0; i < 5; i++) {
            if (i == TimeUtils.getCalendar().get(Calendar.DAY_OF_WEEK) - 2) {
                restaurantWeekData.addMenuForDay(dailyData);
            } else {
                restaurantWeekData.addMenuForDay(null);
            }
        }

        return restaurantWeekData;
    }
}

