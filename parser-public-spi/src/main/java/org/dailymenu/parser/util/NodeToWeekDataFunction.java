package org.dailymenu.parser.util;


import com.scalified.tree.TreeNode;
import org.dailymenu.entity.food.RestaurantWeekData;

@FunctionalInterface
public interface NodeToWeekDataFunction {

    void processNode(TreeNode<ParsingTreeNode> node, RestaurantWeekData data);

}
