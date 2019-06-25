package com.code.generation.tests.exception;

import com.code.generation.CustomException;

public class WrongPasswordException extends CustomException {
    public WrongPasswordException(String password) {
        super(password);
    }
}
