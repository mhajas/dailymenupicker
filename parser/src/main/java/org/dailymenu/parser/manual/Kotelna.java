package org.dailymenu.parser.manual;

import org.dailymenu.entity.food.Restaurant;

/**
 * Servlet implementation class Kotelna
 */
@SuppressWarnings("serial")
public class Kotelna extends ZomatoRestaurantGetter {

    private Restaurant restaurant = new Restaurant("ChIJrdw1hgCUEkcRRPnwnBsXDfc", "U Kotelny", true);

    @Override
    protected String getZomatoId() {
        return "16506016";
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
