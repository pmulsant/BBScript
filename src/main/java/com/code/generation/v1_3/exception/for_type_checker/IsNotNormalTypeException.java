package com.code.generation.v1_3.exception.for_type_checker;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.StrongType;

public class IsNotNormalTypeException extends RuntimeCustomException {
    public IsNotNormalTypeException(StrongType strongType) {
        super(strongType.toString());
    }
}
