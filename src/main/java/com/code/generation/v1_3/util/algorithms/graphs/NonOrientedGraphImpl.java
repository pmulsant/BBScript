package com.code.generation.v1_3.util.algorithms.graphs;

import java.util.*;

public class NonOrientedGraphImpl<Node, GroupData> implements INonOrientedGraph<Node, GroupData> {
    private Map<Set<Node>, GroupData> nodeGroups = new HashMap<>();

    @Override
    public boolean addNodeGroupAndReturnChange(Set<Node> nodes) {
        if(areNodesAlreadyInAGroup(nodes)){
            return false;
        }
        Set<Set<Node>> toRemoves = getAllNodeGroupsWhichContainsOneOfTheNodes(nodes);
        toRemoves.forEach(toRemove -> nodeGroups.remove(toRemove));
        Set<Node> newGroup = new HashSet<>(nodes);
        toRemoves.forEach(aGroup -> newGroup.addAll(aGroup));
        nodeGroups.put(newGroup, null);
        return true;
    }

    private boolean areNodesAlreadyInAGroup(Set<Node> nodes) {
        for (Set<Node> nodeGroup : nodeGroups.keySet()) {
            if(nodeGroup.containsAll(nodes)){
                return true;
            }
        }
        return false;
    }

    private Set<Set<Node>> getAllNodeGroupsWhichContainsOneOfTheNodes(Set<Node> nodes) {
        Set<Set<Node>> result = new HashSet<>();
        nodeGroups.keySet().forEach(aGroup -> {
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
        return nodeGroups.keySet();
    }

    @Override
    public void markGroup(Set<Node> nodes, GroupData data) {
        if(!nodeGroups.containsKey(nodes)){
            throw new IllegalStateException();
        }
        nodeGroups.put(nodes, data);
    }

    @Override
    public GroupData getDataFromGroup(Set<Node> nodes) {
        return nodeGroups.get(nodes);
    }

    @Override
    public boolean contains(Node node, boolean compareWithEquals) {
        for (Set<Node> nodeGroup : nodeGroups.keySet()) {
            for (Node aNode : nodeGroup) {
                if(aNode == node || (compareWithEquals && aNode.equals(node))){
                    return true;
                }
            }
        }
        return false;
    }
}
