package com.code.generation.v1_3.visitors.for_compile;

import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.StrongVariable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.visitors.after_deduced.result.ExpressionResult;
import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import com.code.generation.v1_3.visitors.for_compile.chunks.BreakLineChunk;
import com.code.generation.v1_3.visitors.for_compile.chunks.IChunk;
import com.code.generation.v1_3.visitors.for_compile.chunks.InlineChunk;
import com.code.generation.v1_3.visitors.for_compile.expression_results.InnerStatementResult;
import com.code.generation.v1_3.visitors.for_compile.statement_results.StatementResult;
import com.generated.GrammarBaseVisitor;
import com.generated.GrammarParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;
import java.util.stream.Collectors;

public class CompilerVisitor extends GrammarBaseVisitor<CompiledResult> {
    private Map<ParserRuleContext, Result> resultMap;

    public CompilerVisitor(Map<ParserRuleContext, Result> resultMap) {
        this.resultMap = resultMap;
    }

    @Override
    public StatementResult visitProg(GrammarParser.ProgContext ctx) {
        List<StatementResult> statementResults = new ArrayList<>(ctx.stat().size());
        for (GrammarParser.StatContext statContext : ctx.stat()) {
            statementResults.add((StatementResult) visit(statContext));
        }
        return StatementResult.getInstance(statementResults);
    }

    /******* isStat ************/

    @Override
    public StatementResult visitIsSimpleStat(GrammarParser.IsSimpleStatContext ctx) {
        return (StatementResult) visit(ctx.simpleStat());
    }

    @Override
    public StatementResult visitIsIfStat(GrammarParser.IsIfStatContext ctx) {
        return (StatementResult) visit(ctx.ifStat());
    }

    @Override
    public StatementResult visitIsThrowStat(GrammarParser.IsThrowStatContext ctx) {
        return (StatementResult) visit(ctx.throwStat());
    }

    @Override
    public StatementResult visitIsReturnStat(GrammarParser.IsReturnStatContext ctx) {
        return (StatementResult) visit(ctx.returnStat());
    }

    @Override
    public StatementResult visitIsBreakStat(GrammarParser.IsBreakStatContext ctx) {
        return manageBreakOrContinueStat(true);
    }

    @Override
    public CompiledResult visitIsContinueStat(GrammarParser.IsContinueStatContext ctx) {
        return manageBreakOrContinueStat(false);
    }


    @Override
    public StatementResult visitIfStat(GrammarParser.IfStatContext ctx) {
        List<IChunk> chunks = new LinkedList<>();
        chunks.add((InnerStatementResult) visit(ctx.ifChunk()));
        for (GrammarParser.ElseIfChunkContext elseIfChunkContext : ctx.elseIfChunk()) {
            chunks.add((InnerStatementResult) visit(elseIfChunkContext));
        }
        if(ctx.elseChunk() != null) {
            chunks.add((InnerStatementResult) visit(ctx.elseChunk()));
        }
        return StatementResult.getInstance(InnerStatementResult.getInstance(chunks));
    }

    @Override
    public InnerStatementResult visitIfChunk(GrammarParser.IfChunkContext ctx) {
        return manageConditionChunk("if", ctx.expr(), ctx.runnableScope());
    }

    @Override
    public InnerStatementResult visitElseIfChunk(GrammarParser.ElseIfChunkContext ctx) {
        return manageConditionChunk(" else if", ctx.expr(), ctx.runnableScope());
    }

    @Override
    public CompiledResult visitElseChunk(GrammarParser.ElseChunkContext ctx) {
        return manageConditionChunk(" else", null, ctx.runnableScope());
    }

    @Override
    public StatementResult visitWhileStat(GrammarParser.WhileStatContext ctx) {
        InnerStatementResult innerStatementResult = manageConditionChunk("while", ctx.expr(), ctx.runnableScope());
        return StatementResult.getInstance(innerStatementResult);
    }

    @Override
    public StatementResult visitRunnableScope(GrammarParser.RunnableScopeContext ctx) {
        List<StatementResult> statementResults = new LinkedList<>();
        for (GrammarParser.StatContext statContext : ctx.stat()) {
            statementResults.add((StatementResult) visit(statContext));
        }
        return StatementResult.getInstance(statementResults);
    }

    /*********** visit simple stat ***********/

