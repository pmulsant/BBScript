package com.code.generation.v1_3.elements.type.custom.callables.simples;

import com.code.generation.v1_3.elements.type.custom.callables.Callable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleCallable extends Callable implements ISimpleCallable {
    protected List<Parameter> parameters;

    public SimpleCallable(TypeInferenceMotor typeInferenceMotor, int paramsNumber) {
        super(paramsNumber);
        this.parameters = new ArrayList<>(paramsNumber);
        for (int index = 0; index < paramsNumber; index++) {
            parameters.add(new Parameter(typeInferenceMotor));
        }
    }

    @Override
    public Parameter getParameter(int index) {
        return parameters.get(index);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}
