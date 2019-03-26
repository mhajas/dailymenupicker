package org.dailymenu.parser.util;


import com.scalified.tree.TreeNode;

import java.util.List;

@FunctionalInterface
public interface NodeProcessingFunction {

    List<TreeNode<ParsingTreeNode>> processNode(TreeNode<ParsingTreeNode> node);

}
