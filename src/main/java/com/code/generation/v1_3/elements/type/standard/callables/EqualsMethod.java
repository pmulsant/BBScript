package com.code.generation.v1_3.elements.type.standard.callables;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class EqualsMethod extends GenericMethod {
    private static final String METHOD_NAME = "equals";

    public EqualsMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor, String name, StandardType standardInnerType, StandardType standardReturnedType, List<StandardType> standardTypeParameters) {
        super(standardTypeDirectory, typeInferenceMotor, METHOD_NAME, null, standardTypeDirectory.getStandardType(StandardKnowledges.BOOLEAN_TYPE_NAME), Collections.singletonList(null));
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        typeInferenceMotor.addFusionOfTypesDeclaration(innerTypable, typableArguments.get(0));
    }
}
