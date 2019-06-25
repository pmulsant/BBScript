package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.symbols.Variable;

public class VariableAlreadyDefinedException extends RuntimeCustomException {
    public VariableAlreadyDefinedException(Variable variable) {
        this(variable.getName());
    }

    public VariableAlreadyDefinedException(String variableName) {
        super(variableName);
    }
}
