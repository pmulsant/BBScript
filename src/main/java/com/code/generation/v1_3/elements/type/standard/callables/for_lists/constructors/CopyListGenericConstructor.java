package com.code.generation.v1_3.elements.type.standard.callables.for_lists.constructors;

import com.code.generation.v1_3.elements.strong_type.CanBeProvideForParameter;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.callables.Constructor;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;
import com.code.generation.v1_3.elements.type.standard.Operable;
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

    @Override
    public Constructor makeStrongConstructor(NormalType topType, List<CanBeProvideForParameter> arguments) {
        return new Constructor(topType.getStrongTypeDirectory(), topType, Collections.singletonList(new Parameter(null, topType)));
    }
}
