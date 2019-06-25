package com.code.generation.v1_3.visitors.for_compile.chunks;

public class InlineChunk implements IInlineChunk {
    private String compiled;

    public InlineChunk(String compiled) {
        this.compiled = compiled;
    }

    @Override
    public String getCompiled() {
        return compiled;
    }
}
