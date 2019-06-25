package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;

public class NonAssignableExpression extends RuntimeCustomException {
    public NonAssignableExpression() {
        super("can't assign a non assignable");
    }
}
