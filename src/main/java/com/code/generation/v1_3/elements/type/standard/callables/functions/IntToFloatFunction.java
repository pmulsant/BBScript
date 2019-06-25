package com.code.generation.v1_3.elements.type.standard.callables.functions;

import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;

public class IntToFloatFunction extends StandardFunction {
    private static final String FUNCTION_NAME = "float";

    public IntToFloatFunction(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, FUNCTION_NAME, standardTypeDirectory.getStandardType(StandardKnowledges.FLOAT_TYPE_NAME), Collections.singletonList(standardTypeDirectory.getStandardType(StandardKnowledges.INT_TYPE_NAME)));
    }
}
