package com.code.generation.v1_3.visitors.for_variables;

import com.code.generation.v1_3.elements.scope.*;
import com.code.generation.v1_3.elements.symbols.Position;
import com.code.generation.v1_3.elements.symbols.Variable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.exception.TypeErrorException;
import com.code.generation.v1_3.exception.for_callables.WrongArgumentConventionException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.util.AccessibleTopContext;
import com.generated.GrammarBaseVisitor;
import com.generated.GrammarParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VariableAndParametersVisitor extends GrammarBaseVisitor<Variable> {
    private TypeInferenceMotor typeInferenceMotor;

    private GlobalScope globalScope;
    private ProgScope progScope;
    private AccessibleTopContext<ScopeData> scopeDataContext = new AccessibleTopContext<>();

    public VariableAndParametersVisitor(TypeInferenceMotor typeInferenceMotor, GlobalScope globalScope) {
        this.typeInferenceMotor = typeInferenceMotor;
        this.globalScope = globalScope;
        this.progScope = new ProgScope(globalScope);
        scopeDataContext.enterContext(new ScopeData(new LocalScope(progScope, null)));
    }

    @Override
    public Variable visitIsSimpleStat(GrammarParser.IsSimpleStatContext ctx) {
        return manageVisitIsStat(ctx);
    }

    @Override
    public Variable visitIsIfStat(GrammarParser.IsIfStatContext ctx) {
        return manageVisitIsStat(ctx);
    }

    @Override
    public Variable visitIsWhileStat(GrammarParser.IsWhileStatContext ctx) {
        return manageVisitIsStat(ctx);
    }

    @Override
    public Variable visitIsThrowStat(GrammarParser.IsThrowStatContext ctx) {
        return manageVisitIsStat(ctx);
    }

    @Override
    public Variable visitIsReturnStat(GrammarParser.IsReturnStatContext ctx) {
        return manageVisitIsStat(ctx);
    }

    @Override
    public Variable visitIsBreakStat(GrammarParser.IsBreakStatContext ctx) {
        return manageVisitIsStat(ctx);
    }

    @Override
    public Variable visitIsContinueStat(GrammarParser.IsContinueStatContext ctx) {
        return manageVisitIsStat(ctx);
    }


    /***********************************/

    @Override
    public Variable visitInstantiation(GrammarParser.InstantiationContext ctx) {
        checkArgNames(ctx.args(), true);
        return super.visitInstantiation(ctx);
    }

    @Override
    public Variable visitInstantiationCallAndDef(GrammarParser.InstantiationCallAndDefContext ctx) {
        NormalCallableScope normalCallableScope = createNormalCallableScope(ctx, ctx.args(), true);
        scopeDataContext.enterContext(new ScopeData(normalCallableScope));
        Variable result = super.visitInstantiationCallAndDef(ctx);
        scopeDataContext.exitContext();
        return result;
    }

    @Override
    public Variable visitMethodCall(GrammarParser.MethodCallContext ctx) {
        checkArgNames(ctx.args(), true);
        return super.visitMethodCall(ctx);
    }

    @Override
    public Variable visitMethodCallAndDef(GrammarParser.MethodCallAndDefContext ctx) {
        scopeDataContext.enterContext(new ScopeData(createNormalCallableScope(ctx, ctx.args(), true)));
        Variable result = super.visitMethodCallAndDef(ctx);
        scopeDataContext.exitContext();
        return result;
    }

    @Override
    public Variable visitFunctionCall(GrammarParser.FunctionCallContext ctx) {
        checkArgNames(ctx.args(), true);
        return super.visitFunctionCall(ctx);
    }

    @Override
    public Variable visitFunctionCallAndDef(GrammarParser.FunctionCallAndDefContext ctx) {
        scopeDataContext.enterContext(new ScopeData(createNormalCallableScope(ctx, ctx.args(), false)));
        Variable result = super.visitFunctionCallAndDef(ctx);
        scopeDataContext.exitContext();
        return result;
    }

    @Override
    public Variable visitLambdaExpr(GrammarParser.LambdaExprContext ctx) {
        scopeDataContext.enterContext(new ScopeData(createCallableScopeForLambda(ctx.lambdaArg())));
        Variable result = super.visitLambdaExpr(ctx);
        scopeDataContext.exitContext();
        return result;
    }

    @Override
    public Variable visitRunnableScope(GrammarParser.RunnableScopeContext ctx) {
        LocalScope localScope = new LocalScope(scopeDataContext.getCurrentContext().getScope(), scopeDataContext.getCurrentContext().getStatIndex());
        scopeDataContext.enterContext(new ScopeData(localScope));
        Variable result = super.visitRunnableScope(ctx);
        scopeDataContext.exitContext();
        return result;
    }

    @Override
    public Variable visitInitExpr(GrammarParser.InitExprContext ctx) {
        Variable leftSymbol = visit(ctx.op1);
        visit(ctx.op2);
        if (leftSymbol != null) {
            leftSymbol.addWritingExpression(ctx.op1);
        }
        return null;
    }

    @Override
    public Variable visitIdentifier(GrammarParser.IdentifierContext ctx) {
        String variableName = ctx.complexId().getText();
        if(variableName.equals(StandardKnowledges.VOID_TYPE_NAME)){
            throw new TypeErrorException("variable name can't be void");
        }
        Scope currentScope = scopeDataContext.getCurrentContext().getScope();
        Variable variable = currentScope.resolveVariable(variableName);
        Position position = new Position(ctx, currentScope, scopeDataContext.getCurrentContext().getStatIndex());
        if (variable == null) {
            variable = currentScope.defineVariable(typeInferenceMotor, variableName, position);
        } else {
            variable.addUseExpression(position);
        }
        if (ctx.PROVIDED_VARIABLE_INDICATOR() != null) {
            variable.setProvided();
        }
        return variable;
    }

    private Variable manageVisitIsStat(GrammarParser.StatContext ctx){
        Integer statIndex = scopeDataContext.getCurrentContext().getStatIndex();
        statIndex = (statIndex == null ? 0 : statIndex + 1);
        scopeDataContext.getCurrentContext().setStatIndex(statIndex);
        return visitFirstChild(ctx);
    }

    private Variable visitFirstChild(GrammarParser.StatContext statContext) {
        ParseTree child = statContext.getChild(0);
        if(child == null){
            throw new IllegalStateException();
        }
        return visit(child);
    }

    public NormalCallableScope createNormalCallableScope(GrammarParser.ExprContext topDefContext, GrammarParser.ArgsContext argsContext, boolean addThis) {
        checkArgNames(argsContext, false);
        NormalCallableScope normalCallableScope = new NormalCallableScope(globalScope);
        argsContext.arg().forEach(argContext -> {
            String name = argContext.complexId().getText();
            normalCallableScope.defineVariable(typeInferenceMotor, name, new Position(argContext.complexId(), normalCallableScope, -1));
        });
        if (addThis) {
            normalCallableScope.defineVariable(typeInferenceMotor, StandardKnowledges.THIS_VARIABLE_NAME, null);
        }
        typeInferenceMotor.putNormalCallableScope(topDefContext, normalCallableScope);
        return normalCallableScope;
    }

    private Scope createCallableScopeForLambda(GrammarParser.LambdaArgContext lambdaArgContext) {
        LambdaScope lambdaScope = new LambdaScope(scopeDataContext.getCurrentContext().getScope());
        lambdaArgContext.complexId().forEach(complexIdContext -> {
            String name = complexIdContext.getText();
            lambdaScope.defineVariable(typeInferenceMotor, name, new Position(complexIdContext, lambdaScope, -1));
        });
        return lambdaScope;
    }

    private void checkArgNames(GrammarParser.ArgsContext argsContext, boolean areNull) {
        Set<String> names = new HashSet<>();
        for (GrammarParser.ArgContext argContext : argsContext.arg()) {
            if ((argContext.complexId() == null) != areNull) {
                throw new WrongArgumentConventionException("argument name convention is unrespected");
            }
            if (!areNull) {
                names.add(argContext.complexId().ID().getText());
            }
        }
        if (!areNull && names.size() != argsContext.arg().size()) {
            throw new WrongArgumentConventionException("same argument name");
        }
    }

    public static String makeTreeString(ParseTree tree) {
        return makeTreeString(tree, 0);
    }

    private static String makeTreeString(ParseTree tree, int indentLevel) {
        if(tree instanceof TerminalNode){
            return makeIndentString(indentLevel) + tree.getText();
        }
        StringBuilder stringBuilder = new StringBuilder(makeIndentString(indentLevel) + ((RuleNode) tree).getClass().getSimpleName());
        for (int index = 0; index < tree.getChildCount(); index++) {
            stringBuilder.append(makeTreeString(tree.getChild(index), indentLevel + 1));
        }
        return stringBuilder.toString();
    }

    private static String makeIndentString(int indentLevel) {
        StringBuilder str = new StringBuilder();
        for (int index = 0; index < indentLevel; index++) {
            str.append('\t');
        }
        return str.toString();
    }
}
