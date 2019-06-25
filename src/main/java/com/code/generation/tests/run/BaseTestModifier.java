package com.code.generation.tests.run;

import com.code.generation.tests.RunResult;
import com.code.generation.tests.TestRunner;
import com.code.generation.tests.exception.WrongArgNumberException;
import com.code.generation.tests.exception.WrongArgsException;
import com.code.generation.tests.exception.WrongPasswordException;
import com.code.generation.v1_3.exception.FileException;
import com.code.generation.v1_3.main.Main;
import com.code.generation.v1_3.util.FileUtil;
import com.code.generation.v1_3.util.for_test.organization.BaseTestFolderOrganization;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.Arrays;

public class BaseTestModifier {
    public static void main(String[] args) throws IOException, WrongPasswordException, WrongArgsException, WrongArgNumberException {
        switch (args.length) {
            case 1:
                if (args[0].equals("all")) {
                    rebuildAll();
                    return;
                }
                throw new WrongArgsException(args);
            case 2:
                if (args[0].equals("rebuild")) {
                    String prefix = args[1];
                    rebuild(prefix);
                    return;
                }
                throw new WrongArgsException(args);
            case 3:
                String goal = args[0];
                String codePrefix = args[1];
                String codeName = args[2];
                if(goal.equals("remove")){
                    removeCode(codePrefix, codeName);
                    return;
                }
                if (goal.equals("add")) {
                    addCode(codePrefix, codeName);
                    return;
                }
                if(goal.equals("rebuild")){
                    rebuildOne(codePrefix, codeName);
                    return;
                }
                throw new WrongArgsException(args);
            case 5:
                if (args[0].equals("replace")) {
                    String oldCodePrefix = args[1];
                    String oldCodeName = args[2];
                    String newCodePrefix = args[3];
                    String newCodeName = args[4];
                    replaceCode(oldCodePrefix, oldCodeName, newCodePrefix, newCodeName);
                    return;
                }
                throw new WrongArgsException(args);
            default:
                throw new WrongArgNumberException(args);
        }
    }

    private static void rebuildAll() throws WrongPasswordException, IOException {
        rebuild(null);
    }

    private static void rebuild(String prefix) throws WrongPasswordException, IOException {
        assertWantContinue();
        BaseTestFolderOrganization.removeResultsFolder();
        BaseTestFolderOrganization.getResultsRebuildCodeFolderProcessor(prefix).process(((sourceFolder, targetFolder) -> {
            build(sourceFolder, targetFolder);
        }));
    }

    private static void removeCode(String codePrefix, String codeName) throws NotDirectoryException {
        File sourceFolder = getSourceCodeFolder(true, BaseTestFolderOrganization.SOURCES_FOLDER_NAME, codePrefix, codeName);
        File resultCodeFolder = BaseTestFolderOrganization.getResultTargetFolder(sourceFolder);
        File tempCodeFolder = BaseTestFolderOrganization.getTempTargetFolder(sourceFolder);
        BaseTestFolderOrganization.removeFolderForce(sourceFolder);
        BaseTestFolderOrganization.removeFolderForce(resultCodeFolder);
        BaseTestFolderOrganization.removeFolderForce(tempCodeFolder);
        //BaseTestFolderOrganization.clean_BaseTest_AllButNotSourceOrResultFolder();
    }

    private static void addCode(String codePrefix, String codeName) throws IOException {
        assertWantContinue();
        File initialSourceFolder = new File(Main.CODE_SOURCE_FOLDER_NAME);
        File sourceCodeFolder = getSourceCodeFolder(false, BaseTestFolderOrganization.SOURCES_FOLDER_NAME, codePrefix, codeName);
        for (File file : FileUtil.getSubEls(initialSourceFolder)) {
            FileUtils.copyFileToDirectory(file, sourceCodeFolder);
        }
        File resultCodeFolder = BaseTestFolderOrganization.getResultTargetFolder(sourceCodeFolder);
        build(sourceCodeFolder, resultCodeFolder);
    }

    private static void rebuildOne(String codePrefix, String codeName) throws NotDirectoryException {
        assertWantContinue();
        File sourceFolder = getSourceCodeFolder(true, BaseTestFolderOrganization.SOURCES_FOLDER_NAME, codePrefix, codeName);
        File resultCodeFolder = BaseTestFolderOrganization.getResultTargetFolder(sourceFolder);
        BaseTestFolderOrganization.removeFolderForce(resultCodeFolder);
        build(sourceFolder, resultCodeFolder);
    }

    private static void replaceCode(String oldCodePrefix, String oldCodeName, String newCodePrefix, String newCodeName) throws IOException, WrongPasswordException {
        File oldSourceFolder = getSourceCodeFolder(true, BaseTestFolderOrganization.SOURCES_FOLDER_NAME, oldCodePrefix, oldCodeName);
        File newSourceFolder = getSourceCodeFolder(false, BaseTestFolderOrganization.SOURCES_FOLDER_NAME, newCodePrefix, newCodeName);
        for (File file : FileUtil.getSubEls(oldSourceFolder)) {
            FileUtils.moveFileToDirectory(file, newSourceFolder, true);
        }
        BaseTestFolderOrganization.removeFolderForce(oldSourceFolder);
        build(newSourceFolder, BaseTestFolderOrganization.getResultTargetFolder(newSourceFolder));
    }

    private static void build(File sourceFolder, File resultCodeFolder) {
        System.out.println("build code : " + sourceFolder.getAbsolutePath());
        RunResult runResult = TestRunner.run(sourceFolder, resultCodeFolder);
        if (runResult.getMsg() != null) {
            throw new IllegalStateException(runResult.getMsg());
        }
    }

    private static File getSourceCodeFolder(boolean isExist, String localizationSources, String codePrefix, String codeName) throws NotDirectoryException {
        String path = String.join("/", Arrays.asList(localizationSources, codePrefix, BaseTestFolderOrganization.PREFIX_FOR_SOURCE_OR_RESULT_FOLDER + codeName));
        File file = new File(path);
        if(file.exists() != isExist){
            throw new FileException(file, (isExist ? "not " : "") + "exists");
        }
        if(!isExist){
            file.mkdirs();
        }
        if(!file.isDirectory()){
            throw new NotDirectoryException(file.getAbsolutePath());
        }
        return file;
    }

    private static void assertWantContinue(){
        if(!TestRunner.wantContinue()){
            throw new IllegalStateException();
        }
    }
}
