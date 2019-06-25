package com.code.generation.v1_3.util.for_test.organization.code_folder_processor;

import com.code.generation.v1_3.util.for_test.organization.IBuilderConsumer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public interface IBiCodeFolderProcessorForRebuild extends ICodeFolderProcessor {
    static IBiCodeFolderProcessorForRebuild instance(File sourcesFolder, File originalResultsFolder){
        return new BiCodeFolderProcessorForRebuildImpl(sourcesFolder, originalResultsFolder);
    }

    File getInitialSourcesFolder();
    File getInitialOriginalResultsFolder();

    default void process(IBuilderConsumer consumer) throws IOException {
        File initialOriginalResultsFolder = getInitialOriginalResultsFolder();
        if(initialOriginalResultsFolder.exists()) {
            FileUtils.cleanDirectory(initialOriginalResultsFolder);
        }
        process(getInitialSourcesFolder(), initialOriginalResultsFolder, consumer);
    }

    default void process(File sourceFolder, File originalResultFolder, IBuilderConsumer consumer) {
        originalResultFolder.mkdirs();
        boolean isFirstFileCodeFolder = ICodeFolderProcessor.isSourceOrResultFolder(sourceFolder);
        if(isFirstFileCodeFolder){
            consumer.accept(sourceFolder, originalResultFolder);
            return;
        }
        processNonCodeFolder(sourceFolder, originalResultFolder, consumer);
    }

    default void processNonCodeFolder(File firstFolder, File secondFolder, IBuilderConsumer consumer) {
        File[] files = firstFolder.listFiles();
        if(files == null){
            return;
        }
        for (File subFile : files) {
            File firstFile = new File(firstFolder.getAbsolutePath() + "/" + subFile.getName());
            File secondFile = new File(secondFolder.getAbsolutePath() + "/" + subFile.getName());
            process(firstFile, secondFile, consumer);
        }
    }
}
