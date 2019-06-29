package com.code.generation.v1_3.visitors.for_variables;

import com.code.generation.v1_3.elements.scope.*;
import com.code.generation.v1_3.elements.symbols.Position;
import com.code.generation.v1_3.elements.symbols.Variable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.exception.CantUseThisOutsideConstructorOrMethodException;
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
        manageArgumentExpressionsBeforeChangingScope(null, ctx.args());
        NormalCallableScope normalCallableScope = createNormalCallableScope(ctx, ctx.args(), NormalCallableKind.CONSTRUCTOR);
        scopeDataContext.enterContext(new ScopeData(normalCallableScope));
        Variable result = visit(ctx.runnableScope());
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
        manageArgumentExpressionsBeforeChangingScope(ctx.expr(), ctx.args());
        scopeDataContext.enterContext(new ScopeData(createNormalCallableScope(ctx, ctx.args(), NormalCallableKind.METHOD)));
        Variable result = visit(ctx.runnableScope());
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
        manageArgumentExpressionsBeforeChangingScope(null, ctx.args());
        scopeDataContext.enterContext(new ScopeData(createNormalCallableScope(ctx, ctx.args(), NormalCallableKind.FUNCTION)));
        Variable result = visit(ctx.runnableScope());
        scopeDataContext.exitContext();
        return result;
    }

    @Override
    public Variable visitLambdaExpr(GrammarParser.LambdaExprContext ctx) {
        Scope lambdaScope = createCallableScopeForLambda(ctx.lambdaArg());
        scopeDataContext.enterContext(new ScopeData(lambdaScope));
        ScopeData context = new ScopeData(new LocalScope(lambdaScope, 0));
        initializeOrIncrementStatIndex(context);
        scopeDataContext.enterContext(context);
        Variable result = visit(ctx.lambdaProcess());
        scopeDataContext.exitContext();
        return result;
    }

    private void initializeOrIncrementStatIndex(ScopeData context) {
        Integer statIndex = context.getStatIndex();
        statIndex = (statIndex == null ? 0 : statIndex + 1);
        context.setStatIndex(statIndex);
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
        String variableName = ctx.complexId().ID().getText();
        if (variableName.equals(StandardKnowledges.VOID_TYPE_NAME)) {
            throw new TypeErrorException("variable name can't be void");
        }
        if (variableName.equals(StandardKnowledges.THIS_VARIABLE_NAME)) {
            if (scopeDataContext.getCurrentContext() == null) {
                throw new CantUseThisOutsideConstructorOrMethodException();
            }
            CallableScope callableScope = scopeDataContext.getCurrentContext().getScope().searchCallableScope();
            if (!(callableScope instanceof NormalCallableScope)) {
                throw new CantUseThisOutsideConstructorOrMethodException();
            }
            if (!((NormalCallableScope) callableScope).haveThisVariableDefined()) {
                throw new CantUseThisOutsideConstructorOrMethodException();
            }
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

    /**********************/

    private Variable manageVisitIsStat(GrammarParser.StatContext ctx) {
        initializeOrIncrementStatIndex(scopeDataContext.getCurrentContext());
        return visitFirstChild(ctx);
    }

    private Variable visitFirstChild(GrammarParser.StatContext statContext) {
        ParseTree child = statContext.getChild(0);
        if (child == null) {
            throw new IllegalStateException();
        }
        return visit(child);
    }

    private void manageArgumentExpressionsBeforeChangingScope(GrammarParser.ExprContext innerExprCtx, GrammarParser.ArgsContext argsContext) {
        if (innerExprCtx != null) {
            visit(innerExprCtx);
        }
        for (GrammarParser.ArgContext argContext : argsContext.arg()) {
            visit(argContext.expr());
        }
    }

    private NormalCallableScope createNormalCallableScope(GrammarParser.ExprContext topDefContext, GrammarParser.ArgsContext argsContext, NormalCallableKind normalCallableKind) {
        checkArgNames(argsContext, false);
        NormalCallableScope normalCallableScope = new NormalCallableScope(globalScope, normalCallableKind);
        argsContext.arg().forEach(argContext -> {
            String name = argContext.complexId().ID().getText();
            normalCallableScope.defineVariable(typeInferenceMotor, name, new Position(argContext.complexId(), normalCallableScope, -1));
        });
        if (normalCallableKind.equals(NormalCallableKind.CONSTRUCTOR) || normalCallableKind.equals(NormalCallableKind.METHOD)) {
            normalCallableScope.defineVariable(typeInferenceMotor, StandardKnowledges.THIS_VARIABLE_NAME, null);
        }
        typeInferenceMotor.putNormalCallableScope(topDefContext, normalCallableScope);
        return normalCallableScope;
    }

    private Scope createCallableScopeForLambda(GrammarParser.LambdaArgContext lambdaArgContext) {
        LambdaScope lambdaScope = new LambdaScope(scopeDataContext.getCurrentContext().getScope());
        lambdaArgContext.complexId().forEach(complexIdContext -> {
            String name = complexIdContext.ID().getText();
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
        if (tree instanceof TerminalNode) {
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
