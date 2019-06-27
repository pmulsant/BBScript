package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;

public class WrongUsageOfThisException extends RuntimeCustomException {
    public WrongUsageOfThisException() {
        super("this can't be used outside a constructor or a method");
    }
}