    @Override
    public StatementResult visitSimpleStat(GrammarParser.SimpleStatContext ctx) {
        InnerStatementResult result = (InnerStatementResult) visit(ctx.expr());
        InnerStatementResult resultWithEndStantementSymbol = InnerStatementResult.getInstance(Arrays.asList(result, new InlineChunk(";")));
        return StatementResult.getInstance(resultWithEndStantementSymbol);
    }

    @Override
    public InnerStatementResult visitIntExpr(GrammarParser.IntExprContext ctx) {
        return makeBasicInnerStatementResult((ctx.SUB() != null ? "-" : "") + ctx.INT().getText());
    }

    @Override
    public InnerStatementResult visitFloatExpr(GrammarParser.FloatExprContext ctx) {
        return makeBasicInnerStatementResult((ctx.SUB() != null ? "-" : "") + ctx.FLOAT().getText());
    }

    @Override
    public InnerStatementResult visitCharExpr(GrammarParser.CharExprContext ctx) {
        return makeBasicInnerStatementResult(ctx.CHAR().getText());
    }

    @Override
    public InnerStatementResult visitBooleanExpr(GrammarParser.BooleanExprContext ctx) {
        return makeBasicInnerStatementResult(ctx.BOOLEAN().getText());
    }

    @Override
    public InnerStatementResult visitStringExpr(GrammarParser.StringExprContext ctx) {
        return makeBasicInnerStatementResult(ctx.STRING().getText());
    }

    @Override
    public InnerStatementResult visitRegularExpr(GrammarParser.RegularExprContext ctx) {
        return makeBasicInnerStatementResult(ctx.REGEX().getText());
    }

    @Override
    public CompiledResult visitNullExpr(GrammarParser.NullExprContext ctx) {
        return makeBasicInnerStatementResult(StandardKnowledges.NULL_KEY_WORD);
    }

    @Override
    public InnerStatementResult visitIdentifier(GrammarParser.IdentifierContext ctx) {
        ExpressionResult result = (ExpressionResult) resultMap.get(ctx);
        StrongVariable strongVariable = (StrongVariable) result.getAssignable();
        if(strongVariable.getVariable().isDefinedHere(ctx)){
            return InnerStatementResult.getInstance(strongVariable, true);
        }
        return InnerStatementResult.getInstance(strongVariable, false);
    }

    @Override
    public InnerStatementResult visitInstantiation(GrammarParser.InstantiationContext ctx) {
        return manageInstantiation(ctx, ctx.args());
    }

    @Override
    public InnerStatementResult visitInstantiationCallAndDef(GrammarParser.InstantiationCallAndDefContext ctx) {
        return manageInstantiation(ctx, ctx.args());
    }

    @Override
    public InnerStatementResult visitAttributeCall(GrammarParser.AttributeCallContext ctx) {
        InnerStatementResult innerStatementResult = (InnerStatementResult) visit(ctx.expr());
        String attributeName = ctx.complexId().ID().getText();
        return InnerStatementResult.getInstance(Arrays.asList(innerStatementResult, new InlineChunk("." + attributeName)));
    }

    @Override
    public InnerStatementResult visitMethodCall(GrammarParser.MethodCallContext ctx) {
        return manageMethodCall(ctx.expr(), ctx.complexId().ID().getText(), ctx.args());
    }

    @Override
    public InnerStatementResult visitMethodCallAndDef(GrammarParser.MethodCallAndDefContext ctx) {
        return manageMethodCall(ctx.expr(), ctx.complexId().ID().getText(), ctx.args());
    }

    @Override
    public InnerStatementResult visitFunctionCall(GrammarParser.FunctionCallContext ctx) {
        return manageFunctionCall(ctx.complexId().ID().getText(), ctx.args());
    }

    @Override
    public InnerStatementResult visitFunctionCallAndDef(GrammarParser.FunctionCallAndDefContext ctx) {
        return manageFunctionCall(ctx.complexId().ID().getText(), ctx.args());
    }

    @Override
    public InnerStatementResult visitNegation(GrammarParser.NegationContext ctx) {
        InnerStatementResult innerChunk = (InnerStatementResult) visit(ctx.expr());
        return InnerStatementResult.getInstance(Arrays.asList(new InlineChunk("!"), innerChunk));
    }

