package com.code.generation.v1_3.visitors.for_compile;

public class CompiledLine {
    private int indentLevel;
    private String inLineCompiled;

    public CompiledLine(int indentLevel, String inLineCompiled) {
        this.indentLevel = indentLevel;
        this.inLineCompiled = inLineCompiled;
    }

    public int getIndentLevel() {
        return indentLevel;
    }

    public String getInLineCompiled() {
        return inLineCompiled;
    }
}
