package com.code.generation.v1_3.exception.for_callables;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.type.custom.callables.Callable;
import com.code.generation.v1_3.elements.type.standard.StandardType;

public class NotStandardCallableForThisTypeException extends RuntimeCustomException {
    public NotStandardCallableForThisTypeException(StandardType standardType, Callable callable) {
        super("no " + callable + " for standard type " + standardType.getSimpleName());
    }
}
