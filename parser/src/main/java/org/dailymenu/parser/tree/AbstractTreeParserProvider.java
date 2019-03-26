package org.dailymenu.parser.tree;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import org.dailymenu.entity.food.FoodEntity;
import org.dailymenu.entity.food.RestaurantDailyData;
import org.dailymenu.entity.food.RestaurantWeekData;
import org.dailymenu.parser.TreeParserProvider;
import org.dailymenu.parser.tree.util.EmptyNode;
import org.dailymenu.parser.util.NodeToWeekDataFunction;
import org.dailymenu.parser.util.NodeType;
import org.dailymenu.parser.util.ParsingFunctionsContainer;
import org.dailymenu.parser.util.ParsingTreeNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.dailymenu.parser.util.NodeType.FOOD_NODE;
import static org.dailymenu.parser.util.NodeType.FULL_FOOD_NODE;
import static org.dailymenu.parser.util.NodeType.PRICE_NODE;
import static org.dailymenu.parser.util.NodeType.SOUP_NODE;

public abstract class AbstractTreeParserProvider implements TreeParserProvider {

    @Override
    public TreeNode<ParsingTreeNode> buildParsingTree() {

        // Create root node
        TreeNode<ParsingTreeNode> tree = new ArrayMultiTreeNode<>(new EmptyNode());

        // Add root node to processingQueue
        Queue<TreeNode<ParsingTreeNode>> processingQueue = new LinkedList<>();
        processingQueue.add(tree);

        // Initialize parsing functions for current restaurant
        ParsingFunctionsContainer parsingFunctionContainer = getParsingFunctionContainer();

        // Process all nodes in queue with functions
        while(!processingQueue.isEmpty()) {
            TreeNode<ParsingTreeNode> processedNode = processingQueue.remove(); // Get node

            List<TreeNode<ParsingTreeNode>> newlyCreatedNodes = parsingFunctionContainer.applyFunctions(processedNode); // create child nodes for processedNode
            newlyCreatedNodes.forEach(processedNode::add); // Add childNodes to parent node
            processingQueue.addAll(newlyCreatedNodes); // add all newly created nodes to processing queue
        }

        return tree;
    }

    @Override
    public Map<NodeType, NodeToWeekDataFunction> getWeekDataFunctionSet() {

        Map<NodeType, NodeToWeekDataFunction> resultSet = new HashMap<>();

        resultSet.put(NodeType.DAY_MENU_NODE,
                (nodeToProcess, weekDataToStore) -> {
                    RestaurantDailyData d = new RestaurantDailyData();
                    nodeToProcess.preOrdered().stream().filter(n -> n.data().getNodeType() == FULL_FOOD_NODE)
                            .forEach(n -> d.addMenuToDay(createFoodEntityFromTreeNode(n)));
                    nodeToProcess.preOrdered().stream().filter(n -> n.data().getNodeType() == SOUP_NODE)
                            .forEach(n -> {
                                FoodEntity soup = new FoodEntity();
                                soup.setName((String) n.data().getObject());
                                soup.setPrice(0);
                                d.addSoupToDay(soup);
                            });

                    weekDataToStore.addMenuForDay(d);
                }
        );

        return resultSet;
    }

    public FoodEntity createFoodEntityFromTreeNode(TreeNode<ParsingTreeNode> node) {
        assert (node.data().getNodeType() == FULL_FOOD_NODE);

        FoodEntity f = new FoodEntity();

        node.forEach(n -> {
            switch (n.data().getNodeType()) {
                case FOOD_NODE:
                    f.setName((String) n.data().getObject());
                    break;
                case PRICE_NODE:
                    f.setPrice((Integer) n.data().getObject());
                    break;
            }
        });

        return f;
    }

    @Override
    public RestaurantWeekData parse() {
        Map<NodeType, NodeToWeekDataFunction> functionMap = getWeekDataFunctionSet();
        RestaurantWeekData weekData = new RestaurantWeekData();

        weekData.setRestaurant(getRestaurant());
        TreeNode<ParsingTreeNode> tree = buildParsingTree();

        System.out.println(tree);

        System.out.println("FULL " + tree.preOrdered().stream().filter(node -> node.data().getNodeType().equals(FULL_FOOD_NODE)).count());
        System.out.println("FOOD " + tree.preOrdered().stream().filter(node -> node.data().getNodeType().equals(FOOD_NODE)).count());
        System.out.println("PRICE " + tree.preOrdered().stream().filter(node -> node.data().getNodeType().equals(PRICE_NODE)).count());
        System.out.println("SOUP " + tree.preOrdered().stream().filter(node -> node.data().getNodeType().equals(SOUP_NODE)).count());

        tree.forEach(parsingTreeNodeTreeNode -> {
            if (functionMap.containsKey(parsingTreeNodeTreeNode.data().getNodeType())) {
                functionMap.get(parsingTreeNodeTreeNode.data().getNodeType()).processNode(parsingTreeNodeTreeNode, weekData);
            }
        });

        return weekData;
    }
}
