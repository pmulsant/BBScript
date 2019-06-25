package com.code.generation.v1_3.elements.build;

import com.code.generation.v1_3.writers.CodeDirectory;

import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;

public class EmptyBuildData extends BuiltData {
    public EmptyBuildData() {
        super(null, null, null);
    }

    @Override
    public void compile(File targetDirectory) throws IOException {
        CodeDirectory codeDirectory = new CodeDirectory(targetDirectory, new IdentityHashMap<>());
        codeDirectory.write();
    }
}
