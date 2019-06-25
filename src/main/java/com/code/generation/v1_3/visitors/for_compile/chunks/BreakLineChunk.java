package com.code.generation.v1_3.visitors.for_compile.chunks;

import com.code.generation.v1_3.visitors.for_compile.statement_results.StatementResult;

public class BreakLineChunk implements IBreakLineChunk {
    private StatementResult statementResult;

    public BreakLineChunk(StatementResult statementResult) {
        this.statementResult = statementResult;
    }

    public StatementResult getStatementResult() {
        return statementResult;
    }
}
