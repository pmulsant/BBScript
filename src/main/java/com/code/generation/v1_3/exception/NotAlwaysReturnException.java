package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.callables.Callable;

public class NotAlwaysReturnException extends RuntimeCustomException {
    public NotAlwaysReturnException(Callable callable) {
        super(callable.toString());
    }
}
