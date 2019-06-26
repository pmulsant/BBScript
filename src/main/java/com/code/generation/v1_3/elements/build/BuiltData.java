package com.code.generation.v1_3.elements.build;

import com.code.generation.v1_3.elements.scope.Code;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.callables.Function;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import com.code.generation.v1_3.writers.CodeDirectory;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BuiltData {
    private Map<ParserRuleContext, Result> resultMap;
    private StrongTypeDirectory strongTypeDirectory;
    private List<Code> codes;

    public BuiltData(Map<ParserRuleContext, Result> resultMap, StrongTypeDirectory strongTypeDirectory, List<Code> codes) {
        this.resultMap = resultMap;
        this.strongTypeDirectory = strongTypeDirectory;
        this.codes = codes;
    }

    public void compile(File targetDirectory) throws IOException {
        CodeDirectory codeDirectory = new CodeDirectory(targetDirectory, resultMap, strongTypeDirectory.getNonStandardFunctions());
        for (CustomType customType : strongTypeDirectory.getCustomTypes().values()) {
            codeDirectory.addCustomType(customType);
        }
        for (Code code : codes) {
            codeDirectory.addCode(code);
        }
        codeDirectory.write();
    }
}
