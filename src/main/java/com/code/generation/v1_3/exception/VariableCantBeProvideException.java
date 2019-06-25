package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.symbols.Variable;

public class VariableCantBeProvideException extends RuntimeCustomException {
    public VariableCantBeProvideException(Variable variable) {
        super(variable.getName());
    }
}
