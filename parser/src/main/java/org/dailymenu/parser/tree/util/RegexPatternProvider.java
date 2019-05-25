package org.dailymenu.parser.tree.util;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegexPatternProvider {
    private static final RegexPatternProvider INSTANCE = new RegexPatternProvider();
    private Map<String, Pattern> patterns = new HashMap<>();

    public static RegexPatternProvider getInstance() {
        return INSTANCE;
    }

    private RegexPatternProvider() {}

    public Pattern getPattern(String pattern) {
        return patterns.computeIfAbsent(pattern, Pattern::compile);
    }


}
