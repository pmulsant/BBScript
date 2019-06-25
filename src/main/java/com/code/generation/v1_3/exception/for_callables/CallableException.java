package com.code.generation.v1_3.exception.for_callables;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.callables.Callable;

public class CallableException extends RuntimeCustomException {
    public CallableException(String msg) {
        super(msg);
    }
}
