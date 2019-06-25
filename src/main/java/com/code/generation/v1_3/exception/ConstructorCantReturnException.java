package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.callables.Constructor;
import com.code.generation.v1_3.elements.type.custom.callables.ICallable;

public class ConstructorCantReturnException extends RuntimeCustomException {
    public ConstructorCantReturnException(ICallable callable){
        super(callable.toString());
    }

    public ConstructorCantReturnException(Constructor constructor) {
        super(constructor.toString());
    }
}
