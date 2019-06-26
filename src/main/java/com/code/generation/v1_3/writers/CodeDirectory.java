package com.code.generation.v1_3.writers;

import com.code.generation.v1_3.elements.scope.Code;
import com.code.generation.v1_3.elements.strong_type.callables.Function;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.util.for_test.organization.BaseTestFolderOrganization;
import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CodeDirectory {
    private File targetFolder;
    private List<CodeWriter> codeWriters = new LinkedList<>();
    private Map<ParserRuleContext, Result> resultMap;
    private List<Function> functions;

    public CodeDirectory(File targetFolder, Map<ParserRuleContext, Result> resultMap, List<Function> functions) {
        this.targetFolder = targetFolder;
        BaseTestFolderOrganization.removeFolderForce(targetFolder);
        targetFolder.mkdirs();
        this.resultMap = resultMap;
        this.functions = functions;
    }

    public void addCustomType(CustomType customType) {
        if(customType.isStandard()){
            return;
        }
        codeWriters.add(new CustomTypeWriter(targetFolder, customType, resultMap));
    }

    public void addCode(Code code) {
        codeWriters.add(new ProgContextWriter(code, targetFolder, resultMap));
    }

    public void write() throws IOException {
        if(!functions.isEmpty()){
            codeWriters.add(new FunctionsWriter(targetFolder, resultMap, functions));
        }
        for (CodeWriter codeWriter : codeWriters) {
            codeWriter.writeAndClose();
        }
    }
}
