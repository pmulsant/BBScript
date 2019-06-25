package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;

import java.io.File;

public class ParsingFailedException extends RuntimeCustomException {
    public ParsingFailedException(File codeFile) {
        super(codeFile.getAbsolutePath());
    }
}
