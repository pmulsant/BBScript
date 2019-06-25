package com.code.generation.v1_3.elements.type.standard.simple_types;

import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.elements.type.standard.callables.for_regex.RegexGenericConstructor;
import com.code.generation.v1_3.elements.type.standard.callables.for_regex.TestStandardMethod;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class RegexStandardType extends StandardType {
    public RegexStandardType(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, StandardKnowledges.REGEX_TYPE_NAME);
    }

    @Override
    public void build() {
        addConstructor(new RegexGenericConstructor(standardTypeDirectory, typeInferenceMotor));
        addMethod(new TestStandardMethod(standardTypeDirectory, typeInferenceMotor));
    }
}
