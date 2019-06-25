package com.code.generation.v1_3.elements.type.custom.callables.simples;

import com.code.generation.v1_3.elements.type.custom.callables.IConstructor;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class Constructor extends SimpleCallable implements LinkedToType, IConstructor {
    public Constructor(TypeInferenceMotor typeInferenceMotor, int paramsNumber) {
        super(typeInferenceMotor, paramsNumber);
    }

    @Override
    public String toString() {
        return "constructor (" + getParamsNumber() + " params number";
    }
}
