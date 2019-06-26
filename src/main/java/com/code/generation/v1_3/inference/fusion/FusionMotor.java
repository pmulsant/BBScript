package com.code.generation.v1_3.inference.fusion;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.util.algorithms.graphs.INonOrientedGraph;

import java.util.*;
import java.util.stream.Collectors;

public class FusionMotor {
    private TypeInferenceMotor typeInferenceMotor;
    private List<Typable> typables = new LinkedList<>();
    private List<FusionDeclaration> fusionDeclarations = new LinkedList<>();

    public FusionMotor(TypeInferenceMotor typeInferenceMotor) {
        this.typeInferenceMotor = typeInferenceMotor;
    }

    public void fusion() {
        addFusionDeclarationFromTypeNames();
        fusionDeclarations = cleanFusionDeclarationWithOneElement(fusionDeclarations);
        List<FusionDeclaration> fusionDeclarations = this.fusionDeclarations;
        while (!(fusionDeclarations = fusion(fusionDeclarations)).isEmpty()) ;
    }

    private void addFusionDeclarationFromTypeNames() {
        Map<String, List<Typable>> typablesMap = new HashMap<>();
        for (Typable typable : typables) {
            if (typable.getType().getSimpleName() != null) {
                List<Typable> sameNameTypableList = typablesMap.computeIfAbsent(typable.getType().getSimpleName(), key -> new LinkedList<>());
                sameNameTypableList.add(typable);
            }
        }
        for (List<Typable> sameNameTypableList : typablesMap.values()) {
            fusionDeclarations.add(new FusionDeclaration(typeInferenceMotor, sameNameTypableList));
        }
    }

    public List<FusionDeclaration> fusion(List<FusionDeclaration> declarations) {
        List<FusionDeclaration> reducedList = simplifyDeclarationsIntoDistinctDeclarations(declarations);
        List<FusionDeclaration> result = new LinkedList<>();
        Set<TypeSet> typeSets = new HashSet<>();
        for (FusionDeclaration declaration : reducedList) {
            TypeSet typeSet = declaration.fusionTypeSets();
            typeSet.checkTypeSetOfTypesCoherence();
            typeSets.add(typeSet);
        }
        for (TypeSet typeSet : typeSets) {
            result.addAll(typeSet.fusionAndClean());
        }
        return cleanFusionDeclarationWithOneElement(result);
    }

    private List<FusionDeclaration> simplifyDeclarationsIntoDistinctDeclarations(List<FusionDeclaration> declarations) {
        Set<FusionDeclaration> reduced = new HashSet<>(declarations);
        int previousSize;
        do {
            previousSize = reduced.size();
            reduced = reduce(reduced);
            if(reduced.size() > previousSize){
                throw new IllegalStateException();
            }
        } while (reduced.size() != previousSize);
        return new ArrayList<>(reduced);
    }

    private Set<FusionDeclaration> reduce(Set<FusionDeclaration> reduced) {
        INonOrientedGraph<Typable> graph = reduceWithFusionDeclarations(reduced);
        reduceWithTypeSets(graph);
        return graph.getNodeGroups().stream().map(group -> new FusionDeclaration(typeInferenceMotor, new ArrayList<>(group)))
                .collect(Collectors.toSet());
    }

    private INonOrientedGraph<Typable> reduceWithFusionDeclarations(Set<FusionDeclaration> reduced) {
        INonOrientedGraph<Typable> graph = INonOrientedGraph.instance();
        reduced.forEach(declaration -> {
            graph.addNodeGroup(new HashSet<>(declaration.getTypables()));
        });
        return graph;
    }

    private void reduceWithTypeSets(INonOrientedGraph<Typable> graph) {
        Set<Set<Typable>> nodeGroups = new HashSet<>(graph.getNodeGroups());
        nodeGroups.forEach(group -> {
            group.forEach(typable -> {
                TypeSet typeSet = typable.getType().getTypeSet();
                graph.addNodeGroup(getTypablesAlreadyInGraph(graph, typeSet));
            });
        });
    }

    private Set<Typable> getTypablesAlreadyInGraph(INonOrientedGraph<Typable> graph, TypeSet typeSet) {
        Set<Typable> result = new HashSet<>();
        typeSet.getTypes().forEach(type -> {
            type.getTypables().forEach(typable -> {
                if(graph.contains(typable, false)){
                    result.add(typable);
                }
            });
        });
        return result;
    }

    private List<FusionDeclaration> cleanFusionDeclarationWithOneElement(List<FusionDeclaration> fusionDeclarations) {
        return fusionDeclarations.stream().filter(aDeclaration -> aDeclaration.getTypables().size() != 1).collect(Collectors.toList());
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
}
