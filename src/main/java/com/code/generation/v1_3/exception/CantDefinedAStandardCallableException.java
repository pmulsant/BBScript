package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;

public class CantDefinedAStandardCallableException extends RuntimeCustomException {
    public CantDefinedAStandardCallableException(String callableName) {
        super(callableName);
    }
}
