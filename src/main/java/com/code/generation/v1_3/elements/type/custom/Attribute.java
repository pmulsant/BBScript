package com.code.generation.v1_3.elements.type.custom;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class Attribute extends Typable {
    private String name;
    private boolean isStrong = false;

    public Attribute(TypeInferenceMotor typeInferenceMotor, String name) {
        super(typeInferenceMotor);
        this.name = name;
        initialize();
    }

    public String getName() {
        return name;
    }

    public void setStrong() {
        isStrong = true;
    }

    public boolean isStrong() {
        return isStrong;
    }
}
