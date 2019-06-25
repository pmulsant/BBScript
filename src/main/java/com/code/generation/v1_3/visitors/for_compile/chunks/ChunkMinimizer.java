package com.code.generation.v1_3.visitors.for_compile.chunks;

import java.util.LinkedList;
import java.util.List;

public class ChunkMinimizer {
    private List<IChunk> chunks;

    public ChunkMinimizer(List<IChunk> chunks){
        this.chunks = chunks;
    }

    public List<ISingleChunk> minimized(){
        List<ISingleChunk> flattenChunkList = computeFlattenList();
        return minimizedFlattenList(flattenChunkList);
    }

    private List<ISingleChunk> computeFlattenList() {
        List<ISingleChunk> result = new LinkedList<>();
        for (IChunk chunk : chunks) {
            if(chunk instanceof ISingleChunk){
                result.add((ISingleChunk) chunk);
                continue;
            }
            result.addAll(((IMultiChunks) chunk).getSingleChunks());
        }
        return result;
    }

    private List<ISingleChunk> minimizedFlattenList(List<ISingleChunk> flattenList){
        List<ISingleChunk> result = new LinkedList<>();
        StringBuilder compiled = new StringBuilder();
        for (ISingleChunk chunk : flattenList) {
            if(chunk instanceof IInlineChunk){
                compiled.append(((IInlineChunk) chunk).getCompiled());
                continue;
            }
            flushCompiledStringBuilder(result, compiled);
            result.add(chunk);
        }
        flushCompiledStringBuilder(result, compiled);
        return result;
    }

    private void flushCompiledStringBuilder(List<ISingleChunk> result, StringBuilder compiled){
        if(compiled.length() == 0){
            return;
        }
        result.add(new InlineChunk(compiled.toString()));
        compiled.setLength(0);
    }
}
