package org.dailymenu.parser.tree;

import org.dailymenu.parser.ParserProvider;
import org.dailymenu.parser.ParserProviderFactory;
import org.dailymenu.parser.TreeParserProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class TreeProviderFactory implements ParserProviderFactory {

    private static ServiceLoader<TreeParserProvider> loader = ServiceLoader
            .load(TreeParserProvider.class);

    private static Map<String, TreeParserProvider> providers = new HashMap<>();

    @Override
    public void init() {
        loader.iterator().forEachRemaining(provider -> providers.put(provider.getRestaurant().getGoogleId(), provider));
    }

    @Override
    public ParserProvider create(String googleId) {
        return providers.get(googleId);
    }

    @Override
    public boolean canProvide(String googleId) {
        return providers.containsKey(googleId);
    }

    @Override
    public int priority() {
        return 100;
    }
}
