package org.dailymenu.store;

import org.dailymenu.entity.Restaurant;

import java.util.List;

public interface RestaurantStore {

    Restaurant addRestaurant(Restaurant restaurant);

    Restaurant updateRestaurant(Restaurant restaurant);

    void deleteRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants();

}
