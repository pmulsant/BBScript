package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.type.Type;

public class CantFindTypeException extends RuntimeCustomException {
    public CantFindTypeException(Type type){
        super(type.toString());
    }
}
