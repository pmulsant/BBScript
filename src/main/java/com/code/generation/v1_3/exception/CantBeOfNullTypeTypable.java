package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.type.Typable;

public class CantBeOfNullTypeTypable extends RuntimeCustomException {
    public CantBeOfNullTypeTypable(Typable typable) {
        super(typable.toString());
    }
}
