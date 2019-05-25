package org.dailymenu.parser.tree.util;

import com.scalified.tree.TreeNode;
import org.dailymenu.parser.util.NodeProcessingFunction;
import org.dailymenu.parser.util.NodeType;
import org.dailymenu.parser.util.ParsingFunctionsContainer;
import org.dailymenu.parser.util.ParsingTreeNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultParsingFunctionsContainer implements ParsingFunctionsContainer {

    private Map<NodeType, NodeProcessingFunction> functionsMap;

    public DefaultParsingFunctionsContainer(Builder builder) {
        functionsMap = builder.functionsMap;
    }

    @Override
    public List<TreeNode<ParsingTreeNode>> applyFunctions(TreeNode<ParsingTreeNode> processedNode) {
        NodeType type = processedNode.data().getNodeType();

        if (functionsMap.containsKey(type)) { // If functions set contains function for current node type
            return functionsMap.get(type).processNode(processedNode); // process it and return all newly created nodes
        }

        return Collections.EMPTY_LIST;
    }

    public static class Builder {
        private Map<NodeType, NodeProcessingFunction> functionsMap = new HashMap<>();

        public Builder addFunction(NodeProcessingFunctionBuilder functionBuilder) {
            functionsMap.merge(functionBuilder.getFromNodeType(),
                    functionBuilder.build(),
                    (oldFunction, newFunction) ->
                            (node) -> Stream.concat(
                                oldFunction.processNode(node).stream(),
                                newFunction.processNode(node).stream()
                            ).collect(Collectors.toList())
            );

            return this;
        }

        public DefaultParsingFunctionsContainer build() {
            return new DefaultParsingFunctionsContainer(this);
        }
    }
}
