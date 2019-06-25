package com.code.generation.v1_3.util.for_test.organization.cleaners;

import java.io.File;

public class NormalCleanerImpl implements INormalCleaner {
    private File fileOrFolder;

    public NormalCleanerImpl(File fileOrFolder) {
        this.fileOrFolder = fileOrFolder;
    }

    @Override
    public File getCurrentFileOrFolder() {
        return fileOrFolder;
    }
}
