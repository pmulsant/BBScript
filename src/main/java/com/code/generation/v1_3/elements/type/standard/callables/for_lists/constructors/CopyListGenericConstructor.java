package com.code.generation.v1_3.elements.type.standard.callables.for_lists.constructors;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class CopyListGenericConstructor extends GenericConstructor {
    public CopyListGenericConstructor(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, null, Collections.singletonList(null));
    }

    @Override
    protected void processLinksSpecial(Typable topTypable, List<? extends Typable> typableArguments) {
        Typable innerListTopTypable = topTypable.getType().setList();
        Typable innerListReturnedTypable = typableArguments.get(0).getType().setList();
        typeInferenceMotor.addFusionOfTypesDeclaration(innerListReturnedTypable, innerListTopTypable);
    }
}
