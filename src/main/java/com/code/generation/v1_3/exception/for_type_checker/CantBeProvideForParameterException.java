package com.code.generation.v1_3.exception.for_type_checker;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.strong_type.StrongType;

public class CantBeProvideForParameterException extends RuntimeCustomException {
    public CantBeProvideForParameterException(StrongType strongType) {
        super(strongType.toString());
    }
}
