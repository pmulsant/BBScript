package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;

public class NonOperableException extends RuntimeCustomException {
    public NonOperableException(String msg){
        super(msg);
    }
}
