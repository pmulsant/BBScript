package com.code.generation.v1_3.elements.symbols;

import com.code.generation.v1_3.elements.scope.Scope;
import org.antlr.v4.runtime.ParserRuleContext;

public class Position {
    private ParserRuleContext context;
    private Scope scope;
    private int statIndex;

    public Position(ParserRuleContext context, Scope scope, int statIndex) {
        this.context = context;
        this.scope = scope;
        this.statIndex = statIndex;
    }

    public ParserRuleContext getContext() {
        return context;
    }

    public Scope getScope() {
        return scope;
    }

    public int getStatIndex() {
        return statIndex;
    }
}
