package com.code.generation.v1_3.visitors.after_deduced;

import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import com.generated.GrammarBaseVisitor;
import com.generated.GrammarParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Map;

public class GetResultVisitor extends GrammarBaseVisitor<Result> {
    private Map<ParserRuleContext, Result> resultMap;

    public GetResultVisitor(Map<ParserRuleContext, Result> resultMap) {
        this.resultMap = resultMap;
    }

    public Result innerVisit(ParserRuleContext ctx){
        super.visit(ctx);
        return resultMap.get(ctx);
    }

    @Override
    public Result visitProg(GrammarParser.ProgContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitSimpleStat(GrammarParser.SimpleStatContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitFunctionCallAndDef(GrammarParser.FunctionCallAndDefContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitMulExpr(GrammarParser.MulExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitAndExpr(GrammarParser.AndExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitStringExpr(GrammarParser.StringExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitAttributeCall(GrammarParser.AttributeCallContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitFloatExpr(GrammarParser.FloatExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitNegation(GrammarParser.NegationContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitCompExpr(GrammarParser.CompExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitMethodCall(GrammarParser.MethodCallContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitNullExpr(GrammarParser.NullExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitIdentifier(GrammarParser.IdentifierContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitEqualsExpr(GrammarParser.EqualsExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitInitExpr(GrammarParser.InitExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitFunctionCall(GrammarParser.FunctionCallContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitInstantiation(GrammarParser.InstantiationContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitEnrichedExpr(GrammarParser.EnrichedExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitBooleanExpr(GrammarParser.BooleanExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitLambdaExpr(GrammarParser.LambdaExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitRegularExpr(GrammarParser.RegularExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitAddExpr(GrammarParser.AddExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitOrExpr(GrammarParser.OrExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitCharExpr(GrammarParser.CharExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitInstantiationCallAndDef(GrammarParser.InstantiationCallAndDefContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitInnerExpr(GrammarParser.InnerExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitMethodCallAndDef(GrammarParser.MethodCallAndDefContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitIntExpr(GrammarParser.IntExprContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitArgs(GrammarParser.ArgsContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitArg(GrammarParser.ArgContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitType(GrammarParser.TypeContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitLambdaArg(GrammarParser.LambdaArgContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitLambdaProcess(GrammarParser.LambdaProcessContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitAdditionalData(GrammarParser.AdditionalDataContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitAData(GrammarParser.ADataContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitIfStat(GrammarParser.IfStatContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitIfChunk(GrammarParser.IfChunkContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitElseIfChunk(GrammarParser.ElseIfChunkContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitElseChunk(GrammarParser.ElseChunkContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitWhileStat(GrammarParser.WhileStatContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitRunnableScope(GrammarParser.RunnableScopeContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitThrowStat(GrammarParser.ThrowStatContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitReturnStat(GrammarParser.ReturnStatContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitBreakStat(GrammarParser.BreakStatContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitContinueStat(GrammarParser.ContinueStatContext ctx) {
        return innerVisit(ctx);
    }

    @Override
    public Result visitComplexId(GrammarParser.ComplexIdContext ctx) {
        return innerVisit(ctx);
    }
}
