package org.dailymenu.parser.manual;

import org.dailymenu.entity.food.Restaurant;

/**
 * Servlet implementation class Liquid Bread
 */
public class LiquidBread extends ZomatoRestaurantGetter {

    private Restaurant restaurant = new Restaurant("ChIJ3VMlLwWUEkcRiEOim7E_5ks", "Liquid Bread", true);

    @Override
    protected String getZomatoId() {
    	return "18599936";
    }

    @Override
    protected Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public String getGoogleId() {
        return restaurant.getGoogleId();
    }
}
