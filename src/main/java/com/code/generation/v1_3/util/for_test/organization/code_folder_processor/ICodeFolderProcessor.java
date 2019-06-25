package com.code.generation.v1_3.util.for_test.organization.code_folder_processor;

import com.code.generation.v1_3.util.FileUtil;
import com.code.generation.v1_3.util.for_test.organization.BaseTestFolderOrganization;

import java.io.File;

public interface ICodeFolderProcessor {
    static boolean isSourceOrResultFolder(File aFileOrFolder){
        return FileUtil.processFileOrFolder(Boolean.class,
                aFileOrFolder,
                aFile -> false,
                aFolder -> aFolder.getName().startsWith(BaseTestFolderOrganization.PREFIX_FOR_SOURCE_OR_RESULT_FOLDER));
    }
}
