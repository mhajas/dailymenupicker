package org.dailymenu.jpa;

import org.dailymenu.entity.Restaurant;
import org.dailymenu.store.RestaurantStore;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class JPARestaurantStore implements RestaurantStore {

    private static final Logger LOGGER = Logger.getLogger( JPARestaurantStore.class.getName() );

    @PersistenceContext(unitName = "ParserManager")
    private EntityManager em;

    @Override
    @Transactional
    public Restaurant addRestaurant(Restaurant restaurant){
        em.persist(restaurant);
        em.flush();
        return restaurant;
    }

    @Override
    @Transactional
    public Restaurant updateRestaurant(Restaurant restaurant) {
        em.merge(restaurant);
        em.flush();
        return restaurant;
    }

    @Override
    @Transactional
    public void deleteRestaurant(Restaurant restaurant) {
        em.remove(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        Query query = em.createQuery("SELECT r FROM Restaurant r");
        return (List<Restaurant>) query.getResultList();
    }

}
