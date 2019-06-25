package com.code.generation.v1_3.util.for_test.organization.folder_processors;

import com.code.generation.v1_3.util.for_test.organization.FolderComparator;

import java.io.*;

public interface IFolderComparator {
    static IFolderComparator instance(File firstFolder, File secondFolder) {
        return new FolderComparator(firstFolder, secondFolder);
    }

    File getFirstFolder();
    File getSecondFolder();

    BufferedReader getCurrentFirstReader();
    BufferedReader getCurrentSecondReader();

    void setCurrentFirstReader(BufferedReader reader);
    void setCurrentSecondReader(BufferedReader reader);

    default boolean compare() {
        boolean result = compareFilesOrFolders(getFirstFolder(), getSecondFolder());
        close();
        return result;
    }

    default void close() {
        try {
            BufferedReader currentFirstReader = getCurrentFirstReader();
            if(currentFirstReader != null) {
                currentFirstReader.close();
            }
            BufferedReader currentSecondReader = getCurrentSecondReader();
            if(currentSecondReader!= null) {
                currentSecondReader.close();
            }
        } catch (IOException e){
        }
    }

    default boolean compareFilesOrFolders(File firstFile, File secondFile) {
        if(firstFile.isDirectory() != secondFile.isDirectory()){
            return false;
        }
        if(!firstFile.isDirectory()){
            return comparePureFiles(firstFile, secondFile);
        }
        try {
            return computePureFolders(firstFile, secondFile);
        } catch (NotCompatibleFoldersException e) {
            return false;
        }
    }

    default boolean computePureFolders(File firstFolder, File secondFolder) throws NotCompatibleFoldersException {
        IBiFolderProcessor biFolderProcessor = IBiFolderProcessor.instance(firstFolder, secondFolder);
        boolean[] isOneCompareFailded = {false};
        biFolderProcessor.process((firstFile, secondFile) -> {
            if(!compareFilesOrFolders(firstFile, secondFile)){
                isOneCompareFailded[0] = true;
            }
        });
        return !isOneCompareFailded[0];
    }

    default boolean comparePureFiles(File originalFile, File tempFile) {
        try {
            setCurrentFirstReader(new BufferedReader(new InputStreamReader(new FileInputStream(originalFile))));
            setCurrentSecondReader(new BufferedReader(new InputStreamReader(new FileInputStream(tempFile))));
            return compareReaders(getCurrentFirstReader(), getCurrentSecondReader());
        } catch (IOException e){
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
    }

    default boolean compareReaders(BufferedReader originalReader, BufferedReader tempReader) throws IOException {
        String originalLine;
        String tempLine = null;
        while(true){
            originalLine = originalReader.readLine();
            tempLine = tempReader.readLine();
            if(originalLine == null && tempLine == null){
                // both finish properly
                break;
            }
            boolean isSameLine = originalLine != null && tempLine != null && originalLine.equals(tempLine);
            if(!isSameLine){
                return false;
            }
        }
        return true;
    }
}
