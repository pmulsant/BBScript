package com.code.generation.v1_3.elements.type.custom.callables.simples;

import com.code.generation.v1_3.elements.type.ReturnedTypable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class Lambda extends SimpleCallable implements CanReturn {
    private ReturnedTypable returnedTypable;

    public Lambda(TypeInferenceMotor typeInferenceMotor, int paramsNumber) {
        super(typeInferenceMotor, paramsNumber);
        returnedTypable = new ReturnedTypable(typeInferenceMotor);
    }

    @Override
    public ReturnedTypable getReturnedTypable() {
        return returnedTypable;
    }
}
