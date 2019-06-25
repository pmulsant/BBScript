package com.code.generation.v1_3.writers;

import com.code.generation.v1_3.elements.scope.GlobalScope;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.callables.Callable;
import com.code.generation.v1_3.elements.strong_type.callables.CallableDefinition;
import com.code.generation.v1_3.elements.strong_type.callables.Constructor;
import com.code.generation.v1_3.elements.strong_type.callables.Method;
import com.code.generation.v1_3.elements.strong_type.custom.Attribute;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import com.code.generation.v1_3.visitors.for_compile.CompilerVisitor;
import com.code.generation.v1_3.visitors.for_compile.statement_results.StatementResult;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CustomTypeWriter extends CodeWriter {
    private static final int INNER_CLASS_INDENT_LEVEL = 1;
    private static final int INNER_CALLABLE_INDENT_LEVEL = 2;
    private CustomType customType;

    public CustomTypeWriter(File targetDirectory, CustomType customType,  Map<ParserRuleContext, Result> resultMap) {
        super(new File(targetDirectory.getPath() + "/" + customType.getName() + GlobalScope.GENERATED_EXTENSION_NAME), resultMap);
        this.customType = customType;
    }

    @Override
    protected void write() throws IOException {
        writeLine("class " + customType.getName() + " {", 0);
        for (Attribute attribute : customType.getAttributes().values()) {
            writeLine(attribute.toDefinitionStatementString(), INNER_CLASS_INDENT_LEVEL);
        }
        for (Constructor constructor : customType.getConstructors().values()) {
            writeConstructor(constructor);
        }
        for (Method method : customType.getMethods().values()) {
            writeMethod(method);
        }
        writeLine("}", 0);
    }

    private void writeConstructor(Constructor constructor) throws IOException {
        String constructorHeader = customType.getName() + "(" + getParamsString(constructor) + ") {";
        writeCallable(constructor, constructorHeader);
    }

    private void writeMethod(Method method) throws IOException {
        String methodHeader = customType.getName() + "(" + getParamsString(method) + ") {";
        writeCallable(method, methodHeader);
    }

    private void writeCallable(Callable callable, String callableHeader) throws IOException {
        writeLine(callableHeader, INNER_CLASS_INDENT_LEVEL);
        CallableDefinition callableDefinition = callable.getCallableDefinition();
        StatementResult statementResult = (StatementResult) new CompilerVisitor(resultMap).visit(callableDefinition.getRunnableScopeContext());
        writeStatementResult(statementResult, INNER_CALLABLE_INDENT_LEVEL);
        writeLine("}", INNER_CLASS_INDENT_LEVEL);
    }

    public String getParamsString(Callable callable){
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";
        for (Parameter parameter : callable.getParameters()) {
            stringBuilder.append(prefix + ((NormalType) parameter.getType()).getComplexName() + " " + parameter.getName());
            prefix = ", ";
        }
        return stringBuilder.toString();
    }
}
