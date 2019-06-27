package com.code.generation.v1_3.inference.fusion;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.inference.rules.OperablesRuleMotor;
import com.code.generation.v1_3.inference.rules.Rule;
import com.code.generation.v1_3.util.algorithms.graphs.INonOrientedGraph;

import java.util.*;
import java.util.stream.Collectors;

public class FusionMotor {
    private static final boolean IS_USED = true;

    private TypeInferenceMotor typeInferenceMotor;
    private Set<Rule> rules = new HashSet<>();

    private List<Typable> typables = new LinkedList<>();
    private Set<FusionDeclaration> fusionDeclarations = new HashSet<>();

    public FusionMotor(TypeInferenceMotor typeInferenceMotor) {
        this.typeInferenceMotor = typeInferenceMotor;
    }

    public void fusion() {
        cleanFusionDeclarationWithOneElementOrLess(fusionDeclarations);
        INonOrientedGraph<Typable, Boolean> graph = makeGraph(fusionDeclarations);

        boolean somethingDiscoverFromRuleMotor;
        boolean somethingDiscoverByClassicFusion;
        do {
            somethingDiscoverFromRuleMotor = inferRuleMotor(graph);
            somethingDiscoverByClassicFusion = fusion(graph);
        } while (somethingDiscoverFromRuleMotor || somethingDiscoverByClassicFusion);
    }

    private boolean inferRuleMotor(INonOrientedGraph<Typable, Boolean> graph) {
        Set<FusionDeclaration> declarationsFromRuleMotor = new OperablesRuleMotor(typeInferenceMotor, rules).infer(); // will set names of some types
        return populateGraphAndReturnChange(graph, declarationsFromRuleMotor);
    }

    private boolean fusion(INonOrientedGraph<Typable, Boolean> graph) {
        addFusionDeclarationFromTypeNames(graph);
        Set<Set<Typable>> notUsedGroups = graph.getNodeGroups().stream().filter(group -> {
            Boolean isUsed = graph.getDataFromGroup(group);
            return isUsed == null || !isUsed;
        }).collect(Collectors.toSet());
        if(!notUsedGroups.isEmpty()){
            notUsedGroups.forEach(group -> graph.markGroup(group, IS_USED));
            Set<FusionDeclaration> notUsedDeclarations = notUsedGroups.stream().map(group -> new FusionDeclaration(typeInferenceMotor, group)).collect(Collectors.toSet());
            Set<FusionDeclaration> newFusionDeclarations = fusion(notUsedDeclarations);
            populateGraphAndReturnChange(graph, newFusionDeclarations);
            return true;
        }
        return false;
    }


    private boolean populateGraphAndReturnChange(INonOrientedGraph<Typable, Boolean> graph, Set<FusionDeclaration> declarations) {
        boolean change = false;
        for (FusionDeclaration fusionDeclaration : declarations) {
            if (graph.addNodeGroupAndReturnChange(fusionDeclaration.getTypables())) {
                change = true;
            }
        }
        return change;
    }

    private void addFusionDeclarationFromTypeNames(INonOrientedGraph<Typable, Boolean> graph) {
        Map<String, Set<Typable>> typablesMap = new HashMap<>();
        for (Typable typable : typables) {
            if (typable.getType().getSimpleName() != null) {
                Set<Typable> sameNameTypableList = typablesMap.computeIfAbsent(typable.getType().getSimpleName(), key -> new HashSet<>());
                sameNameTypableList.add(typable);
            }
        }
        for (Set<Typable> sameNameTypableSet : typablesMap.values()) {
            Set<TypeSet> typeSets = sameNameTypableSet.stream().map(typable -> typable.getType().getTypeSet()).collect(Collectors.toSet());
            if(typeSets.size() > 1) {
                graph.addNodeGroupAndReturnChange(sameNameTypableSet);
            }
        }
    }

    public Set<FusionDeclaration> fusion(Set<FusionDeclaration> declarations) {
        Set<TypeSet> typeSets = new HashSet<>();
        for (FusionDeclaration declaration : declarations) {
            TypeSet typeSet = declaration.fusionTypeSets();
            typeSet.checkTypeSetOfTypesCoherence();
            typeSets.add(typeSet);
        }
        Set<FusionDeclaration> result = new HashSet<>();
        for (TypeSet typeSet : typeSets) {
            List<FusionDeclaration> newFusionDeclarations = typeSet.fusionAndClean();
            result.addAll(newFusionDeclarations);
        }
        cleanFusionDeclarationWithOneElementOrLess(result);
        return result;
    }

    private INonOrientedGraph<Typable, Boolean> makeGraph(Set<FusionDeclaration> fusionDeclarations) {
        INonOrientedGraph<Typable, Boolean> graph = reduceWithFusionDeclarations(fusionDeclarations);
        return reduceWithTypeSets(graph);
    }

    private INonOrientedGraph<Typable, Boolean> reduceWithFusionDeclarations(Set<FusionDeclaration> reduced) {
        INonOrientedGraph<Typable, Boolean> graph = INonOrientedGraph.instance();
        reduced.forEach(declaration -> {
            graph.addNodeGroupAndReturnChange(new HashSet<>(declaration.getTypables()));
        });
        return graph;
    }

    private INonOrientedGraph<Typable, Boolean> reduceWithTypeSets(INonOrientedGraph<Typable, Boolean> graph) {
        Set<Set<Typable>> nodeGroups = new HashSet<>(graph.getNodeGroups());
        nodeGroups.forEach(group -> {
            group.forEach(typable -> {
                TypeSet typeSet = typable.getType().getTypeSet();
                graph.addNodeGroupAndReturnChange(getTypablesAlreadyInGraph(graph, typeSet));
            });
        });
        return graph;
    }

    private Set<Typable> getTypablesAlreadyInGraph(INonOrientedGraph<Typable, Boolean> graph, TypeSet typeSet) {
        Set<Typable> result = new HashSet<>();
        typeSet.getTypes().forEach(type -> {
            type.getTypables().forEach(typable -> {
                if (graph.contains(typable, false)) {
                    result.add(typable);
                }
            });
        });
        return result;
    }

    private void cleanFusionDeclarationWithOneElementOrLess(Set<FusionDeclaration> fusionDeclarations) {
        fusionDeclarations.removeIf(fusionDeclaration -> fusionDeclaration.getTypables().size() <= 1);
    }

    public void addTypable(Typable typable) {
        typables.add(typable);
    }

    public List<Typable> getTypables() {
        return typables;
    }

    public void addFusion(Typable typable1, Typable typable2) {
        fusionDeclarations.add(new FusionDeclaration(typeInferenceMotor, typable1, typable2));
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }
}
