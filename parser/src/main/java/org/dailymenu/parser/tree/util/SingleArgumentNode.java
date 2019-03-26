package org.dailymenu.parser.tree.util;

import org.dailymenu.parser.util.NodeType;
import org.dailymenu.parser.util.ParsingTreeNode;

public class SingleArgumentNode implements ParsingTreeNode {

    private NodeType type;
    private Object argument;

    public SingleArgumentNode(NodeType type, Object argument) {
        this.type = type;
        this.argument = argument;
    }

    @Override
    public NodeType getNodeType() {
        return type;
    }

    @Override
    public String toString() {
        return argument.getClass().getName() + "{" +
                "type=" + type +
                ", argument=" + argument +
                '}';
    }

    @Override
    public Object getObject() {
        return argument;
    }
}

