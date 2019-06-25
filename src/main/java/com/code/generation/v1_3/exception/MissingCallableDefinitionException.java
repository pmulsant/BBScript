package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.callables.Callable;
import com.code.generation.v1_3.elements.strong_type.callables.Function;

public class MissingCallableDefinitionException extends RuntimeCustomException {
    public MissingCallableDefinitionException(Callable callable) {
        super(callable.toString());
    }
}
