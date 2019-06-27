package com.code.generation.v1_3.elements.type;

import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public abstract class Typable {
    private Type type;
    private TypeInferenceMotor typeInferenceMotor;

    public Typable(TypeInferenceMotor typeInferenceMotor) {
        type = new Type(typeInferenceMotor, this);
        this.typeInferenceMotor = typeInferenceMotor;
    }

    public Typable(TypeInferenceMotor typeInferenceMotor, StandardType existingType) {
        this.typeInferenceMotor = typeInferenceMotor;
        type = existingType;
        type.addTypable(this);
    }

    public void initialize(){
        typeInferenceMotor.addTypable(this);
    }

    public void setType(Type newType) {
        this.type = newType;
        newType.addTypable(this);
    }

    public Type getType() {
        return type;
    }

    public boolean canBeNull(){
        return false;
    }
}