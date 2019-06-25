package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.StrongType;

public class WrongTypeException extends RuntimeCustomException {
    public WrongTypeException(StrongType expectedType, StrongType type) {
        super("expected " + expectedType + " vs " + type);
    }
}
