package org.dailymenu.parser.util;

public enum NodeType {
    EMPTY_NODE(null),
    FULL_DOCUMENT_NODE(String.class),
    WHOLE_MENU_NODE(String.class),
    DAY_MENU_NODE(String.class),
    FULL_FOOD_NODE(String.class),
    SOUP_NODE(String.class),
    FOOD_NODE(String.class),
    PRICE_NODE(Integer.class);

    private Class<?> clazz;

    NodeType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
