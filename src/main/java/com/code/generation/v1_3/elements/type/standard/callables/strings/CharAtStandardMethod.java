package com.code.generation.v1_3.elements.type.standard.callables.strings;

import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.elements.type.standard.callables.StandardMethod;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CharAtStandardMethod extends StandardMethod {
    private static final String METHOD_NAME = "charAt";
    private static final String INNER_TYPE_NAME = StandardKnowledges.STRING_TYPE_NAME;
    private static final String RETURN_TYPE_NAME = StandardKnowledges.CHAR_TYPE_NAME;
    private static final List<String> PARAMETER_TYPE_NAMES = Collections.singletonList(StandardKnowledges.INT_TYPE_NAME);

    public CharAtStandardMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, METHOD_NAME, getInnerStandardType(standardTypeDirectory),
                getReturnedStandardType(standardTypeDirectory), getParameterStandardTypes(standardTypeDirectory));
    }

    private static StandardType getInnerStandardType(StandardTypeDirectory directory) {
        return directory.getStandardType(INNER_TYPE_NAME);
    }

    private static StandardType getReturnedStandardType(StandardTypeDirectory directory) {
        return directory.getStandardType(RETURN_TYPE_NAME);
    }

    private static List<StandardType> getParameterStandardTypes(StandardTypeDirectory directory) {
        return PARAMETER_TYPE_NAMES.stream().map(typeName -> directory.getStandardType(typeName)).collect(Collectors.toList());
    }
}
