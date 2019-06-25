package com.code.generation.v1_3.elements.scope;

import com.generated.GrammarParser;

public class Code {
    private String name;
    private GrammarParser.ProgContext progContext;

    public Code(String name, GrammarParser.ProgContext progContext) {
        this.name = name;
        this.progContext = progContext;
    }

    public String getName() {
        return name;
    }

    public GrammarParser.ProgContext getProgContext() {
        return progContext;
    }
}
