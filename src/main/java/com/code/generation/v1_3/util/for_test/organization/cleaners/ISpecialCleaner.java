package com.code.generation.v1_3.util.for_test.organization.cleaners;

import com.code.generation.v1_3.util.FileUtil;
import com.code.generation.v1_3.util.TreeComputer;
import com.code.generation.v1_3.util.for_test.organization.BaseTestFolderOrganization;
import com.code.generation.v1_3.util.for_test.organization.code_folder_processor.ICodeFolderProcessor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public interface ISpecialCleaner extends IFolderCleaner {
    static ISpecialCleaner instance(File fileOrFolder) {
        return new SpecialCleanerImpl(fileOrFolder);
    }

    File getCurrentFileOrFolder();

    @Override
    default void clean() {
        // return canClean
        TreeComputer<File, Boolean> treeComputer = new TreeComputer<>(
                getCurrentFileOrFolder(),
                fileOrFolder -> fileOrFolder.isFile() || ICodeFolderProcessor.isSourceOrResultFolder(fileOrFolder),
                aFileOrACodeFolder -> {
                    if (ICodeFolderProcessor.isSourceOrResultFolder(aFileOrACodeFolder)) {
                        return false;
                    }
                    aFileOrACodeFolder.delete();
                    return true;
                },
                fileOrFolder -> FileUtil.getSubEls(fileOrFolder),
                (aFolder, subResults) -> {
                    boolean canClean = subResults.stream().allMatch(aResult -> aResult);
                    if (canClean) {
                        BaseTestFolderOrganization.removeFolderForce(aFolder);
                    }
                    return canClean;
                }
        );
        treeComputer.compute();
    }

    default void clean_old() {
        File file = getCurrentFileOrFolder();
        if (file.isFile()) {
            file.delete();
            return;
        }
        File[] subEls = file.listFiles();
        if (subEls == null || subEls.length == 0) {
            BaseTestFolderOrganization.removeFolderForce(file);
        }
        boolean removable = false;
        for (File subEl : subEls) {
            if (cleanAndReturnRemovable(subEl)) {
                removable = true;
            }
        }
        if (removable) {
            BaseTestFolderOrganization.removeFolderForce(file);
        }
    }

    default boolean cleanAndReturnRemovable(File fileOrFolder) {
        if (ICodeFolderProcessor.isSourceOrResultFolder(fileOrFolder)) {
            return false;
        }
        if (fileOrFolder.isFile()) {
            removeFileOrFolder(fileOrFolder);
            return true;
        }
        File[] subEls = fileOrFolder.listFiles();
        if (subEls == null || subEls.length == 0) {
            removeFileOrFolder(fileOrFolder);
            return true;
        }
        boolean removable = true;
        List<File> toRemoves = new LinkedList<>();
        for (File subEl : subEls) {
            if (cleanAndReturnRemovable(subEl)) {
                toRemoves.add(subEl);
            }
            removable = false;
        }
        for (File toRemove : toRemoves) {
            removeFileOrFolder(toRemove);
        }
        if (removable) {
            removeFileOrFolder(fileOrFolder);
        }
        return removable;
    }

    default void removeFileOrFolder(File fileOrFolder) {
        if (fileOrFolder.isFile()) {
            fileOrFolder.delete();
            return;
        }
        BaseTestFolderOrganization.removeFolderForce(fileOrFolder);
    }
}
