package org.dailymenu.parser.menicka;

import org.dailymenu.entity.food.Restaurant;
import org.dailymenu.parser.ParserProvider;
import org.dailymenu.parser.ParserProviderFactory;

import java.util.HashMap;
import java.util.Map;

public class MenickaCZParserProvider implements ParserProviderFactory {

    private static final Map<String, MenickaRestaurantEntry> menickaRestaurants = new HashMap<>();

    static {
        menickaRestaurants.put("ChIJD9jFvQ6UEkcR3MEv3LQ0Yn4", new MenickaRestaurantEntry(new Restaurant("ChIJD9jFvQ6UEkcR3MEv3LQ0Yn4", "Borgeska", true), "https://www.menicka.cz/4919-borgeska.html"));
        menickaRestaurants.put("ChIJRcvQLAaUEkcRvZezuZ_I54M", new MenickaRestaurantEntry(new Restaurant("ChIJRcvQLAaUEkcRvZezuZ_I54M", "U Hrebicku", true), "https://www.menicka.cz/5349-u-hrebicku.html"));
    }

    private static class MenickaRestaurantEntry {
        private final Restaurant restaurant;
        private final String url;

        private MenickaRestaurantEntry(Restaurant restaurant, String url) {
            this.restaurant = restaurant;
            this.url = url;
        }

        public Restaurant getRestaurant() {
            return restaurant;
        }

        public String getUrl() {
            return url;
        }
    }

    @Override
    public void init() {

    }

    @Override
    public ParserProvider create(String googleId) {
        if (menickaRestaurants.containsKey(googleId)) {
            MenickaRestaurantEntry e = menickaRestaurants.get(googleId);

            return new MenickaCZParser(e.restaurant, e.url);
        }

        return null;
    }

    @Override
    public boolean canProvide(String googleId) {
        return menickaRestaurants.containsKey(googleId);
    }

    @Override
    public int priority() {
        return 30;
    }
}
