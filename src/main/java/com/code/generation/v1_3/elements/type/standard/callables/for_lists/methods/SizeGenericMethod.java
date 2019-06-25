package com.code.generation.v1_3.elements.type.standard.callables.for_lists.methods;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class SizeGenericMethod extends GenericMethod {
    public static final String METHOD_NAME = "size";

    public SizeGenericMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, METHOD_NAME, null, standardTypeDirectory.getStandardType(StandardKnowledges.INT_TYPE_NAME), Collections.EMPTY_LIST);
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        innerTypable.getType().setList();
    }
}
