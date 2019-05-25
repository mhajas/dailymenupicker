package org.dailymenu.parser;

/**
 * Interface for factories which is able to create provider for restaurant specified by googleId
 *
 */
public interface ParserProviderFactory extends Comparable<ParserProviderFactory> {

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

    /**
     * @return priority of ProviderFactory, higher priority means the provider will be chosen in case of more possible providers
     */
    int priority();

    @Override
    default int compareTo(ParserProviderFactory o) {
        return Integer.compare(o.priority(), priority());
    }
}
