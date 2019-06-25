package com.code.generation.v1_3.visitors.after_deduced.result;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Map;

public abstract class Result {
    protected Result(Map<ParserRuleContext, Result> resultMap, ParserRuleContext parserRuleContext){
        resultMap.put(parserRuleContext, this);
    }
}
