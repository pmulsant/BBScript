package com.code.generation.v1_3.elements.type.standard.callables.for_lists.methods;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class GetByIndexGenericMethod extends GenericMethod {
    public static final String METHOD_NAME = "get";

    public GetByIndexGenericMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, METHOD_NAME, null, null, Collections.singletonList(standardTypeDirectory.getStandardType(StandardKnowledges.INT_TYPE_NAME)));
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        Typable innerListTypable = innerTypable.getType().setList();
        typeInferenceMotor.addFusionOfTypesDeclaration(returnedTypable, innerListTypable);
    }
}
