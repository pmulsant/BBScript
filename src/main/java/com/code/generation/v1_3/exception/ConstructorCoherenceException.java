package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.StrongType;

public class ConstructorCoherenceException extends RuntimeCustomException {

    public ConstructorCoherenceException(StrongType strongType) {
        super("can't build type : " + strongType);
    }
}
