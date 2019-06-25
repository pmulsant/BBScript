package com.code.generation.v1_3.elements.type.standard.simple_types;

import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.elements.type.standard.callables.strings.CharAtStandardMethod;
import com.code.generation.v1_3.elements.type.standard.callables.strings.LengthStandardMethod;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class StringStandardType extends StandardType {
    public StringStandardType(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, StandardKnowledges.STRING_TYPE_NAME);
    }

    @Override
    public void build() {
        addMethod(new LengthStandardMethod(standardTypeDirectory, typeInferenceMotor));
        addMethod(new CharAtStandardMethod(standardTypeDirectory, typeInferenceMotor));
    }
}
