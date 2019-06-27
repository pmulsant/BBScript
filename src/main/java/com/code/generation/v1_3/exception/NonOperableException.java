package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.type.Type;

public class NonOperableException extends RuntimeCustomException {
    public NonOperableException(){
        this("there must be one string");
    }

    public NonOperableException(Type type) {
        this(type + " is not an operable");
    }

    public NonOperableException(String msg){
        super(msg);
    }
}
