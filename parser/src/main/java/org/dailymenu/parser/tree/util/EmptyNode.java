package org.dailymenu.parser.tree.util;

import org.dailymenu.parser.util.NodeType;
import org.dailymenu.parser.util.ParsingTreeNode;

public class EmptyNode implements ParsingTreeNode {
    @Override
    public NodeType getNodeType() {
        return NodeType.EMPTY_NODE;
    }

    @Override
    public Object getObject() {
        return null;
    }
}
