package com.code.generation.v1_3.elements.type.standard.callables.for_lists.methods;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class AddGenericMethod extends GenericMethod {
    private static final String NAME = "add";

    public AddGenericMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, NAME, null, standardTypeDirectory.getStandardType(StandardKnowledges.VOID_TYPE_NAME), Collections.singletonList(null));
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        Typable innerListTypable = innerTypable.getType().setList();
        typeInferenceMotor.addFusionOfTypesDeclaration(typableArguments.get(0), innerListTypable);
    }
}
