package com.code.generation.v1_3.elements.type.custom.callables.simples;

import com.code.generation.v1_3.elements.type.ReturnedTypable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class Function extends SimpleCallable implements CanReturn {
    private String name;
    private ReturnedTypable returnedTypable;

    public Function(TypeInferenceMotor typeInferenceMotor, String name, int paramsNumber) {
        super(typeInferenceMotor, paramsNumber);
        this.name = name;
        returnedTypable = new ReturnedTypable(typeInferenceMotor);
    }

    @Override
    public ReturnedTypable getReturnedTypable() {
        return returnedTypable;
    }

    public String getName() {
        return name;
    }
}
