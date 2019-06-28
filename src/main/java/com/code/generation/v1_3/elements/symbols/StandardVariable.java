package com.code.generation.v1_3.elements.symbols;

import com.code.generation.v1_3.elements.scope.GlobalScope;
import com.code.generation.v1_3.elements.type.standard.ProvidedCustomizableType;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class StandardVariable extends Variable {
    public StandardVariable(TypeInferenceMotor typeInferenceMotor, GlobalScope globalScope, String name, String typeName) {
        super(typeInferenceMotor, globalScope, name, null);
        setType(new ProvidedCustomizableType(typeInferenceMotor, typeName));
    }

    @Override
    public String toString() {
        return "standard variable : " + getName();
    }
}
