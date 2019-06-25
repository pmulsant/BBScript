package com.code.generation.v1_3.elements.type.custom.callables.simples;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class Parameter extends Typable {
    private String name;

    public Parameter(TypeInferenceMotor typeInferenceMotor) {
        super(typeInferenceMotor);
        initialize();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "parameter : " + name + " (" + getType() + ")";
    }
}
