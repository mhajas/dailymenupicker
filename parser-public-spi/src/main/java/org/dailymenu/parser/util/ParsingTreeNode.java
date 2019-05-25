package org.dailymenu.parser.util;

import java.util.Collection;

public interface ParsingTreeNode {

    NodeType getNodeType();

    String toString();

    Object getObject();
}
