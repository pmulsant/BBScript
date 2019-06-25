package com.code.generation.v1_3.writers;

import com.code.generation.v1_3.elements.scope.Code;
import com.code.generation.v1_3.elements.scope.GlobalScope;
import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import com.code.generation.v1_3.visitors.for_compile.CompilerVisitor;
import com.code.generation.v1_3.visitors.for_compile.statement_results.StatementResult;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ProgContextWriter extends CodeWriter {
    private Code code;

    public ProgContextWriter(Code code, File targetFolder, Map<ParserRuleContext, Result> resultMap) {
        super(new File(targetFolder.getPath() + "/" + code.getName() + GlobalScope.GENERATED_EXTENSION_NAME), resultMap);
        this.code = code;
    }

    @Override
    protected void write() throws IOException {
        StatementResult statementResult = (StatementResult) new CompilerVisitor(resultMap).visit(code.getProgContext());
        writeStatementResult(statementResult, 0);
    }
}
