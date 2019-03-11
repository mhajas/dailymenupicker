package org.dailymenu.parser;

/**
 * Interface for factories which is able to create provider for restaurant specified by googleId
 *
 */
public interface ParserProviderFactory {

    /**
     * Init function is called when creating each ParserProviderFactory
     *
     * This function might serve as a loader for each ParserProviders for specific factory type
     */
    void init();

    /**
     *
     * @param googleId for specific restaurant
     * @return ParserProvider which is able to parse restaurant with @googleId or null if no provider was found
     */
    ParserProvider create(String googleId);

    /**
     *
     * @param googleId for specific restaurant
     * @return true if this factory can provide ParserProvider for @googleId, false otherwise
     */
    boolean canProvide(String googleId);
}
