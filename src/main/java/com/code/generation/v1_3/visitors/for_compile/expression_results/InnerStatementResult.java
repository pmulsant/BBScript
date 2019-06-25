package com.code.generation.v1_3.visitors.for_compile.expression_results;

import com.code.generation.v1_3.elements.strong_type.StrongVariable;
import com.code.generation.v1_3.visitors.for_compile.CompiledResult;
import com.code.generation.v1_3.visitors.for_compile.chunks.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class InnerStatementResult implements CompiledResult, IChunk {
    public static InnerStatementResult getInstance(StrongVariable strongVariable, boolean isDefinedHere) {
        List<StrongVariable> nonDefinedVariables = isDefinedHere ? Collections.singletonList(strongVariable) : Collections.EMPTY_LIST;
        return new SingleChunkInnerStatementResult(nonDefinedVariables, new InlineChunk(strongVariable.getNameWithDollar()));
    }

    public static InnerStatementResult getInstance(List<IChunk> chunks){
        if(chunks.isEmpty()){
            throw new IllegalStateException();
        }
        List<StrongVariable> nonDefinedVariables = getNonDefinedVariables(chunks);
        List<ISingleChunk> minimizedChunks = new ChunkMinimizer(chunks).minimized();
        if(minimizedChunks.size() > 1){
            return new MultiChunkInnerStatementResult(nonDefinedVariables, minimizedChunks);
        }
        IInlineChunk inlineChunk = (IInlineChunk) minimizedChunks.get(0);
        return new SingleChunkInnerStatementResult(nonDefinedVariables, inlineChunk);
    }

    private static List<StrongVariable> getNonDefinedVariables(List<IChunk> chunks) {
        List<StrongVariable> result = new LinkedList<>();
        for (IChunk chunk : chunks) {
            if(chunk instanceof InnerStatementResult){
                result.addAll(((InnerStatementResult) chunk).getNonDefinedVariables());
            }
        }
        return result;
    }

    private List<StrongVariable> nonDefinedVariables;
    protected List<ISingleChunk> minimizedChunkList;

    protected InnerStatementResult(List<StrongVariable> nonDefinedVariables, List<ISingleChunk> minimizedChunkList) {
        this.nonDefinedVariables = nonDefinedVariables;
        this.minimizedChunkList = minimizedChunkList;
    }

    public List<StrongVariable> getNonDefinedVariables() {
        return nonDefinedVariables;
    }

    public List<ISingleChunk> getMinimizedChunkList() {
        return minimizedChunkList;
    }
}
