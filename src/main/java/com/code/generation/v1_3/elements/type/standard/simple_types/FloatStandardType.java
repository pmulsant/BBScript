package com.code.generation.v1_3.elements.type.standard.simple_types;

import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class FloatStandardType extends StandardType {
    public FloatStandardType(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, StandardKnowledges.FLOAT_TYPE_NAME);
    }
}
