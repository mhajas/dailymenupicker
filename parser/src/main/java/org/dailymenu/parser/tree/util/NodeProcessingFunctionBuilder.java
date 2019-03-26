package org.dailymenu.parser.tree.util;

import com.scalified.tree.TreeNode;
import com.scalified.tree.multinode.ArrayMultiTreeNode;
import org.dailymenu.parser.util.NodeProcessingFunction;
import org.dailymenu.parser.util.NodeType;
import org.dailymenu.parser.util.ParsingTreeNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.dailymenu.parser.tree.util.NodeProcessingFunctionBuilder.PatternParameterType.PATTERN_PARAMETER;
import static org.dailymenu.parser.tree.util.NodeProcessingFunctionBuilder.UrlParameterType.HTML_PAGE_URL_PARAMETER;

public class NodeProcessingFunctionBuilder {


    public enum PatternParameterType implements ParameterTypeInterface<Pattern> {
        PATTERN_PARAMETER;

        @Override
        public String getName() {
            return name();
        }
    }

    public enum UrlParameterType implements ParameterTypeInterface<URL> {
        HTML_PAGE_URL_PARAMETER;

        @Override
        public String getName() {
            return name();
        }
    }

    public enum FunctionType {
        /**
         * Mandatory argument @pattern
         *
         * apply regular expression to node content and return
         */
        PATTERN_FUNCTION(nodeProcessingFunctionBuilder ->
            (nodeToProcess) -> {
                Matcher m = nodeProcessingFunctionBuilder.getArgument(PatternParameterType.PATTERN_PARAMETER).matcher(nodeToProcess.data().toString());

                List<String> resultList = new LinkedList<>();

                while (m.find()) {
                    resultList.add(m.group(1).trim());
                }

                return resultList.stream().map(s -> mapToNode(nodeProcessingFunctionBuilder, s)).collect(Collectors.toList());
            }
        ),

        /**
         * Mandatory argument HTML_SOURCE_ARGUMENT
         *
         * load webpage from url specified in argument
         */
        OBTAIN_HTML(nodeProcessingFunctionBuilder ->
            (nodeToProcess) -> {
                URL url = nodeProcessingFunctionBuilder.getArgument(HTML_PAGE_URL_PARAMETER);

                try {
                    URLConnection connection = url.openConnection();
                    connection.setReadTimeout(20000);
                    connection.setConnectTimeout(20000);

                    StringBuffer sb = new StringBuffer();
                    BufferedReader is = new BufferedReader(new InputStreamReader((InputStream) connection.getContent(), StandardCharsets.UTF_8));
                    String line;
                    while ((line = is.readLine()) != null) {
                        sb.append(line);
                    }

                    return Collections.singletonList(mapToNode(nodeProcessingFunctionBuilder, sb.toString().replace("\n", "").replace("\r", "")));
                } catch (IOException e) {
                    throw new RuntimeException("Unable to obtain HTML from url: " + url.toString(), e);
                }
            }
        );

        Function<NodeProcessingFunctionBuilder, NodeProcessingFunction> provideFunction;

        FunctionType(Function<NodeProcessingFunctionBuilder, NodeProcessingFunction> provideFunction) {
            this.provideFunction = provideFunction;
        }

        private static TreeNode<ParsingTreeNode> mapToNode(NodeProcessingFunctionBuilder builder, String contentOfNode) {
            Constructor<?> ctor;
            TreeNode<ParsingTreeNode> newNode;

            try {
                ctor = builder.clazz.getConstructor(String.class);
                newNode = new ArrayMultiTreeNode<>(new SingleArgumentNode(builder.toNodeType, ctor.newInstance(contentOfNode)));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Class " + builder.clazz.getName() + " is not constructable from String", e);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("Error during creation of node of type " + builder.toNodeType.name() + " from String: " + contentOfNode, e);
            }

            return newNode;
        }
    }

    private NodeType fromNodeType;
    private NodeType toNodeType;
    private FunctionType typeOfFunction;

    protected Map<ParameterTypeInterface, Object> arguments = new HashMap<>();
    private Class<?> clazz;

    public NodeProcessingFunctionBuilder(NodeType from, NodeType to, FunctionType type) {
        this.fromNodeType = from;
        this.toNodeType = to;
        this.clazz = to.getClazz();
        this.typeOfFunction = type;
    }

    protected <T> T getArgument(ParameterTypeInterface<T> type) {
        if (arguments.get(type) == null) {
            throw new RuntimeException("Argument with name " + type.getName() + " was not found");
        }

        return (T) arguments.get(type);
    }

    public NodeProcessingFunctionBuilder pattern(String pattern) {
        arguments.put(PATTERN_PARAMETER, RegexPatternProvider.getInstance().getPattern(pattern));
        return this;
    }

    public NodeProcessingFunctionBuilder url(String url) {
        try {
            arguments.put(HTML_PAGE_URL_PARAMETER, new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Cannot create URL from String " + url, e);
        }

        return this;
    }

    public NodeType getFromNodeType() {
        return fromNodeType;
    }

    public NodeProcessingFunction build() {
        return typeOfFunction.provideFunction.apply(this);
    }
}
