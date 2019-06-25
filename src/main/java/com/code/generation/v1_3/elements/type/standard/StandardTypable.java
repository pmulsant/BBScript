package com.code.generation.v1_3.elements.type.standard;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class StandardTypable extends Typable {
    public StandardTypable(TypeInferenceMotor typeInferenceMotor, StandardType standardType) {
        super(typeInferenceMotor, standardType);
        initialize();
    }

    @Override
    public String toString() {
        return "standard typable : " + getType().getSimpleName();
    }

    @Override
    public boolean canBeNull() {
        return true;
    }
}