    @Override
    public InnerStatementResult visitAddExpr(GrammarParser.AddExprContext ctx) {
        return manageBinaryOp(ctx.op1, ctx.op2, ctx.operator.getText());
    }

    @Override
    public InnerStatementResult visitMulExpr(GrammarParser.MulExprContext ctx) {
        return manageBinaryOp(ctx.op1, ctx.op2, ctx.operator.getText());
    }

    @Override
    public InnerStatementResult visitCompExpr(GrammarParser.CompExprContext ctx) {
        return manageBinaryOp(ctx.op1, ctx.op2, ctx.operator.getText());
    }

    @Override
    public InnerStatementResult visitEqualsExpr(GrammarParser.EqualsExprContext ctx) {
        return manageBinaryOp(ctx.op1, ctx.op2, ctx.operator.getText());
    }

    @Override
    public InnerStatementResult visitAndExpr(GrammarParser.AndExprContext ctx) {
        return manageBinaryOp(ctx.op1, ctx.op2, "&&");
    }

    @Override
    public InnerStatementResult visitOrExpr(GrammarParser.OrExprContext ctx) {
        return manageBinaryOp(ctx.op1, ctx.op2, "||");
    }

    @Override
    public InnerStatementResult visitInnerExpr(GrammarParser.InnerExprContext ctx) {
        InnerStatementResult innerChunk = (InnerStatementResult) visit(ctx.expr());
        return InnerStatementResult.getInstance(Arrays.asList(new InlineChunk("("), innerChunk, new InlineChunk(")")));
    }

    @Override
    public InnerStatementResult visitInitExpr(GrammarParser.InitExprContext ctx) {
        return manageBinaryOp(ctx.op1, ctx.op2, "=");
    }

    @Override
    public InnerStatementResult visitLambdaExpr(GrammarParser.LambdaExprContext ctx) {
        List<IChunk> chunks = new LinkedList<>();

        String argsStr = getCompiledForLambdaArg(ctx.lambdaArg());
        String inlineBeginningArgsStr = argsStr + " " + StandardKnowledges.LAMBDA_KEY_WORD + " "; // exemple "(...) -> "
        IChunk firstChunk = new InlineChunk(inlineBeginningArgsStr);

        chunks.add(firstChunk);

        CompiledResult compiledResult = visit(ctx.lambdaProcess());
        if(compiledResult instanceof StatementResult){
            chunks.add(new InlineChunk("{"));
            chunks.add(new BreakLineChunk((StatementResult) compiledResult));
            chunks.add(new InlineChunk("{"));
        } else {
            InnerStatementResult innerStatementResult = (InnerStatementResult) compiledResult;
            if(innerStatementResult.getNonDefinedVariables().isEmpty()){
                chunks.add(innerStatementResult);
            }
            else {
                chunks.add(new InlineChunk("{"));
                chunks.add(new BreakLineChunk(StatementResult.getInstance(innerStatementResult)));
                chunks.add(new InlineChunk("{"));
            }
        }
        return InnerStatementResult.getInstance(chunks);
    }

    @Override
    public CompiledResult visitLambdaProcess(GrammarParser.LambdaProcessContext ctx) {
        if(ctx.expr() != null){
            return visit(ctx.expr());
        }
        return visit(ctx.runnableScope());
    }

    @Override
    public InnerStatementResult visitEnrichedExpr(GrammarParser.EnrichedExprContext ctx) {
        return (InnerStatementResult) visit(ctx.expr());
    }

    /*********** visit other stats ***********/

    @Override
    public StatementResult visitThrowStat(GrammarParser.ThrowStatContext ctx) {
        return manageReturnOrThrowStat(false, ctx.expr());
    }

    @Override
    public StatementResult visitReturnStat(GrammarParser.ReturnStatContext ctx) {
        return manageReturnOrThrowStat(true, ctx.expr());
    }

    /*******************************/

    public InnerStatementResult makeBasicInnerStatementResult(String txt){
        return InnerStatementResult.getInstance(Collections.singletonList(new InlineChunk(txt)));
    }

    private InnerStatementResult manageInstantiation(GrammarParser.ExprContext topContext, GrammarParser.ArgsContext argsContext) {
        ExpressionResult expressionResult = (ExpressionResult) resultMap.get(topContext);
        String complexName = ((NormalType) expressionResult.getStrongType()).getComplexName();
        return InnerStatementResult.getInstance(Arrays.asList(new InlineChunk("new "), getChunkForCallableParty(complexName, argsContext)));
    }

