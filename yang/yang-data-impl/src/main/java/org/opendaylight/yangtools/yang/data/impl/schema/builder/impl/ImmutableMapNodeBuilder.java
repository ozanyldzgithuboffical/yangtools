/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.impl.schema.builder.impl;

import java.util.List;
import java.util.Map;

import org.opendaylight.yangtools.yang.data.api.InstanceIdentifier;
import org.opendaylight.yangtools.yang.data.api.schema.MapEntryNode;
import org.opendaylight.yangtools.yang.data.api.schema.MapNode;
import org.opendaylight.yangtools.yang.data.impl.schema.builder.api.CollectionNodeBuilder;
import org.opendaylight.yangtools.yang.data.impl.schema.nodes.AbstractImmutableNormalizedNode;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

public class ImmutableMapNodeBuilder
        implements CollectionNodeBuilder<MapEntryNode, MapNode> {

    protected Map<InstanceIdentifier.NodeIdentifierWithPredicates, MapEntryNode> value;
    protected InstanceIdentifier.NodeIdentifier nodeIdentifier;

    public static CollectionNodeBuilder<MapEntryNode, MapNode> create() {
        return new ImmutableMapNodeBuilder();
    }

    public CollectionNodeBuilder<MapEntryNode, MapNode> withChild(MapEntryNode child) {
        if(this.value == null) {
            this.value = Maps.newLinkedHashMap();
        }

        this.value.put(child.getIdentifier(), child);
        return this;
    }

    @Override
    public CollectionNodeBuilder<MapEntryNode, MapNode> withValue(List<MapEntryNode> value) {
        // TODO replace or putAll ?
        for (MapEntryNode mapEntryNode : value) {
            withChild(mapEntryNode);
        }

        return this;
    }

    public CollectionNodeBuilder<MapEntryNode, MapNode> withNodeIdentifier(InstanceIdentifier.NodeIdentifier nodeIdentifier) {
        this.nodeIdentifier = nodeIdentifier;
        return this;
    }

    @Override
    public MapNode build() {
        return new ImmutableMapNode(nodeIdentifier, value);
    }

    static final class ImmutableMapNode extends AbstractImmutableNormalizedNode<InstanceIdentifier.NodeIdentifier, Iterable<MapEntryNode>> implements MapNode {

        private final Map<InstanceIdentifier.NodeIdentifierWithPredicates, MapEntryNode> mappedChildren;

        ImmutableMapNode(InstanceIdentifier.NodeIdentifier nodeIdentifier,
                         Map<InstanceIdentifier.NodeIdentifierWithPredicates, MapEntryNode> children) {
            super(nodeIdentifier, children.values());
            this.mappedChildren = children;
        }

        @Override
        public Optional<MapEntryNode> getChild(InstanceIdentifier.NodeIdentifierWithPredicates child) {
            return Optional.fromNullable(mappedChildren.get(child));
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("ImmutableMapNode{");
            sb.append("nodeIdentifier=").append(nodeIdentifier);
            sb.append(", children=").append(mappedChildren);
            sb.append('}');
            return sb.toString();
        }
    }
}
