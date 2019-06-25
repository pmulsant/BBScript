package com.code.generation.v1_3.util.for_test.organization.cleaners;

import java.io.File;

public class SpecialCleanerImpl implements ISpecialCleaner {
    private File fileOrFolder;

    public SpecialCleanerImpl(File fileOrFolder) {
        this.fileOrFolder = fileOrFolder;
    }

    @Override
    public File getCurrentFileOrFolder() {
        return fileOrFolder;
    }
}
