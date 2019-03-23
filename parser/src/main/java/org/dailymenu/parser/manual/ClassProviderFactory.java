package org.dailymenu.parser.manual;

import org.dailymenu.parser.ClassParserProvider;
import org.dailymenu.parser.ParserProvider;
import org.dailymenu.parser.ParserProviderFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ClassProviderFactory implements ParserProviderFactory {

    private static ServiceLoader<ClassParserProvider> loader = ServiceLoader
            .load(ClassParserProvider.class);

    private static Map<String, ClassParserProvider> providers = new HashMap<>();

    @Override
    public void init() {
        loader.iterator().forEachRemaining(provider -> providers.put(provider.getGoogleId(), provider));
    }

    @Override
    public ParserProvider create(String googleId) {
        return providers.get(googleId);
    }

    @Override
    public boolean canProvide(String googleId) {
        return providers.containsKey(googleId);
    }
}
