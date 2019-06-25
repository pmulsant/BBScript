package com.code.generation.v1_3.elements.type.standard.callables;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.List;

public abstract class StandardMethod extends GenericMethod {
    protected StandardTypeDirectory standardTypeDirectory;

    public StandardMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor, String name, StandardType standardInnerType, StandardType standardReturnedType, List<StandardType> standardTypeParameters) {
        super(standardTypeDirectory, typeInferenceMotor, name, standardInnerType, standardReturnedType, standardTypeParameters);
    }

    @Override
    protected final void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
    }
}
