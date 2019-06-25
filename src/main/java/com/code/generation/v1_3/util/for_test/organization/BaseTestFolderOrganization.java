package com.code.generation.v1_3.util.for_test.organization;

import com.code.generation.tests.exception.WrongPasswordException;
import com.code.generation.v1_3.util.for_test.organization.cleaners.INormalCleaner;
import com.code.generation.v1_3.util.for_test.organization.cleaners.ISpecialCleaner;
import com.code.generation.v1_3.util.for_test.organization.code_folder_processor.IBiCodeFolderProcessorForRebuild;
import com.code.generation.v1_3.util.for_test.organization.code_folder_processor.ICodeFolderProcessor;
import com.code.generation.v1_3.util.for_test.organization.code_folder_processor.ITriCodeFolderProcessor;

import javax.swing.*;
import java.io.File;

public class BaseTestFolderOrganization {
    private static final String PASS_CODE = "pass";

    private static final String BASE_TEST_FOLDER_NAME = "base_test";
    public static final String PREFIX_FOR_SOURCE_OR_RESULT_FOLDER = "code_";
    public static final String SOURCES = "sources";
    public static final String RESULTS = "results";
    public static final String TEMP = "temp";

    private static final String TEMP_FOLDER_NAME = BASE_TEST_FOLDER_NAME + "/" + TEMP;
    public static final String SOURCES_FOLDER_NAME = BASE_TEST_FOLDER_NAME + "/" + SOURCES;
    public static final String RESULTS_FOLDER_NAME = BASE_TEST_FOLDER_NAME + "/results";

    public static IBiCodeFolderProcessorForRebuild getResultsRebuildCodeFolderProcessor(String prefix) throws WrongPasswordException {
        assertCorrectPassword();
        String suffix = (prefix == null ? "" : "/" + prefix);
        File sourcesFolder = new File(SOURCES_FOLDER_NAME + suffix);
        File resultsFolder = new File(RESULTS_FOLDER_NAME + suffix);
        return IBiCodeFolderProcessorForRebuild.instance(sourcesFolder, resultsFolder);
    }

    public static void assertCorrectPassword() throws WrongPasswordException {
        String response = JOptionPane.showInputDialog(null, "this run can remove folders in base_test", "WARNING", JOptionPane.WARNING_MESSAGE);
        if (response == null || !response.equals(PASS_CODE)) {
            throw new WrongPasswordException(response);
        }
    }

    public static ITriCodeFolderProcessor getCodeFolderProcessor() {
        File sourcesFolder = new File(SOURCES_FOLDER_NAME);
        File resultsFolder = new File(RESULTS_FOLDER_NAME);
        return ITriCodeFolderProcessor.instance(sourcesFolder, resultsFolder);
    }

    public static File getTempTargetFolder(File innerSourceFolder) {
        return getEquivalentTargetFolder(innerSourceFolder, true);
    }

    public static File getResultTargetFolder(File innerSourceFolder) {
        return getEquivalentTargetFolder(innerSourceFolder, false);
    }

    private static File getEquivalentTargetFolder(File innerSourceFolder, boolean tempTarget) {
        String absolutePath = innerSourceFolder.getAbsolutePath();
        String replacement = tempTarget ? TEMP : RESULTS;
        String tempTargetAbsolutePath = absolutePath.replaceFirst(SOURCES, replacement);
        return new File(tempTargetAbsolutePath);
    }


    public static void removeTempFolders() {
        removeFolder(new File(TEMP_FOLDER_NAME));
    }


    /*public static void clean_BaseTest_AllButNotSourceOrResultFolder() {
        cleanAllButNotSourceOrResultFolder(new File(BASE_TEST_FOLDER_NAME));
    }*/

    public static void clean_tempFolder_AllButNotSourceOrResultFolder() {
        cleanAllButNotSourceOrResultFolder(new File(TEMP_FOLDER_NAME));
    }

    public static void cleanAllButNotSourceOrResultFolder(File aFolder) {
        ISpecialCleaner.instance(aFolder).clean();
    }


    public static void cleanTempFolder() {
        cleanEmptyFolders(new File(TEMP_FOLDER_NAME));
    }

    public static void cleanEmptyFolders(File aFolder) {
        INormalCleaner.instance(aFolder).clean();
    }

    private static void removeIfNoMoreSubFiles(File aFolder) {
        if (!aFolder.isDirectory()) {
            throw new IllegalStateException();
        }
        File[] files = aFolder.listFiles();
        boolean isEmptyFolder = files == null || files.length == 0;
        if (isEmptyFolder) {
            removeFolderForce(aFolder);
        }
        return;
    }

    public static void removeFolder(File folder) {
        if (folder.getAbsolutePath().contains(TEMP)) {
            removeFolderForce(folder);
        }
    }

    public static void removeFolderForce(File folder) {
        deleteDir(folder);
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    public static void removeResultsFolder() {
        File folder = new File(RESULTS_FOLDER_NAME);
        if (folder.exists()) {
            removeFolderForce(folder);
        }
    }
}
