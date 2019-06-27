package com.code.generation.v1_3.util.algorithms.graphs;

import java.util.Set;

public interface INonOrientedGraph<Node, GroupData> {
    static INonOrientedGraph instance(){
        return new NonOrientedGraphImpl<>();
    }
    boolean addNodeGroupAndReturnChange(Set<Node> nodes);
    Set<Set<Node>> getNodeGroups();

    void markGroup(Set<Node> nodes, GroupData data);
    GroupData getDataFromGroup(Set<Node> nodes);

    boolean contains(Node node, boolean compareWithEquals);
}
