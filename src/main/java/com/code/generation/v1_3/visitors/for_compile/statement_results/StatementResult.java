package com.code.generation.v1_3.visitors.for_compile.statement_results;

import com.code.generation.v1_3.elements.strong_type.StrongVariable;
import com.code.generation.v1_3.visitors.for_compile.CompiledLine;
import com.code.generation.v1_3.visitors.for_compile.CompiledResult;
import com.code.generation.v1_3.visitors.for_compile.chunks.ISingleChunk;
import com.code.generation.v1_3.visitors.for_compile.chunks.InlineChunk;
import com.code.generation.v1_3.visitors.for_compile.expression_results.InnerStatementResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public interface StatementResult extends CompiledResult {
    static StatementResult getInstance(InnerStatementResult innerStatementResult){
        if(innerStatementResult.getNonDefinedVariables().isEmpty()){
            return new SingleStatementResult(innerStatementResult.getMinimizedChunkList());
        }
        List<SingleStatementResult> singleStatementResults = new ArrayList<>(innerStatementResult.getNonDefinedVariables().size());
        for (StrongVariable strongVariable : innerStatementResult.getNonDefinedVariables()) {
            ISingleChunk chunk = new InlineChunk(strongVariable.toDefinitionStatementString());
            singleStatementResults.add(new SingleStatementResult(Collections.singletonList(chunk)));
        }
        singleStatementResults.add(new SingleStatementResult(innerStatementResult.getMinimizedChunkList()));
        return new MultiStatementResult(singleStatementResults);
    }

    static StatementResult getInstance(List<StatementResult> statementResults){
        List<SingleStatementResult> singleStatementResults = flattenStatementResults(statementResults);
        if(singleStatementResults.size() == 1){
            return singleStatementResults.get(0);
        }
        return new MultiStatementResult(singleStatementResults);
    }

    static List<SingleStatementResult> flattenStatementResults(List<StatementResult> statementResults) {
        List<SingleStatementResult> singleStatementResults = new LinkedList<>();
        for (StatementResult statementResult : statementResults) {
            if(statementResult instanceof SingleStatementResult){
                singleStatementResults.add((SingleStatementResult) statementResult);
                continue;
            }
            singleStatementResults.addAll(((MultiStatementResult) statementResult).getSingleStatementResults());
        }
        return singleStatementResults;
    }

    List<CompiledLine> getCompiledLines(int indentLevel);
}
