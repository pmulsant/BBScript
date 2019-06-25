package com.code.generation.v1_3.util.for_test.organization.code_folder_processor;

import com.code.generation.v1_3.util.for_test.organization.BaseTestFolderOrganization;
import com.code.generation.v1_3.util.for_test.organization.ICodeConsumer;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public interface ITriCodeFolderProcessor extends ICodeFolderProcessor {
    static ITriCodeFolderProcessor instance(File sourcesFolder, File originalResultsFolder){
        return new TriCodeFolderProcessorImpl(sourcesFolder, originalResultsFolder);
    }

    File getInitialSourcesFolder();
    File getInitialResultsFolder();

    default void process(ICodeConsumer consumer) throws NotCompatibleFoldersException {
        process(getInitialSourcesFolder(), getInitialResultsFolder(), consumer);
    }

    default void process(File innerSourceFolder, File innerResultFolder, ICodeConsumer consumer) throws NotCompatibleFoldersException {
        boolean isFirstFileCodeFolder = ICodeFolderProcessor.isSourceOrResultFolder(innerSourceFolder);
        boolean isSecondFileCodeFolder = ICodeFolderProcessor.isSourceOrResultFolder(innerResultFolder);
        if(isFirstFileCodeFolder != isSecondFileCodeFolder){
            throw new IllegalStateException("not same folders type");
        }
        if(isFirstFileCodeFolder && isSecondFileCodeFolder){
            consumer.accept(innerSourceFolder, BaseTestFolderOrganization.getTempTargetFolder(innerSourceFolder), innerResultFolder);
            return;
        }
        processNonCodeFolder(innerSourceFolder, innerResultFolder, consumer);
    }

    default void processNonCodeFolder(File innerSourceFolder, File innerResultFolder, ICodeConsumer consumer) throws NotCompatibleFoldersException {
        Set<String> fileNameSet = getFileNameSet(innerSourceFolder, innerResultFolder);
        if(fileNameSet == null){
            throw new NotCompatibleFoldersException();
        }
        for (String fileName : fileNameSet) {
            File firstFile = new File(innerSourceFolder.getAbsolutePath() + "/" + fileName);
            File secondFile = new File(innerResultFolder.getAbsolutePath() + "/" + fileName);
            process(firstFile, secondFile, consumer);
        }
    }

    default Set<String> getFileNameSet(File firstFolder, File secondFolder) {
        Set<String> firstNames = Arrays.stream(firstFolder.listFiles()).map(file -> file.getName()).collect(Collectors.toSet());
        Set<String> secondNames = Arrays.stream(secondFolder.listFiles()).map(file -> file.getName()).collect(Collectors.toSet());
        if(!firstNames.equals(secondNames)){
            return null;
        }
        return firstNames;
    }
}
