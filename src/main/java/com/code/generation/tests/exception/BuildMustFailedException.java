package com.code.generation.tests.exception;

import com.code.generation.CustomException;

public class BuildMustFailedException extends CustomException {
    public BuildMustFailedException(){
        super("build must failed");
    }
}
