package org.dailymenu.entity.food;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RestaurantDailyData {

    private Date dateOfMenu;

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

    public Date getDateOfMenu() {
        return dateOfMenu;
    }

    public void setDateOfMenu(Date dateOfMenu) {
        this.dateOfMenu = dateOfMenu;
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

    public String getFormattedMenu() {
        StringBuilder b = new StringBuilder();

        b.append("Menu for day: ").append(dateOfMenu).append("\n");
        b.append("Soups:").append('\n');

        for (FoodEntity f : getSoup()) {
            b.append(f.getFormattedMenu()).append('\n');
        }

        b.append("Food:").append('\n');
        for (FoodEntity f : getMenu()) {
            b.append(f.getFormattedMenu()).append('\n');
        }

        return b.toString();
    }
}
