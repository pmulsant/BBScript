package com.code.generation.v1_3.inference.fusion;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.util.Util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FusionDeclaration {
    private TypeInferenceMotor typeInferenceMotor;
    private Set<Typable> typables;
    private boolean isUsed;

    public FusionDeclaration(TypeInferenceMotor typeInferenceMotor, Typable typable1, Typable typable2) {
        this(typeInferenceMotor, Stream.of(typable1, typable2).collect(Collectors.toSet()));
    }

    public FusionDeclaration(TypeInferenceMotor typeInferenceMotor, Set<Typable> typables) {
        this.typeInferenceMotor = typeInferenceMotor;
        if(typables.isEmpty()){
            throw new IllegalStateException();
        }
        this.typables = typables;
        if (typables.stream().anyMatch(Objects::isNull)) {
            throw new IllegalStateException();
        }
    }

    public TypeSet fusionTypeSets() {
        Set<TypeSet> typeSets = typables.stream().map(typable -> typable.getType().getTypeSet()).collect(Collectors.toSet());
        if (typeSets.size() == 1) {
            return Util.getOneFromSet(typeSets);
        }
        TypeSet newTypeSet = new TypeSet(typeInferenceMotor, typeSets);
        newTypeSet.getTypes().forEach(type -> type.setTypeSet(newTypeSet));
        isUsed = true;
        return newTypeSet;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "fusion ";
        for (Typable typable : typables) {
            stringBuilder.append(prefix + typable.toString());
            prefix = ", ";
        }
        return stringBuilder.toString();
    }

    public Set<Typable> getTypables() {
        return typables;
    }

    public boolean isNotUsed() {
        return !isUsed;
    }
}
