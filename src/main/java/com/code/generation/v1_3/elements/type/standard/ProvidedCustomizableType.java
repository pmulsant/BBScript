package com.code.generation.v1_3.elements.type.standard;

import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Constructor;
import com.code.generation.v1_3.exception.CantInstantiateAProvidedCustomizableTypeException;
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

    @Override
    public void checkDeducedAndCoherence() {
        super.checkDeducedAndCoherence();
        if(!getConstructors().isEmpty()){
            throw new CantInstantiateAProvidedCustomizableTypeException(getSimpleName());
        }
    }

    @Override
    public boolean canBeReplaced() {
        return false;
    }
}