    private InnerStatementResult manageMethodCall(GrammarParser.ExprContext expr, String methodName, GrammarParser.ArgsContext argsContext) {
        InnerStatementResult innerChunk = (InnerStatementResult) visit(expr);
        return InnerStatementResult.getInstance(Arrays.asList(innerChunk, new InlineChunk("."), getChunkForCallableParty(methodName, argsContext)));
    }

    private InnerStatementResult manageFunctionCall(String functionName, GrammarParser.ArgsContext argsContext) {
        return InnerStatementResult.getInstance(Collections.singletonList(getChunkForCallableParty(functionName, argsContext)));
    }

    private InnerStatementResult getChunkForCallableParty(String callableName, GrammarParser.ArgsContext argsContext){
        List<IChunk> chunks = new ArrayList<>(argsContext.arg().size() * 2 + 1); // for name + args + close parenthesis (1 + (argsNumber * 2 - 1) + 1
        chunks.add(new InlineChunk(callableName + "("));
        InlineChunk separator = null;
        for (GrammarParser.ArgContext argContext : argsContext.arg()) {
            if(separator != null){
                chunks.add(separator);
            } else {
                separator = new InlineChunk(", ");
            }
            chunks.add((InnerStatementResult) visit(argContext.expr()));
        }
        chunks.add(new InlineChunk(")"));
        return InnerStatementResult.getInstance(chunks);
    }

    private InnerStatementResult manageBinaryOp(GrammarParser.ExprContext op1, GrammarParser.ExprContext op2, String op) {
        InnerStatementResult chunk1 = (InnerStatementResult) visit(op1);
        InnerStatementResult chunk2 = (InnerStatementResult) visit(op2);
        op = " " + op + " ";
        return InnerStatementResult.getInstance(Arrays.asList(chunk1, new InlineChunk(op), chunk2));
    }

    private InnerStatementResult manageConditionChunk(String conditionKeyWord, GrammarParser.ExprContext exprContext, GrammarParser.RunnableScopeContext runnableScopeContext){
        InnerStatementResult innerExpressionResult = null;
        if(exprContext != null){
            innerExpressionResult = (InnerStatementResult) visit(exprContext);
        }
        StatementResult runnableScopeResult = (StatementResult) visit(runnableScopeContext);
        InlineChunk headerBeginningChunk = new InlineChunk(exprContext == null ? conditionKeyWord + " {" : conditionKeyWord + "(");
        InlineChunk headerEndChunk = new InlineChunk(exprContext == null ? "" : ") {" );
        List<IChunk> chunks = new LinkedList<>();
        chunks.add(headerBeginningChunk);
        if(innerExpressionResult != null){
            chunks.add(innerExpressionResult);
        }
        chunks.add(headerEndChunk);
        chunks.add(new BreakLineChunk(runnableScopeResult));
        chunks.add(new InlineChunk("}"));
        return InnerStatementResult.getInstance(chunks);
    }

    private StatementResult manageReturnOrThrowStat(boolean isReturnStat, GrammarParser.ExprContext expr) {
        List<IChunk> chunks = new ArrayList<>();
        chunks.add(new InlineChunk(isReturnStat? "return" : "throw "));
        if(expr != null){
            chunks.add(new InlineChunk(" "));
            chunks.add((InnerStatementResult) visit(expr));
        }
        chunks.add(new InlineChunk(";"));
        return StatementResult.getInstance(InnerStatementResult.getInstance(chunks));
    }

    private StatementResult manageBreakOrContinueStat(boolean isBreakStat) {
        InlineChunk inlineChunk = new InlineChunk((isBreakStat ? "break" : "continue") + ';');
        return StatementResult.getInstance(InnerStatementResult.getInstance(Collections.singletonList(inlineChunk)));
    }

    private String getCompiledForLambdaArg(GrammarParser.LambdaArgContext lambdaArgContext){
        if(lambdaArgContext.complexId().size() == 1){
            return lambdaArgContext.complexId(0).ID().getText();
        }
        List<String> ids = lambdaArgContext.complexId().stream().map(complexIdContext -> complexIdContext.ID().getText()).collect(Collectors.toList());
        return "(" + ids + ")";
    }

}
