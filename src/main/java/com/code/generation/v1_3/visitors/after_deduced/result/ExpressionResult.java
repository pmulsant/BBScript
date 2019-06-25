package com.code.generation.v1_3.visitors.after_deduced.result;

import com.code.generation.v1_3.elements.strong_type.Assignable;
import com.code.generation.v1_3.elements.strong_type.StrongType;
import com.generated.GrammarParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Map;

public class ExpressionResult extends Result {
    private StrongType strongType;
    private Assignable assignable;

    public ExpressionResult(StrongType strongType, Map<ParserRuleContext, Result> resultMap, GrammarParser.ExprContext exprContext, Assignable assignable) {
        this(strongType, resultMap, exprContext);
        this.assignable = assignable;
    }

    public ExpressionResult(StrongType strongType, Map<ParserRuleContext, Result> resultMap, GrammarParser.ExprContext exprContext) {
        super(resultMap, exprContext);
        this.strongType = strongType;
    }

    public StrongType getStrongType() {
        return strongType;
    }

    public boolean isAssignable() {
        return assignable != null;
    }

    public Assignable getAssignable() {
        return assignable;
    }
}
