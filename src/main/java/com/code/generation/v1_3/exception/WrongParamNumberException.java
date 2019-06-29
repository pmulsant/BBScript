package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;

import java.util.Arrays;

public class WrongParamNumberException extends RuntimeCustomException {
    public WrongParamNumberException(String msg){
        super(msg);
    }

    public WrongParamNumberException(int number1, int number2) {
        super((number1 <= number2 ? number1 : number2) + " vs " + (number2 >= number1 ? number2 : number1));
    }

    public WrongParamNumberException(GenericConstructor genericConstructor, int paramsNumber) {
        super("no generic constructor " + genericConstructor + " with param number" + paramsNumber);
    }
}
