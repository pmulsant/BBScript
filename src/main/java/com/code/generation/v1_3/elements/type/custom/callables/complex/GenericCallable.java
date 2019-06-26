package com.code.generation.v1_3.elements.type.custom.callables.complex;

import com.code.generation.v1_3.elements.strong_type.CanBeParameterType;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.Callable;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.List;

public abstract class GenericCallable extends Callable implements IGenericCallable {
    protected TypeInferenceMotor typeInferenceMotor;
    protected List<StandardType> standardTypeParameters;
    protected StandardTypeDirectory standardTypeDirectory;

    public GenericCallable(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor, List<StandardType> standardTypeParameters) {
        super(standardTypeParameters.size());
        this.standardTypeDirectory = standardTypeDirectory;
        this.typeInferenceMotor = typeInferenceMotor;
        this.standardTypeParameters = standardTypeParameters;
        register();
    }

    protected abstract void register();

    @Override
    public final boolean processLinkStandardTypeParameters(List<? extends Typable> typableArguments) {
        int index = 0;
        boolean deduced = true;
        for (StandardType standardTypeParameter : standardTypeParameters) {
            if (!fusionIfNonNull(typableArguments.get(index), standardTypeParameter)) {
                deduced = false;
            }
            index++;
        }
        return deduced;
    }

    public boolean fusionIfNonNull(Typable typable, StandardType standardType) {
        if (standardType != null) {
            typeInferenceMotor.addFusionOfTypesDeclaration(typable, standardType.getOriginalTypable());
            return true;
        }
        return false;
    }
}
