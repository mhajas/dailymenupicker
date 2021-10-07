package org.dailymenu.services.parser;

import org.dailymenu.parser.ParserProvider;

public interface ParserService {

    ParserProvider getParserProvider(String googleId);
    boolean hasProviderFor(String googleId);

}
