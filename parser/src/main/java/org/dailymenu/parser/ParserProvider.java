package org.dailymenu.parser;

import org.dailymenu.entity.food.RestaurantWeekData;

/**
 * Classes implementing this interface provide parse functionality for restaurant
 */
public interface ParserProvider {

    /**
     * Function which perform parsing
     * @return Food menu for whole week
     */
    RestaurantWeekData parse();

}
