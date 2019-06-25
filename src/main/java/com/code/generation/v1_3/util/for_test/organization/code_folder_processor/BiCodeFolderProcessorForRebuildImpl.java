package com.code.generation.v1_3.util.for_test.organization.code_folder_processor;

import java.io.File;

public class BiCodeFolderProcessorForRebuildImpl implements IBiCodeFolderProcessorForRebuild {
    private File initialSourcesFolder;
    private File initialOriginalResultsFolder;

    public BiCodeFolderProcessorForRebuildImpl(File initialSourcesFolder, File initialOriginalResultsFolder) {
        this.initialSourcesFolder = initialSourcesFolder;
        this.initialOriginalResultsFolder = initialOriginalResultsFolder;
    }

    @Override
    public File getInitialSourcesFolder() {
        return initialSourcesFolder;
    }

    @Override
    public File getInitialOriginalResultsFolder() {
        return initialOriginalResultsFolder;
    }
}
