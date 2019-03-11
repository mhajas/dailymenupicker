package org.dailymenu.jpa;

import org.dailymenu.Configuration;
import org.dailymenu.entity.Restaurant;
import org.dailymenu.store.RestaurantStore;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class JPARestaurantStoreTest {

    @Deployment
    public static WebArchive createDeployment() {
        return Configuration.deployment();
    }

    @Inject
    protected RestaurantStore restaurantStore;

    @PersistenceContext
    private EntityManager manager;


    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void addRestaurantTest(){
        Restaurant r = new Restaurant();
        r.setName("Purkynka");



        restaurantStore.addRestaurant(r);

        List<Restaurant> allRestaurants = restaurantStore.getAllRestaurants();

        assertThat(allRestaurants).hasSize(1);

        System.out.println(allRestaurants.get(0).getId());
        System.out.println(allRestaurants.get(0).getName());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void addRestaurant2Test(){
        Restaurant r = new Restaurant();
        r.setName("Purkynka2");

        restaurantStore.addRestaurant(r);

        List<Restaurant> allRestaurants = restaurantStore.getAllRestaurants();

        assertThat(allRestaurants).hasSize(1);

        System.out.println(allRestaurants.get(0).getId());
        System.out.println(allRestaurants.get(0).getName());
    }
}
