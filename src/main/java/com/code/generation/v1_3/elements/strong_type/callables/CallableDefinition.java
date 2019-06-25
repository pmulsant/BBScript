package com.code.generation.v1_3.elements.strong_type.callables;

import com.code.generation.v1_3.visitors.after_deduced.checking.TypeCheckerVisitor;
import com.generated.GrammarParser;

public class CallableDefinition {
    private TypeCheckerVisitor typeCheckerVisitor;
    private GrammarParser.ExprContext exprContext;
    private GrammarParser.RunnableScopeContext runnableScopeContext;

    public CallableDefinition(TypeCheckerVisitor typeCheckerVisitor, GrammarParser.ExprContext exprContext) {
        this.typeCheckerVisitor = typeCheckerVisitor;
        this.exprContext = exprContext;
    }

    public CallableDefinition(TypeCheckerVisitor typeCheckerVisitor, GrammarParser.RunnableScopeContext runnableScopeContext) {
        this.typeCheckerVisitor = typeCheckerVisitor;
        this.runnableScopeContext = runnableScopeContext;
    }

    public GrammarParser.ExprContext getExprContext() {
        return exprContext;
    }

    public GrammarParser.RunnableScopeContext getRunnableScopeContext() {
        return runnableScopeContext;
    }

    public TypeCheckerVisitor getTypeCheckerVisitor() {
        return typeCheckerVisitor;
    }
}
