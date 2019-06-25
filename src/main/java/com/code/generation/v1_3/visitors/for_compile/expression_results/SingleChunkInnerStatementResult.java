package com.code.generation.v1_3.visitors.for_compile.expression_results;

import com.code.generation.v1_3.elements.strong_type.StrongVariable;
import com.code.generation.v1_3.visitors.for_compile.chunks.IInlineChunk;

import java.util.Collections;
import java.util.List;

public class SingleChunkInnerStatementResult extends InnerStatementResult implements IInlineChunk {
    private IInlineChunk inlineChunk;

    protected SingleChunkInnerStatementResult(List<StrongVariable> nonDefinedVariables, IInlineChunk inlineChunk) {
        super(nonDefinedVariables, Collections.singletonList(inlineChunk));
        this.inlineChunk = inlineChunk;
    }

    @Override
    public String getCompiled() {
        return inlineChunk.getCompiled();
    }
}
