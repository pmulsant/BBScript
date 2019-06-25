package com.code.generation.v1_3.elements.type.standard.callables;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class ToStringMethod extends GenericMethod {
    private static final String METHOD_NAME = "toString";

    public ToStringMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, METHOD_NAME, null, standardTypeDirectory.getStandardType(StandardKnowledges.STRING_TYPE_NAME), Collections.EMPTY_LIST);
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
    }
}
