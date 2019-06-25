package com.code.generation.v1_3.util.for_test.organization.folder_processors;

import java.io.File;

public class BiFolderProcessor implements IBiFolderProcessor {
    private File first;
    private File second;

    public BiFolderProcessor(File first, File second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public File getFirstFolder() {
        return first;
    }

    @Override
    public File getSecondFolder() {
        return second;
    }
}
