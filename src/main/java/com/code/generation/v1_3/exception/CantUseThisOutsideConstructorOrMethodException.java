package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;

public class CantUseThisOutsideConstructorOrMethodException extends RuntimeCustomException {
    public CantUseThisOutsideConstructorOrMethodException() {
        super("can't use this here");
    }
}
