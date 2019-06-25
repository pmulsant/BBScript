package com.code.generation.v1_3.util.for_test.organization;

import com.code.generation.v1_3.util.for_test.organization.folder_processors.IFolderComparator;

import java.io.BufferedReader;
import java.io.File;

public class FolderComparator implements IFolderComparator {
    private File firstFolder;
    private File secondFolder;
    private BufferedReader firstReader;
    private BufferedReader secondReader;

    public FolderComparator(File firstFolder, File secondFolder) {
        this.firstFolder = firstFolder;
        this.secondFolder = secondFolder;
    }

    @Override
    public File getFirstFolder() {
        return firstFolder;
    }

    @Override
    public File getSecondFolder() {
        return secondFolder;
    }

    @Override
    public BufferedReader getCurrentFirstReader() {
        return firstReader;
    }

    @Override
    public BufferedReader getCurrentSecondReader() {
        return secondReader;
    }

    @Override
    public void setCurrentFirstReader(BufferedReader reader) {
        this.firstReader = reader;
    }

    @Override
    public void setCurrentSecondReader(BufferedReader reader) {
        this.secondReader = reader;
    }
}
