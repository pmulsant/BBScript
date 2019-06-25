package com.code.generation.v1_3.elements.type.standard.callables.functions;

import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;

public class HashFunction extends StandardFunction {
    private static final String FUNCTION_NAME = "hash";

    public HashFunction(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, FUNCTION_NAME, standardTypeDirectory.getStandardType(StandardKnowledges.STRING_TYPE_NAME), Collections.singletonList(standardTypeDirectory.getStandardType(StandardKnowledges.STRING_TYPE_NAME)));
    }
}
