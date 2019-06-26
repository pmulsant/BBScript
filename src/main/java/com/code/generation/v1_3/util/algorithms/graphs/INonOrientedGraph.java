package com.code.generation.v1_3.util.algorithms.graphs;

import java.util.Set;

public interface INonOrientedGraph<Node> {
    static INonOrientedGraph instance(){
        return new NonOrientedGraphImpl<>();
    }
    void addNodeGroup(Set<Node> nodes);
    Set<Set<Node>> getNodeGroups();
}
