package com.code.generation.v1_3.writers;

import com.code.generation.v1_3.elements.scope.GlobalScope;
import com.code.generation.v1_3.elements.strong_type.callables.CallableDefinition;
import com.code.generation.v1_3.elements.strong_type.callables.Function;
import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import com.code.generation.v1_3.visitors.for_compile.CompilerVisitor;
import com.code.generation.v1_3.visitors.for_compile.statement_results.StatementResult;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FunctionsWriter extends CodeWriter {
    private static final int INNER_CALLABLE_INDENT_LEVEL = 1;

    private List<Function> functions;

    public FunctionsWriter(File targetDirectory, Map<ParserRuleContext, Result> resultMap, List<Function> functions) {
        super(new File(targetDirectory.getAbsolutePath() + "/" + GlobalScope.UTIL + GlobalScope.GENERATED_EXTENSION_NAME), resultMap);
        this.functions = functions;
    }

    @Override
    protected void write() throws IOException {
        for (Function function : functions) {
            writeFunction(function);
        }
    }

    private void writeFunction(Function function) throws IOException {
        String paramStr = String.join(", ", function.getParameters().stream().map(parameter -> parameter.getType().toString() + " " + parameter.getName()).collect(Collectors.toList()));
        writeLine("function " + function.getReturnedType().getComplexName() + " " + function.getName() + " (" + paramStr + ") {", 0);
        CallableDefinition callableDefinition = function.getCallableDefinition();
        StatementResult statementResult = (StatementResult) new CompilerVisitor(resultMap).visit(callableDefinition.getRunnableScopeContext());
        writeStatementResult(statementResult, INNER_CALLABLE_INDENT_LEVEL);
        writeLine("}", 0);
        writeLine("", 0);
    }
}
