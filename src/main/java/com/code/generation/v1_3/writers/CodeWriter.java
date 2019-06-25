package com.code.generation.v1_3.writers;

import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import com.code.generation.v1_3.visitors.for_compile.CompiledLine;
import com.code.generation.v1_3.visitors.for_compile.statement_results.StatementResult;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.*;
import java.util.Map;

public abstract class CodeWriter {
    protected Map<ParserRuleContext, Result> resultMap;

    private File file;
    private OutputStream outputStream;
    private PrintWriter printWriter;

    public CodeWriter(File file, Map<ParserRuleContext, Result> resultMap) {
        this.file = file;
        this.resultMap = resultMap;
    }

    public void writeAndClose() throws IOException {
        write();
        finish();
    }

    protected abstract void write() throws IOException;

    protected void writeLine(String line, int indent) throws IOException {
        if(printWriter == null){
            outputStream = new FileOutputStream(file);
            printWriter = new PrintWriter(outputStream);
        }
        printWriter.println(computeIndentString(indent) + line);
    }

    protected void finish() throws IOException {
        if(printWriter == null){
            return;
        }
        printWriter.flush();
        outputStream.close();
    }

    protected String computeIndentString(int indent){
        StringBuilder indentString = new StringBuilder();
        for (int index = 0; index < indent; index++) {
            indentString.append('\t');
        }
        return indentString.toString();
    }

    protected void writeStatementResult(StatementResult statementResult, int indentLevel) throws IOException {
        for (CompiledLine compiledLine : statementResult.getCompiledLines(indentLevel)) {
            writeLine(compiledLine.getInLineCompiled(), compiledLine.getIndentLevel());
        }
    }
}
