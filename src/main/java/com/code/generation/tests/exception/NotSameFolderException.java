package com.code.generation.tests.exception;

import com.code.generation.CustomException;

import java.io.File;

public class NotSameFolderException extends CustomException {
    public NotSameFolderException(File file1, File file2){
        super("different files " + file1.getAbsolutePath() + " , " + file2.getAbsolutePath());
    }
}
