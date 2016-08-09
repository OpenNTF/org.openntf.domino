package com.tinkerpop.blueprints.util.io.graphson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Configure how the GraphSON utility treats edge and vertex properties.
 *
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class ElementPropertyConfig {

    public static enum ElementPropertiesRule {
        INCLUDE, EXCLUDE
    }

    private final List<String> vertexPropertyKeys;
    private final List<String> edgePropertyKeys;
    private final ElementPropertiesRule vertexPropertiesRule;
    private final ElementPropertiesRule edgePropertiesRule;
    private final boolean normalized;

    /**
     * A configuration that includes all properties of vertices and edges.
     */
    public static final ElementPropertyConfig AllProperties = new ElementPropertyConfig(null, null,
            ElementPropertiesRule.INCLUDE, ElementPropertiesRule.INCLUDE, false);


    public ElementPropertyConfig(final Set<String> vertexPropertyKeys, final Set<String> edgePropertyKeys,
                                 final ElementPropertiesRule vertexPropertiesRule,
                                 final ElementPropertiesRule edgePropertiesRule) {
        this(vertexPropertyKeys, edgePropertyKeys, vertexPropertiesRule, edgePropertiesRule, false);
    }

    public ElementPropertyConfig(final Set<String> vertexPropertyKeys, final Set<String> edgePropertyKeys,
                                 final ElementPropertiesRule vertexPropertiesRule,
                                 final ElementPropertiesRule edgePropertiesRule, final boolean normalized) {
        this.vertexPropertiesRule = vertexPropertiesRule;
        this.vertexPropertyKeys = sortKeys(vertexPropertyKeys, normalized);
        this.edgePropertiesRule = edgePropertiesRule;
        this.edgePropertyKeys = sortKeys(edgePropertyKeys, normalized);
        this.normalized = normalized;
    }

    /**
     * Construct a configuration that includes the specified properties from both vertices and edges.
     */
    public static ElementPropertyConfig includeProperties(final Set<String> vertexPropertyKeys,
                                                          final Set<String> edgePropertyKeys) {
        return new ElementPropertyConfig(vertexPropertyKeys, edgePropertyKeys, ElementPropertiesRule.INCLUDE,
                ElementPropertiesRule.INCLUDE);
    }

    public static ElementPropertyConfig includeProperties(final Set<String> vertexPropertyKeys,
                                                          final Set<String> edgePropertyKeys, final boolean normalized) {
        return new ElementPropertyConfig(vertexPropertyKeys, edgePropertyKeys, ElementPropertiesRule.INCLUDE,
                ElementPropertiesRule.INCLUDE, normalized);
    }

    /**
     * Construct a configuration that excludes the specified properties from both vertices and edges.
     */
    public static ElementPropertyConfig excludeProperties(final Set<String> vertexPropertyKeys,
                                                          final Set<String> edgePropertyKeys) {
        return new ElementPropertyConfig(vertexPropertyKeys, edgePropertyKeys, ElementPropertiesRule.EXCLUDE,
                ElementPropertiesRule.EXCLUDE);
    }

    public static ElementPropertyConfig excludeProperties(final Set<String> vertexPropertyKeys,
                                                          final Set<String> edgePropertyKeys, final boolean normalized) {
        return new ElementPropertyConfig(vertexPropertyKeys, edgePropertyKeys, ElementPropertiesRule.EXCLUDE,
                ElementPropertiesRule.EXCLUDE, normalized);
    }

    public List<String> getVertexPropertyKeys() {
        return vertexPropertyKeys;
    }

    public List<String> getEdgePropertyKeys() {
        return edgePropertyKeys;
    }

    public ElementPropertiesRule getVertexPropertiesRule() {
        return vertexPropertiesRule;
    }

    public ElementPropertiesRule getEdgePropertiesRule() {
        return edgePropertiesRule;
    }

    public boolean isNormalized() {
        return normalized;
    }

    private static List<String> sortKeys(final Set<String> keys, final boolean normalized) {
        final List<String> propertyKeyList;
        if (keys != null) {
            if (normalized) {
                final List<String> sorted = new ArrayList<String>(keys);
                Collections.sort(sorted);
                propertyKeyList = sorted;
            } else
                propertyKeyList = new ArrayList<String>(keys);
        } else
            propertyKeyList = null;

        return propertyKeyList;
    }
}
