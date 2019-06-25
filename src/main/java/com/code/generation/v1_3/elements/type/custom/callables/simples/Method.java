package com.code.generation.v1_3.elements.type.custom.callables.simples;

import com.code.generation.v1_3.elements.type.ReturnedTypable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.custom.callables.IMethod;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class Method extends SimpleCallable implements LinkedToType, CanReturn, IMethod {
    private String name;
    protected ReturnedTypable returnedTypable;

    public Method(TypeInferenceMotor typeInferenceMotor, Type type, String name, int paramsNumber) {
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

    @Override
    public String toString() {
        return "method " + name;
    }
}
