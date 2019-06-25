package com.code.generation.v1_3.util.for_test.organization.cleaners;

import com.code.generation.v1_3.util.FileUtil;
import com.code.generation.v1_3.util.for_test.organization.BaseTestFolderOrganization;

import java.io.File;

public interface INormalCleaner extends IFolderCleaner {
    static INormalCleaner instance(File fileOrFolder) {
        return new NormalCleanerImpl(fileOrFolder);
    }

    File getCurrentFileOrFolder();

    default void clean(){
        // return canClean
        FileUtil.processFileOrFolderRecursive(Boolean.class,
                getCurrentFileOrFolder(),
                file -> false,
                (aFolder, subResults) -> {
                    boolean canClean = subResults.stream().allMatch(aResult -> aResult);
                    if(canClean) {
                        BaseTestFolderOrganization.removeFolderForce(aFolder);
                    }
                    return canClean;
                });
    }
}
