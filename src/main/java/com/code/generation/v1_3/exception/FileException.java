package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;

import java.io.File;

public class FileException extends RuntimeCustomException {
    public FileException(File file, String msg) {
        super(file.getAbsolutePath() + " " + msg);
    }
}
