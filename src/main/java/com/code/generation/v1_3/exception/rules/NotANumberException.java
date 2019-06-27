package com.code.generation.v1_3.exception.rules;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.type.Type;

public class NotANumberException extends RuntimeCustomException {
    public NotANumberException(Type type) {
        super(type + " is not a number");
    }
}
