package org.dailymenu.services.parser;

import org.dailymenu.parser.ParserProvider;
import org.dailymenu.parser.ParserProviderFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


@Named
@ApplicationScoped
public class MultiFactoryParserService implements ParserService {

    private static ServiceLoader<ParserProviderFactory> loader = ServiceLoader.load(ParserProviderFactory.class);

    private static List<ParserProviderFactory> factories = new ArrayList<>();

    public MultiFactoryParserService() {
        loader.iterator().forEachRemaining(parserProviderFactory -> {
            parserProviderFactory.init();
            factories.add(parserProviderFactory);
        });

        Collections.sort(factories);
    }

    @Override
    public ParserProvider getParserProvider(String googleId) {
        List<ParserProviderFactory> canProvide = factories.stream().filter(f -> f.canProvide(googleId)).collect(Collectors.toList());

        if (canProvide.isEmpty()) {
            return null;
        }

        return canProvide.get(0).create(googleId);
    }

    @Override
    public boolean hasProviderFor(String googleId) {
        return factories.stream().anyMatch(f -> f.canProvide(googleId));
    }
}
