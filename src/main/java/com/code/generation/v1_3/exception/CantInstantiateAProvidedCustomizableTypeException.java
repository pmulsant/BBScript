package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;

public class CantInstantiateAProvidedCustomizableTypeException extends RuntimeCustomException {
    public CantInstantiateAProvidedCustomizableTypeException(String typeName) {
        super(typeName);
    }
}
