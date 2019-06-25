package com.code.generation.v1_3.visitors.for_compile.statement_results;

import com.code.generation.v1_3.visitors.for_compile.CompiledLine;
import com.code.generation.v1_3.visitors.for_compile.chunks.IBreakLineChunk;
import com.code.generation.v1_3.visitors.for_compile.chunks.ISingleChunk;
import com.code.generation.v1_3.visitors.for_compile.chunks.InlineChunk;

import java.util.LinkedList;
import java.util.List;

public class SingleStatementResult implements StatementResult{
    private List<ISingleChunk> singleChunks;

    protected SingleStatementResult(List<ISingleChunk> singleChunks) {
        this.singleChunks = singleChunks;
    }

    @Override
    public List<CompiledLine> getCompiledLines(int indentLevel) {
        List<CompiledLine> compiledLines = new LinkedList<>();
        for (ISingleChunk singleChunk : singleChunks) {
            if(singleChunk instanceof InlineChunk){
                compiledLines.add(new CompiledLine(indentLevel, ((InlineChunk) singleChunk).getCompiled()));
                continue;
            }
            compiledLines.addAll(((IBreakLineChunk) singleChunk).getStatementResult().getCompiledLines(indentLevel + 1));
        }
        return compiledLines;
    }
}
