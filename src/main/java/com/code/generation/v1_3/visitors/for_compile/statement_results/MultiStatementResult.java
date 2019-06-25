package com.code.generation.v1_3.visitors.for_compile.statement_results;

import com.code.generation.v1_3.visitors.for_compile.CompiledLine;

import java.util.LinkedList;
import java.util.List;

public class MultiStatementResult implements StatementResult {
    private List<SingleStatementResult> singleStatementResults;

    protected MultiStatementResult(List<SingleStatementResult> singleStatementResults) {
        this.singleStatementResults = singleStatementResults;
    }

    public List<SingleStatementResult> getSingleStatementResults() {
        return singleStatementResults;
    }

    @Override
    public List<CompiledLine> getCompiledLines(int indentLevel) {
        List<CompiledLine> compiledLines = new LinkedList<>();
        for (SingleStatementResult singleStatementResult : singleStatementResults) {
            compiledLines.addAll(singleStatementResult.getCompiledLines(indentLevel));
        }
        return compiledLines;
    }
}
