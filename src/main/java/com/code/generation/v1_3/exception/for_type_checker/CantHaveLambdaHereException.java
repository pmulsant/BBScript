package com.code.generation.v1_3.exception.for_type_checker;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.callables.ICallable;

public class CantHaveLambdaHereException extends RuntimeCustomException {
    public CantHaveLambdaHereException(ICallable callable) {
        super(callable == null ? "lambda are only use for parameters" : "lambda can't be parameter of " + callable.toString());
    }
}
