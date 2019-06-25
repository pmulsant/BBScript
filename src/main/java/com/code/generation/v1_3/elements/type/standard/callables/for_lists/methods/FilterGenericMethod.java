package com.code.generation.v1_3.elements.type.standard.callables.for_lists.methods;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Lambda;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class FilterGenericMethod extends GenericMethod {
    public static final String METHOD_NAME = "filter";

    public FilterGenericMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, METHOD_NAME, null, null, Collections.EMPTY_LIST);
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        Typable innerInnerTypeListTypable = innerTypable.getType().setList();
        Typable innerReturnedListTypable = returnedTypable.getType().setList();
        Lambda lambda = typableArguments.get(0).getType().setLambda(1);
        typeInferenceMotor.addFusionOfTypesDeclaration(innerReturnedListTypable, innerInnerTypeListTypable);
        typeInferenceMotor.addFusionOfTypesDeclaration(lambda.getParameter(0), innerInnerTypeListTypable);
        fusionIfNonNull(lambda.getReturnedTypable(), standardTypeDirectory.getStandardType(StandardKnowledges.BOOLEAN_TYPE_NAME));
    }
}
