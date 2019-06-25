package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.symbols.Variable;

public class VariableReadBeforeInitializationException extends RuntimeCustomException {
    public VariableReadBeforeInitializationException(Variable variable) {
        super(variable.getName());
    }
}
