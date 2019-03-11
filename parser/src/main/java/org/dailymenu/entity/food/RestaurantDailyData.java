package org.dailymenu.entity.food;

import java.util.HashSet;
import java.util.Set;

public class RestaurantDailyData {

    private Set<FoodEntity> menu = new HashSet<>();

    private Set<FoodEntity> soup = new HashSet<>();


    public Set<FoodEntity> getMenu() {
        return menu;
    }

    public void setMenu(Set<FoodEntity> menu) {
        this.menu = menu;
    }

    public Set<FoodEntity> getSoup() {
        return soup;
    }

    public void setSoup(Set<FoodEntity> soup) {
        this.soup = soup;
    }

    public void addMenuToDay(FoodEntity menu) {
        this.menu.add(menu);
    }

    public void addSoupToDay(FoodEntity soup) {
        this.soup.add(soup);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantDailyData)) return false;

        RestaurantDailyData that = (RestaurantDailyData) o;

        if (getMenu() != null ? !getMenu().equals(that.getMenu()) : that.getMenu() != null) return false;
        return getSoup() != null ? getSoup().equals(that.getSoup()) : that.getSoup() == null;
    }

    @Override
    public int hashCode() {
        int result = getMenu() != null ? getMenu().hashCode() : 0;
        result = 31 * result + (getSoup() != null ? getSoup().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RestaurantDailyData{" +
                "menu=" + menu +
                ", soup=" + soup +
                '}';
    }
}
