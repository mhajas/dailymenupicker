package org.dailymenu.entity.food;

public class Restaurant {
    private String googleId;
    private String name;
    private boolean soupIncludedInPrice;

    public Restaurant() {
    }

    public Restaurant(String googleId, String name, boolean soupIncludedInPrice) {
        this.googleId = googleId;
        this.name = name;
        this.soupIncludedInPrice = soupIncludedInPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public boolean isSoupIncludedInPrice() {
        return soupIncludedInPrice;
    }

    public void setSoupIncludedInPrice(boolean soupIncludedInPrice) {
        this.soupIncludedInPrice = soupIncludedInPrice;
    }
}
