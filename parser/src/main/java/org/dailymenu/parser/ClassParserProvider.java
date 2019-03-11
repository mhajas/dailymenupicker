package org.dailymenu.parser;

/**
 * This type uses class for each restaurant
 */
public interface ClassParserProvider extends ParserProvider {

    /**
     * Each provider can parse only one restaurant with googleId specified by this method
     * @return googleId
     */
    String getGoogleId();
}
