package org.dailymenu.entity.food;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

public class WeekMenuBuilder {
    private final RestaurantWeekData weekData = new RestaurantWeekData();

    public static WeekMenuBuilder create(Restaurant restaurant) {
        return new WeekMenuBuilder(restaurant);
    }

    private WeekMenuBuilder(Restaurant restaurant) {
        weekData.setRestaurant(restaurant);
    }

    public WeekMenuBuilder weekNumber(int weekNumber) {
        weekData.setWeekNumber(weekNumber);
        return this;
    }

    public DailyMenuBuilder addDayMenu() {
        return new DailyMenuBuilder();
    }

    public RestaurantWeekData buildWeek() {
        return weekData;
    }

    public class DailyMenuBuilder {

        private final RestaurantDailyData day = new RestaurantDailyData();

        public DailyMenuBuilder date(Date d) {
            day.setDateOfMenu(d);
            return this;
        }

        public boolean hasFood() {
            return day.getMenu().size() > 0;
        }

        public DailyMenuBuilder date(LocalDate localDate) {
                day.setDateOfMenu(java.util.Date.from(localDate.atStartOfDay(ZoneId.of("GMT")).toInstant()));

                return this;
        }

        public FoodEntityBuilder addSoup() {
            return new FoodEntityBuilder(day::addSoupToDay);
        }

        public FoodEntityBuilder addMenu() {
            return new FoodEntityBuilder(day::addMenuToDay);
        }

        public WeekMenuBuilder buildDay() {
            weekData.addMenuForDay(day);

            return WeekMenuBuilder.this;
        }

        public class FoodEntityBuilder {
            private final Consumer<FoodEntity> resultingEntityConsumer;
            private final FoodEntity food = new FoodEntity();

            public FoodEntityBuilder(Consumer<FoodEntity> resultingEntityConsumer) {
                this.resultingEntityConsumer = resultingEntityConsumer;
            }

            public FoodEntityBuilder name(String n) {
                food.setName(n);
                return this;
            }

            public FoodEntityBuilder price(Integer price) {
                food.setPrice(price);
                return this;
            }

            public FoodEntityBuilder tag(Ingredient... tags) {
                Arrays.stream(tags).forEach(food::addTag);
                return this;
            }

            public DailyMenuBuilder buildFood() {
                resultingEntityConsumer.accept(food);
                return DailyMenuBuilder.this;
            }
        }
    }
}
