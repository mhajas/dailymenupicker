package org.dailymenu.parser;

import org.dailymenu.entity.food.Restaurant;

/**
 * Servlet implementation class Portoriko
 */
public class Portoriko extends ZomatoRestaurantGetter {

    private Restaurant restaurant = new Restaurant("ChIJQ7t5NgeUEkcRfF9rW4VCrjA", "Portoriko", true);

    @Override
    protected String getZomatoId() {
        return "16506862";
    }

    @Override
    public String getGoogleId() {
        return restaurant.getGoogleId();
    }

    @Override
    protected Restaurant getRestaurant() {
        return restaurant;
    }
}
