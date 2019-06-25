package com.code.generation.tests.exception;

import com.code.generation.CustomException;

public class WrongArgNumberException extends CustomException {
    public WrongArgNumberException(String[] args){
        super(String.join(" , " + args));
    }
}
