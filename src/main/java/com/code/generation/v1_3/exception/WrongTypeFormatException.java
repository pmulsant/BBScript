package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.StrongType;

public class WrongTypeFormatException extends RuntimeCustomException {
    public WrongTypeFormatException(StrongType strongType, String msg) {
        super(strongType.toString() + " " + msg);
    }
}
