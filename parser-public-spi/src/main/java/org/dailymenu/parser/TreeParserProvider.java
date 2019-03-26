package org.dailymenu.parser;

import com.scalified.tree.TreeNode;
import org.dailymenu.entity.food.Restaurant;
import org.dailymenu.parser.util.NodeProcessingFunction;
import org.dailymenu.parser.util.NodeToWeekDataFunction;
import org.dailymenu.parser.util.NodeType;
import org.dailymenu.parser.util.ParsingFunctionsContainer;
import org.dailymenu.parser.util.ParsingTreeNode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * The idea of TreeParserProvider is building a tree where each node contains information extracted from its parent node
 *
 * High level view of tree parsing mechanism:
 *
 * Root node is full page (FULL_PAGE_NODE)
 * -- Only daily menu area from website in String (WHOLE_MENU_NODE)
 * ---- Monday menu in one String (DAY_MENU_NODE)
 * ------ 1st item from food list for parent node (in this case Monday) still in String (FOOD_ITEM_NODE)
 * -------- Node with parsed food name and price (PARSED_FOOD_NODE)
 * ------ 2st item from food list for parent node (in this case Monday) still in String (FOOD_ITEM_NODE)
 * -------- Node with parsed food name and price (PARSED_FOOD_NODE)
 * ---- Tuesday menu in one String (DAY_MENU_NODE)
 * ------ 1st item from food list for parent node (in this case Tuesday) still in String (FOOD_ITEM_NODE)
 * -------- Node with parsed food name and price (PARSED_FOOD_NODE)
 * ------ 2st item from food list for parent node (in this case Tuesday) still in String (FOOD_ITEM_NODE)
 * -------- Node with parsed food name and price (PARSED_FOOD_NODE)
 * etc.
 *
 * ----------------------------------------------------------------
 *
 * Creation of tree:
 * Based on set of function where each of function is applied to specified type of nodes (source node) and create children nodes (output node) to this node based on its functionality
 * We traverse through whole tree and for each node of specified type execute function
 *
 * For example first function may be applied to nodes of type FULL_PAGE_NODE and create WHOLE_MENU_NODE based on regular expression which gets content of <div id="daily_menu_area"></div>
 *
 * Another function would be applied to WHOLE_MENU_NODE and for each match of regular expression "<div id="[monday|tuesday|wednesday...]_menu">(.*)</div>" create child node of type DAY_MENU_NODE
 *
 * This might be easily extendable to more type of nodes for example week number node, soup name node, day date
 *
 * etc.
 *
 * ----------------------------------------------------------------
 *
 * Each restaurant will have separate set of functions. We are aiming to have set of function represented only by triple (SOURCE_NODE_TYPE, OUTPUT_NODE_TYPE, regular expression) for each restaurant.
 * In this ideal scenario it would be quite easy to maintain this configuration in some web application
 *
 * ----------------------------------------------------------------
 *
 * Tree obtained like this may be easily validated, for example check if every PARSED_FOOD_NODE contains price and not empty name or the tree contains SOUP_NODE etc.
 *
 * Also from valid tree it would be easy to obtain RestaurantWeekData as they are already prepared there
 *
 * Real tree example in README.md file
 *
 *
 *
 *
 *
 */
public interface TreeParserProvider extends ParserProvider {

    /**
     * Each provider can parse only one restaurant with googleId specified by this method
     * @return googleId
     */
    Restaurant getRestaurant();

    /**
     * Build ParsingTree based on functions in FunctionContainer
     * @return parsed tree
     */
    TreeNode<ParsingTreeNode> buildParsingTree();

    ParsingFunctionsContainer getParsingFunctionContainer();

    Map<NodeType, NodeToWeekDataFunction> getWeekDataFunctionSet();
}
