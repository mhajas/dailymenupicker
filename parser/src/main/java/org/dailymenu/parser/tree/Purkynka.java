package org.dailymenu.parser.tree;

import org.dailymenu.entity.food.Restaurant;
import org.dailymenu.parser.TreeParserProvider;
import org.dailymenu.parser.tree.util.DefaultParsingFunctionsContainer;
import org.dailymenu.parser.tree.util.NodeProcessingFunctionBuilder;
import org.dailymenu.parser.util.ParsingFunctionsContainer;
import org.dailymenu.services.parser.MultiFactoryParserService;

import static org.dailymenu.parser.tree.util.NodeProcessingFunctionBuilder.FunctionType.OBTAIN_HTML;
import static org.dailymenu.parser.tree.util.NodeProcessingFunctionBuilder.FunctionType.PATTERN_FUNCTION;
import static org.dailymenu.parser.util.NodeType.DAY_MENU_NODE;
import static org.dailymenu.parser.util.NodeType.EMPTY_NODE;
import static org.dailymenu.parser.util.NodeType.FOOD_NODE;
import static org.dailymenu.parser.util.NodeType.FULL_DOCUMENT_NODE;
import static org.dailymenu.parser.util.NodeType.FULL_FOOD_NODE;
import static org.dailymenu.parser.util.NodeType.PRICE_NODE;
import static org.dailymenu.parser.util.NodeType.SOUP_NODE;
import static org.dailymenu.parser.util.NodeType.WHOLE_MENU_NODE;

public class Purkynka extends AbstractTreeParserProvider {

    private Restaurant restaurant = new Restaurant("ChIJD55B_ASUEkcRgoFJeL_VJjE", "Purkyňka", true);

    @Override
    public Restaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public ParsingFunctionsContainer getParsingFunctionContainer() {
        return new DefaultParsingFunctionsContainer.Builder()
                .addFunction(new NodeProcessingFunctionBuilder(EMPTY_NODE, FULL_DOCUMENT_NODE, OBTAIN_HTML)
                        .url("http://www.napurkynce.cz/denni-menu/"))
                .addFunction(new NodeProcessingFunctionBuilder(FULL_DOCUMENT_NODE, WHOLE_MENU_NODE, PATTERN_FUNCTION)
                        .pattern("<p class=\"Standard\">&nbsp;</p>(.*</div>)<hr class=\"no-show\" />"))
                .addFunction(new NodeProcessingFunctionBuilder(WHOLE_MENU_NODE, DAY_MENU_NODE, PATTERN_FUNCTION)
                        .pattern("((Pondělí:|Úterý:|Středa:|Čtvrtek:|Pátek:).*?(</div><div>&nbsp;</div><div>|$))"))
                .addFunction(new NodeProcessingFunctionBuilder(DAY_MENU_NODE, FULL_FOOD_NODE, PATTERN_FUNCTION)
                        .pattern("<div>(?:&nbsp;)?[1234\\.\\s)]{3}(.{10,}?)</div>"))
                .addFunction(new NodeProcessingFunctionBuilder(DAY_MENU_NODE, SOUP_NODE, PATTERN_FUNCTION)
                        .pattern("(?:&nbsp;)?\\s?([A-Za-zÀ-ž\\s,\\(\\)]{5,})(&nbsp;)?</div>"))
                .addFunction(new NodeProcessingFunctionBuilder(FULL_FOOD_NODE, FOOD_NODE, PATTERN_FUNCTION)
                        .pattern("([A-Za-zÀ-ž\\s,\\(\\)\\-]{5,})(&nbsp;( )?)*(. )?( )?\\d+,-"))
                .addFunction(new NodeProcessingFunctionBuilder(FULL_FOOD_NODE, PRICE_NODE, PATTERN_FUNCTION)
                        .pattern("(\\d+),-"))
                .build();
    }

    public static void main(String[] args) {
        TreeParserProvider p = new Purkynka();
        System.out.println(p.parse().toFormattedMenu());
    }

}
