package com.code.generation.v1_3.inference.fusion;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FusionDeclaration {
    private TypeInferenceMotor typeInferenceMotor;
    private List<? extends Typable> typables;

    public FusionDeclaration(TypeInferenceMotor typeInferenceMotor, Typable typable1, Typable typable2) {
        this(typeInferenceMotor, Arrays.asList(typable1, typable2));
    }

    public FusionDeclaration(TypeInferenceMotor typeInferenceMotor, List<? extends Typable> typables) {
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
        return new TypeSet(typeInferenceMotor, typeSets);
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

    public List<? extends Typable> getTypables() {
        return typables;
    }
}
