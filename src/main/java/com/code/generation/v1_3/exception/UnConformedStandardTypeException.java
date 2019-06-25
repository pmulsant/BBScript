package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.standard.StandardType;

public class UnConformedStandardTypeException extends RuntimeCustomException {
    public UnConformedStandardTypeException(StandardType standardType, Type type) {
        super(standardType + " vs " + type);
    }
}
