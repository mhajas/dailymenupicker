package org.dailymenu.parser.util;

import com.scalified.tree.TreeNode;

import java.util.List;

public interface ParsingFunctionsContainer {
     List<TreeNode<ParsingTreeNode>> applyFunctions(TreeNode<ParsingTreeNode> processedNode);
}
