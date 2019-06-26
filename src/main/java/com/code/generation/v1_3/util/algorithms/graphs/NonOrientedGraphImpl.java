package com.code.generation.v1_3.util.algorithms.graphs;

import java.util.HashSet;
import java.util.Set;

public class NonOrientedGraphImpl<Node> implements INonOrientedGraph<Node> {
    private Set<Set<Node>> nodeGroups = new HashSet<>();

    @Override
    public void addNodeGroup(Set<Node> nodes) {
        Set<Set<Node>> allNodeGroupsWhichContainsOneOfTheNodes = getAllNodeGroupsWhichContainsOneOfTheNodes(nodes);
        nodeGroups.removeIf(nodeGroup -> allNodeGroupsWhichContainsOneOfTheNodes.contains(nodeGroup));
        Set<Node> newGroup = new HashSet<>(nodes);
        allNodeGroupsWhichContainsOneOfTheNodes.forEach(aGroup -> newGroup.addAll(aGroup));
        nodeGroups.add(newGroup);
    }

    private Set<Set<Node>> getAllNodeGroupsWhichContainsOneOfTheNodes(Set<Node> nodes) {
        Set<Set<Node>> result = new HashSet<>();
        nodeGroups.forEach(aGroup -> {
            if(haveNodeInCommon(aGroup, nodes)){
                result.add(aGroup);
            }
        });
        return result;
    }

    private boolean haveNodeInCommon(Set<Node> aGroup, Set<Node> nodes) {
        for (Node node : aGroup) {
            if(nodes.contains(node)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Set<Node>> getNodeGroups() {
        return nodeGroups;
    }
}
