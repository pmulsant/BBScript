package com.code.generation.v1_3.elements.type.custom.callables;

import com.code.generation.v1_3.exception.WrongParamNumberException;

public class Callable implements ICallable {
    private int paramsNumber;

    public Callable(int paramsNumber) {
        this.paramsNumber = paramsNumber;
    }

    @Override
    public void assertRightParamsNumber(int paramsNumber) {
        if (this.paramsNumber != paramsNumber) {
            throw new WrongParamNumberException(this.paramsNumber, paramsNumber);
        }
    }

    @Override
    public int getParamsNumber() {
        return paramsNumber;
    }
}
