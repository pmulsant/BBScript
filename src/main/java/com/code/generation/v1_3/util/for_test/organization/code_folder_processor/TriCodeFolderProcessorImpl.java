package com.code.generation.v1_3.util.for_test.organization.code_folder_processor;

import java.io.File;

public class TriCodeFolderProcessorImpl implements ITriCodeFolderProcessor {
    private File initialFirstFolder;
    private File initialSecondFolder;

    public TriCodeFolderProcessorImpl(File initialFirstFolder, File initialSecondFolder) {
        this.initialFirstFolder = initialFirstFolder;
        this.initialSecondFolder = initialSecondFolder;
    }

    @Override
    public File getInitialSourcesFolder() {
        return initialFirstFolder;
    }

    @Override
    public File getInitialResultsFolder() {
        return initialSecondFolder;
    }
}
