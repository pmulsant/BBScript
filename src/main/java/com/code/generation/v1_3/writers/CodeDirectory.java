package com.code.generation.v1_3.writers;

import com.code.generation.v1_3.elements.scope.Code;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.util.for_test.organization.BaseTestFolderOrganization;
import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodeDirectory {
    private File targetFolder;
    private Map<String, CodeWriter> codeWriters = new HashMap<>();
    private Map<ParserRuleContext, Result> resultMap;

    public CodeDirectory(File targetFolder, Map<ParserRuleContext, Result> resultMap) {
        this.targetFolder = targetFolder;
        BaseTestFolderOrganization.removeFolderForce(targetFolder);
        targetFolder.mkdirs();
        this.resultMap = resultMap;
    }

    public void addCustomType(CustomType customType) {
        if(customType.isStandard()){
            return;
        }
        String fileName = customType.getName();
        codeWriters.put(fileName, new CustomTypeWriter(targetFolder, customType, resultMap));
    }

    public void addCode(Code code) {
        String fileName = code.getName();
        codeWriters.put(fileName, new ProgContextWriter(code, targetFolder, resultMap));
    }

    public void write() throws IOException {
        for (CodeWriter codeWriter : codeWriters.values()) {
            codeWriter.writeAndClose();
        }
    }
}
