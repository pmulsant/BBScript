package com.code.generation.v1_3.util.for_test.organization.folder_processors;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public interface IBiFolderProcessor extends IFolderProcessor {
    static IBiFolderProcessor instance(File firstFolder, File secondFolder) {
        return new BiFolderProcessor(firstFolder, secondFolder);
    }

    File getFirstFolder();
    File getSecondFolder();

    default void process(BiConsumer<File, File> consumer) throws NotCompatibleFoldersException {
        Set<String> fileNameSet = getFileNameSet();
        if(fileNameSet == null){
            throw new NotCompatibleFoldersException();
        }
        for (String fileName : fileNameSet) {
            File firstFile = new File(getFirstFolder().getAbsolutePath() + "/" + fileName);
            File secondFile = new File(getSecondFolder().getAbsolutePath() + "/" + fileName);
            consumer.accept(firstFile, secondFile);
        }
    }

    default Set<String> getFileNameSet() {
        Set<String> firstNames = Arrays.stream(getFirstFolder().listFiles()).map(file -> file.getName()).collect(Collectors.toSet());
        Set<String> secondNames = Arrays.stream(getSecondFolder().listFiles()).map(file -> file.getName()).collect(Collectors.toSet());
        if(!firstNames.equals(secondNames)){
            return null;
        }
        return firstNames;
    }
}
