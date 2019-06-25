package com.code.generation.v1_3.visitors.for_compile.expression_results;

import com.code.generation.v1_3.elements.strong_type.StrongVariable;
import com.code.generation.v1_3.visitors.for_compile.chunks.IMultiChunks;
import com.code.generation.v1_3.visitors.for_compile.chunks.ISingleChunk;

import java.util.List;

public class MultiChunkInnerStatementResult extends InnerStatementResult implements IMultiChunks {
    protected MultiChunkInnerStatementResult(List<StrongVariable> nonDefinedVariables, List<ISingleChunk> minimizedChunkList) {
        super(nonDefinedVariables, minimizedChunkList);
    }

    @Override
    public List<ISingleChunk> getSingleChunks() {
        return minimizedChunkList;
    }
}
