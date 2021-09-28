package org.dailymenu.entity.food;

import java.util.EnumSet;
import java.util.Set;

public class FoodEntity {

    private String name;

    private Integer price;

    private Set<Ingredient> tags = EnumSet.noneOf(Ingredient.class);

    public FoodEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Set<Ingredient> getTags() {
        return tags;
    }

    public void setTags(Set<Ingredient> tags) {
        this.tags.addAll(tags);
    }

    public void addTag(Ingredient tag) {
        this.tags.add(tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodEntity)) return false;

        FoodEntity that = (FoodEntity) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getPrice() != null ? !getPrice().equals(that.getPrice()) : that.getPrice() != null) return false;
        return getTags() != null ? getTags().equals(that.getTags()) : that.getTags() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FoodEntity{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", tags=" + tags +
                '}';
    }

    public String getFormattedMenu() {

        return name + ((price == null || price == 0) ? "" : " " + price + " CZK");
    }
}
