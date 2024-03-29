package org.dailymenu.entity.food;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

public class RestaurantWeekData {

    private int weekNumber;

    private Restaurant restaurant;

    private List<RestaurantDailyData> menuForDays = new ArrayList<>();

    @JsonSerialize
    public boolean isSoupIncludedInPrice() {
        return restaurant.isSoupIncludedInPrice();
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public List<RestaurantDailyData> getMenuForDays() {
        return menuForDays;
    }

    public void setMenuForDays(List<RestaurantDailyData> menuForDays) {
        this.menuForDays = menuForDays;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void addMenuForDay(RestaurantDailyData dailyData) {
        this.menuForDays.add(dailyData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantWeekData)) return false;

        RestaurantWeekData that = (RestaurantWeekData) o;

        if (isSoupIncludedInPrice() != that.isSoupIncludedInPrice()) return false;
        if (getWeekNumber() != that.getWeekNumber()) return false;
        return getRestaurant() != null ? getRestaurant().equals(that.getRestaurant()) : that.getRestaurant() == null;
    }

    @Override
    public int hashCode() {
        int result = (isSoupIncludedInPrice() ? 1 : 0);
        result = 31 * result + getWeekNumber();
        result = 31 * result + (getRestaurant() != null ? getRestaurant().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RestaurantWeekData{" +
                "soupIncludedInPrice=" + restaurant.isSoupIncludedInPrice() +
                ", weekNumber=" + weekNumber +
                ", restaurant=" + ((restaurant==null) ? null : restaurant.getName()) +
                ", menuForDays=" + menuForDays +
                '}';
    }

    public String toFormattedMenu() {
        StringBuilder b = new StringBuilder();

        b.append("Menu for restaurant: ").append(restaurant.getName()).append("\n")
        .append("Week number: ").append(weekNumber).append('\n');

        for (RestaurantDailyData dailyData : getMenuForDays()) {
            b.append(dailyData.getFormattedMenu()).append('\n');
        }

        return b.toString();
    }
}
