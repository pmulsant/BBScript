package com.code.generation.v1_3.visitors.for_compile.chunks;

import java.util.List;

public interface IMultiChunks extends IChunk {
    List<ISingleChunk> getSingleChunks();
}
