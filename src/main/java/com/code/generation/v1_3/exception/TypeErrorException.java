package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;

public class TypeErrorException extends RuntimeCustomException {
    public TypeErrorException(String msg) {
        super(msg);
    }
}
