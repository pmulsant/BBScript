package com.code.generation.tests.exception;

import com.code.generation.CustomException;

public class WrongArgsException extends CustomException {
    public WrongArgsException(String[] args) {
        super(String.join(" , ", args));
    }
}
