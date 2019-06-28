package com.code.generation.v1_3.elements.type.standard;

import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class ProvidedCustomizableType extends Type {
    public ProvidedCustomizableType(TypeInferenceMotor typeInferenceMotor, String name) {
        super(typeInferenceMotor);
        simpleName = name;
    }

    @Override
    public String toString() {
        return "provided customizable type : " + simpleName;
    }
}
