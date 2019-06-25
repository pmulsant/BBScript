package com.code.generation.v1_3.main;

import com.code.generation.v1_3.elements.build.BuiltData;
import com.code.generation.v1_3.elements.scope.GlobalScope;

import java.io.File;
import java.io.IOException;

public class Main {
    public static final String CODE_SOURCE_FOLDER_NAME = "code_source";
    public static final String CODE_TARGET_FOLDER_NAME = "code_target";

    public static void main(String[] args) throws IOException {
        File sourceDirectory = new File(CODE_SOURCE_FOLDER_NAME);
        GlobalScope globalScope = new GlobalScope(sourceDirectory);
        BuiltData builtData = globalScope.build();
        builtData.compile(new File(CODE_TARGET_FOLDER_NAME));
    }
}
