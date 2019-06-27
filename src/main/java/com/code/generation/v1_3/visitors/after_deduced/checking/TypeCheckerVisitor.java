package com.code.generation.v1_3.visitors.after_deduced.checking;

import com.code.generation.v1_3.elements.strong_type.*;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.callables.*;
import com.code.generation.v1_3.elements.strong_type.custom.Attribute;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.symbols.Variable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.callables.functions.StandardFunction;
import com.code.generation.v1_3.exception.NonAssignableExpression;
import com.code.generation.v1_3.exception.NonOperableException;
import com.code.generation.v1_3.exception.for_type_checker.CantBeProvideForParameterException;
import com.code.generation.v1_3.exception.for_type_checker.CantBeReturnedTypeException;
import com.code.generation.v1_3.exception.for_type_checker.CantHaveLambdaHereException;
import com.code.generation.v1_3.exception.for_type_checker.IsNotNormalTypeException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.util.AccessibleTopContext;
import com.code.generation.v1_3.visitors.after_deduced.result.ExpressionResult;
import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import com.code.generation.v1_3.visitors.after_deduced.result.RunnableScopeOrStatResult;
import com.code.generation.v1_3.visitors.after_deduced.result.interruptions.Interruption;
import com.code.generation.v1_3.visitors.after_deduced.result.interruptions.LoopInterruption;
import com.code.generation.v1_3.visitors.after_deduced.result.interruptions.ReturnInterruption;
import com.code.generation.v1_3.visitors.after_deduced.result.interruptions.ThrowInterruption;
import com.generated.GrammarBaseVisitor;
import com.generated.GrammarParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;
import java.util.stream.Collectors;

public class TypeCheckerVisitor extends GrammarBaseVisitor<Result> {
    private Map<ParserRuleContext, Result> resultMap;

    private StrongTypeDirectory strongTypeDirectory;
    private Map<GrammarParser.IdentifierContext, StrongVariable> variableMap = new IdentityHashMap<>();

    private NullType nullType;
    private VoidType voidType;

    private CustomType intType;
    private CustomType floatType;
    private CustomType charType;
    private CustomType booleanType;
    private CustomType stringType;
    private CustomType regexType;

    private Map<GrammarParser.LambdaExprContext, Lambda> lambdas = new IdentityHashMap<>();

    private AccessibleTopContext<LambdaType> expectedLambdaContext = new AccessibleTopContext<>();
    private AccessibleTopContext<ICallable> callableContext = new AccessibleTopContext<>();

    public TypeCheckerVisitor(TypeInferenceMotor typeInferenceMotor, StrongTypeDirectory strongTypeDirectory, Map<ParserRuleContext, Result> resultMap) {
        this.strongTypeDirectory = strongTypeDirectory;
        this.resultMap = resultMap;

        nullType = strongTypeDirectory.getNullType();
        voidType = strongTypeDirectory.getVoidStrongType();

        intType = strongTypeDirectory.getStrongType(Operable.INT);
        floatType = strongTypeDirectory.getStrongType(Operable.FLOAT);
        charType = strongTypeDirectory.getStrongType(Operable.CHAR);
        booleanType = strongTypeDirectory.getStrongType(Operable.BOOLEAN);
        stringType = strongTypeDirectory.getStrongType(Operable.STRING);
        Map<GrammarParser.IdentifierContext, Variable> variables = typeInferenceMotor.getVariables();
        regexType = strongTypeDirectory.getStrongTypeRegex();

        variables.forEach(((identifierContext, variable) -> {
            StrongType strongType = strongTypeDirectory.getStrongType(variable.getType());
            if (!(strongType instanceof NormalType)) {
                throw new IllegalStateException("variable must be of normal type");
            }
            variableMap.put(identifierContext, new StrongVariable(variable, (NormalType) strongType));
        }));
    }

    @Override
    public Result visitIntExpr(GrammarParser.IntExprContext ctx) {
        return new ExpressionResult(intType, resultMap, ctx);
    }

    @Override
    public Result visitFloatExpr(GrammarParser.FloatExprContext ctx) {
        return new ExpressionResult(floatType, resultMap, ctx);
    }

    @Override
    public Result visitCharExpr(GrammarParser.CharExprContext ctx) {
        return new ExpressionResult(charType, resultMap, ctx);
    }

    @Override
    public Result visitBooleanExpr(GrammarParser.BooleanExprContext ctx) {
        return new ExpressionResult(booleanType, resultMap, ctx);
    }

    @Override
    public Result visitStringExpr(GrammarParser.StringExprContext ctx) {
        return new ExpressionResult(stringType, resultMap, ctx);
    }

    @Override
    public Result visitRegularExpr(GrammarParser.RegularExprContext ctx) {
        return new ExpressionResult(regexType, resultMap, ctx);
    }

    @Override
    public Result visitNullExpr(GrammarParser.NullExprContext ctx) {
        return new ExpressionResult(nullType, resultMap, ctx);
    }

    @Override
    public Result visitIdentifier(GrammarParser.IdentifierContext ctx) {
        StrongVariable variable = variableMap.get(ctx);
        checkTypeCompatibility(variable.getNormalType(), ctx.complexId().type());
        return new ExpressionResult(variable.getNormalType(), resultMap, ctx, variable);
    }

    @Override
    public Result visitInstantiation(GrammarParser.InstantiationContext ctx) {
        return manageInstantiation(ctx, ctx.type(), ctx.args(), null);
    }

    @Override
    public Result visitInstantiationCallAndDef(GrammarParser.InstantiationCallAndDefContext ctx) {
        return manageInstantiation(ctx, ctx.type(), ctx.args(), ctx.runnableScope());
    }

    @Override
    public Result visitAttributeCall(GrammarParser.AttributeCallContext ctx) {
        ExpressionResult innerExpressionResult = (ExpressionResult) visit(ctx.expr());
        if (!(innerExpressionResult.getStrongType() instanceof NormalType)) {
            throw new IllegalStateException("non normal type can't have attribute");
        }
        NormalType normalInnerType = (NormalType) innerExpressionResult.getStrongType();
        String attributeName = ctx.complexId().ID().getText();
        Attribute attribute = normalInnerType.getAttributes().get(attributeName);
        if (attribute == null) {
            throw new IllegalStateException(normalInnerType + " doesn't have attribute " + attributeName);
        }
        return new ExpressionResult(attribute.getNormalType(), resultMap, ctx, attribute);
    }

    @Override
    public Result visitMethodCall(GrammarParser.MethodCallContext ctx) {
        return manageMethodCall(ctx, ctx.expr(), ctx.complexId(), ctx.args(), null);
    }

    @Override
    public Result visitMethodCallAndDef(GrammarParser.MethodCallAndDefContext ctx) {
        return manageMethodCall(ctx, ctx.expr(), ctx.complexId(), ctx.args(), ctx.runnableScope());
    }

    @Override
    public Result visitFunctionCall(GrammarParser.FunctionCallContext ctx) {
        return manageFunctionCall(ctx, ctx.complexId(), ctx.args(), null);
    }

    @Override
    public Result visitFunctionCallAndDef(GrammarParser.FunctionCallAndDefContext ctx) {
        return manageFunctionCall(ctx, ctx.complexId(), ctx.args(), ctx.runnableScope());
    }


    @Override
    public Result visitNegation(GrammarParser.NegationContext ctx) {
        ExpressionResult expressionResult = (ExpressionResult) visit(ctx.expr());
        assertIsBoolean(expressionResult.getStrongType());
        return new ExpressionResult(booleanType, resultMap, ctx);
    }

    @Override
    public Result visitMulExpr(GrammarParser.MulExprContext ctx) {
        StrongType strongType1 = ((ExpressionResult) visit(ctx.op1)).getStrongType();
        StrongType strongType2 = ((ExpressionResult) visit(ctx.op2)).getStrongType();
        return manageNumberOperation(strongType1, strongType2, ctx);
    }

    @Override
    public Result visitAddExpr(GrammarParser.AddExprContext ctx) {
        StrongType strongType1 = ((ExpressionResult) visit(ctx.op1)).getStrongType();
        StrongType strongType2 = ((ExpressionResult) visit(ctx.op2)).getStrongType();
        if (isNumber(strongType1) && isNumber(strongType2)) {
            return manageNumberOperation(strongType1, strongType2, ctx);
        }
        assertIsOperable(strongType1);
        assertIsOperable(strongType2);
        if (!stringType.isSame(strongType1) && !stringType.isSame(strongType2)) {
            throw new NonOperableException();
        }
        return new ExpressionResult(stringType, resultMap, ctx);
    }

    @Override
    public Result visitCompExpr(GrammarParser.CompExprContext ctx) {
        StrongType strongType1 = ((ExpressionResult) visit(ctx.op1)).getStrongType();
        StrongType strongType2 = ((ExpressionResult) visit(ctx.op2)).getStrongType();
        assertIsNumberAndReturnTrueIsFloat(strongType1);
        assertIsNumberAndReturnTrueIsFloat(strongType2);
        return new ExpressionResult(booleanType, resultMap, ctx);
    }

    @Override
    public Result visitEqualsExpr(GrammarParser.EqualsExprContext ctx) {
        assertCanBeComparedAndReturnLeftExpressionResult(ctx.op1, ctx.op2);
        return new ExpressionResult(booleanType, resultMap, ctx);
    }

    @Override
    public Result visitAndExpr(GrammarParser.AndExprContext ctx) {
        return manageBinaryBooleanOperation(ctx.op1, ctx.op2, ctx);
    }

    @Override
    public Result visitOrExpr(GrammarParser.OrExprContext ctx) {
        return manageBinaryBooleanOperation(ctx.op1, ctx.op2, ctx);
    }

    @Override
    public Result visitInnerExpr(GrammarParser.InnerExprContext ctx) {
        ExpressionResult expressionResult = (ExpressionResult) visit(ctx.expr());
        if (!(expressionResult.getStrongType() instanceof CanAppearInReturnStat) && ((CanAppearInReturnStat) expressionResult).isVoid()) {
            throw new IllegalStateException("unappropriate type : " + expressionResult.getStrongType());
        }
        return expressionResult;
    }

    @Override
    public Result visitInitExpr(GrammarParser.InitExprContext ctx) {
        ExpressionResult leftExpressionResult = assertCanBeComparedAndReturnLeftExpressionResult(ctx.op1, ctx.op2);
        if (!leftExpressionResult.isAssignable()) {
            throw new NonAssignableExpression();
        }
        return new ExpressionResult(leftExpressionResult.getStrongType(), resultMap, ctx);
    }

    @Override
    public Result visitLambdaExpr(GrammarParser.LambdaExprContext ctx) {
        LambdaType expectedLambdaType = expectedLambdaContext.getCurrentContext();
        if (expectedLambdaType == null) {
            throw new CantHaveLambdaHereException(callableContext.getCurrentContext());
        }
        CallableDefinition callableDefinition;
        if (ctx.lambdaProcess().expr() != null) {
            callableDefinition = new CallableDefinition(this, ctx.lambdaProcess().expr());
        } else {
            callableDefinition = new CallableDefinition(this, ctx.lambdaProcess().runnableScope());
        }
        lambdas.put(ctx, expectedLambdaType.buildLambda(this, callableDefinition, ctx.lambdaArg()));
        return new ExpressionResult(expectedLambdaType, resultMap, ctx);
    }

    @Override
    public Result visitEnrichedExpr(GrammarParser.EnrichedExprContext ctx) {
        return visit(ctx.expr());
    }

    private void assertIsOperable(StrongType strongType) {
        if (!(strongType instanceof CustomType) || !((CustomType) strongType).isOperable()) {
            throw new NonOperableException("type " + strongType + " is not operable");
        }
    }

    public Result manageNumberOperation(StrongType number1, StrongType number2, GrammarParser.ExprContext exprContext) {
        boolean isFloat = assertIsNumberAndReturnTrueIsFloat(number1) || assertIsNumberAndReturnTrueIsFloat(number2);
        return new ExpressionResult(isFloat ? floatType : intType, resultMap, exprContext);
    }

    /*****************stat and runnable scope***********************/

    @Override
    public Result visitRunnableScope(GrammarParser.RunnableScopeContext ctx) {
        List<Interruption> interruptions = new LinkedList<>();
        boolean canContinue = true;
        boolean isAlwaysThrowsOrReturn = false;
        for (GrammarParser.StatContext statContext : ctx.stat()) {
            if (!canContinue) {
                throw new IllegalStateException("error can't continue after a stat which always interrupt");
            }
            RunnableScopeOrStatResult runnableScopeOrStatResult = (RunnableScopeOrStatResult) visit(statContext);
            interruptions.addAll(runnableScopeOrStatResult.getInterruptions());
            if (runnableScopeOrStatResult.isAlwaysInterrupt()) {
                canContinue = false;
                isAlwaysThrowsOrReturn = runnableScopeOrStatResult.isAlwaysThrowsOrReturn();
            }
        }
        return new RunnableScopeOrStatResult(interruptions, !canContinue, isAlwaysThrowsOrReturn, resultMap, ctx);
    }

    @Override
    public Result visitSimpleStat(GrammarParser.SimpleStatContext ctx) {
        visit(ctx.expr());
        return new RunnableScopeOrStatResult(resultMap, ctx);
    }

    @Override
    public Result visitIfStat(GrammarParser.IfStatContext ctx) {
        boolean withElse = ctx.elseChunk() != null;
        List<RunnableScopeOrStatResult> alternativeResults = new LinkedList<>();
        alternativeResults.add((RunnableScopeOrStatResult) visit(ctx.ifChunk()));
        alternativeResults.addAll(ctx.elseIfChunk().stream().map(elseIfChunkContext -> {
            return (RunnableScopeOrStatResult) visit(elseIfChunkContext);
        }).collect(Collectors.toList()));
        if (withElse) {
            alternativeResults.add((RunnableScopeOrStatResult) visit(ctx.elseChunk()));
        }

        List<Interruption> interruptions = new LinkedList<>();
        boolean isAlwaysInterrupt = withElse;
        boolean isAlwaysThrowOrReturn = withElse;
        for (RunnableScopeOrStatResult alternativeResult : alternativeResults) {
            interruptions.addAll(alternativeResult.getInterruptions());
            if (!alternativeResult.isAlwaysInterrupt()) {
                isAlwaysInterrupt = false;
            }
            if (!alternativeResult.isAlwaysThrowsOrReturn()) {
                isAlwaysThrowOrReturn = false;
            }
        }
        return new RunnableScopeOrStatResult(interruptions, isAlwaysInterrupt, isAlwaysThrowOrReturn, resultMap, ctx);
    }

    @Override
    public Result visitIfChunk(GrammarParser.IfChunkContext ctx) {
        return manageBooleanRunnableScope(ctx.expr(), ctx.runnableScope());
    }

    @Override
    public Result visitElseIfChunk(GrammarParser.ElseIfChunkContext ctx) {
        return manageBooleanRunnableScope(ctx.expr(), ctx.runnableScope());
    }

    @Override
    public Result visitWhileStat(GrammarParser.WhileStatContext ctx) {
        RunnableScopeOrStatResult runnableScopeOrStatResult = manageBooleanRunnableScope(ctx.expr(), ctx.runnableScope());
        List<Interruption> interruptions = runnableScopeOrStatResult.getInterruptions();
        interruptions.remove(LoopInterruption.BREAK_INTERRUPTION);
        interruptions.remove(LoopInterruption.CONTINUE_INTERRUPTION);
        boolean isAlwaysThrowsOrReturn = runnableScopeOrStatResult.isAlwaysThrowsOrReturn();
        return new RunnableScopeOrStatResult(interruptions, isAlwaysThrowsOrReturn, isAlwaysThrowsOrReturn, resultMap, ctx);
    }

    @Override
    public Result visitThrowStat(GrammarParser.ThrowStatContext ctx) {
        ExpressionResult expressionResult = (ExpressionResult) visit(ctx.expr());
        StrongType strongType = expressionResult.getStrongType();
        if (!(strongType instanceof NormalType)) {
            throw new IllegalStateException("can't throw a non normal type");
        }
        return new RunnableScopeOrStatResult(new ThrowInterruption((NormalType) strongType), resultMap, ctx);
    }

    @Override
    public Result visitBreakStat(GrammarParser.BreakStatContext ctx) {
        return new RunnableScopeOrStatResult(LoopInterruption.BREAK_INTERRUPTION, resultMap, ctx);
    }

    @Override
    public Result visitContinueStat(GrammarParser.ContinueStatContext ctx) {
        return new RunnableScopeOrStatResult(LoopInterruption.CONTINUE_INTERRUPTION, resultMap, ctx);
    }

    @Override
    public Result visitReturnStat(GrammarParser.ReturnStatContext ctx) {
        if (callableContext.getCurrentContext() == null) {
            throw new IllegalStateException("can't use return here");
        }
        CanAppearInReturnStat canAppearInReturnStat = null;
        if (ctx.expr() == null) {
            canAppearInReturnStat = voidType;
        } else {
            StrongType strongType = ((ExpressionResult) visit(ctx.expr())).getStrongType();
            if (!(strongType instanceof CanAppearInReturnStat)) {
                throw new IllegalStateException("can't appear in return stat");
            }
            canAppearInReturnStat = (CanAppearInReturnStat) strongType;
        }
        return new RunnableScopeOrStatResult(new ReturnInterruption(canAppearInReturnStat), resultMap, ctx);
    }

    /*******************************/

    private Result manageInstantiation(GrammarParser.ExprContext topContext, GrammarParser.TypeContext constructorType, GrammarParser.ArgsContext argsContext, GrammarParser.RunnableScopeContext runnableScopeContext) {
        NormalType topNormalType = strongTypeDirectory.getStrongType(constructorType);
        int paramsNumber = argsContext.arg().size();

        Constructor constructor;
        GenericConstructor genericConstructor = strongTypeDirectory.getGenericConstructor(constructorType, paramsNumber);
        if (genericConstructor != null) {
            StrongType topType = strongTypeDirectory.getStrongType(topContext);
            if (!(topType instanceof NormalType)) {
                throw new IsNotNormalTypeException(topType);
            }
            List<CanBeProvideForParameter> arguments = getArguments(argsContext, false);
            constructor = genericConstructor.makeStrongConstructor((NormalType) topType, arguments);
        } else {
            constructor = topNormalType.getConstructors().get(paramsNumber);
        }

        if (constructor == null) {
            throw new IllegalStateException("no constructor found for type " + topNormalType);
        }
        return manageCallable(topContext, constructor, topNormalType, argsContext, runnableScopeContext);
    }

    private Result manageMethodCall(GrammarParser.ExprContext topContext, GrammarParser.ExprContext innerExprContext, GrammarParser.ComplexIdContext complexIdContext,
                                    GrammarParser.ArgsContext argsContext, GrammarParser.RunnableScopeContext runnableScopeContext) {
        StrongType innerType = ((ExpressionResult) visit(innerExprContext)).getStrongType();

        if (!(innerType instanceof NormalType)) {
            throw new IllegalStateException("can't call method of non normal type");
        }
        NormalType innerNormalType = (NormalType) innerType;
        String methodName = complexIdContext.ID().getText();

        GenericMethod genericMethod = strongTypeDirectory.getGenericMethod(methodName);
        Method method;
        if (genericMethod != null) {
            StrongType strongType = strongTypeDirectory.getStrongType(topContext);
            if (!(strongType instanceof CanBeReturnedType)) {
                throw new CantBeReturnedTypeException(strongType);
            }
            List<CanBeProvideForParameter> arguments = getArguments(argsContext, true);
            method = genericMethod.makeStrongMethod(innerNormalType, (CanBeReturnedType) strongType, arguments);
        } else {
            method = innerNormalType.getMethods().get(methodName);
        }

        if (method == null) {
            throw new IllegalStateException("no method " + methodName + " for type " + innerNormalType);
        }
        return manageCallable(topContext, method, method.getReturnedType(), argsContext, runnableScopeContext);
    }

    private Result manageFunctionCall(GrammarParser.ExprContext topContext, GrammarParser.ComplexIdContext complexIdContext, GrammarParser.ArgsContext argsContext, GrammarParser.RunnableScopeContext runnableScopeContext) {
        String functionName = complexIdContext.ID().getText();

        StandardFunction standardFunction = strongTypeDirectory.getStandardFunction(functionName);
        Function function;
        if (standardFunction != null) {
            StrongType returned = strongTypeDirectory.getStrongType(topContext);
            if (!(returned instanceof CanBeReturnedType)) {
                throw new CantBeReturnedTypeException(returned);
            }
            List<CanBeProvideForParameter> arguments = getArguments(argsContext, false);
            function = standardFunction.makeStrongFunction((CanBeReturnedType) returned, arguments);
        } else {
            function = strongTypeDirectory.getFunction(functionName);
        }

        if (function == null) {
            throw new IllegalStateException("no function with name " + functionName);
        }
        return manageCallable(topContext, function, function.getReturnedType(), argsContext, runnableScopeContext);
    }

    private List<CanBeProvideForParameter> getArguments(GrammarParser.ArgsContext argsContext, boolean canHaveLambdaParameter) {
        List<CanBeProvideForParameter> arguments = new ArrayList<>(argsContext.arg().size());
        for (GrammarParser.ArgContext argContext : argsContext.arg()) {
            if(canHaveLambdaParameter){
                StrongType strongType = strongTypeDirectory.getStrongType(argContext.expr());
                if(strongType instanceof LambdaType) {
                    arguments.add((LambdaType) strongType);
                    continue;
                }
            }
            StrongType argument = ((ExpressionResult) visit(argContext.expr())).getStrongType();
            if (!(argument instanceof CanBeProvideForParameter)) {
                throw new CantBeProvideForParameterException(argument);
            }
            arguments.add((CanBeProvideForParameter) argument);
        }
        return arguments;
    }

    private Result manageCallable(GrammarParser.ExprContext topContext, ICallable callable, CanBeReturnedType topType, GrammarParser.ArgsContext argsContext, GrammarParser.RunnableScopeContext runnableScopeContext) {
        if (callable == null) {
            throw new IllegalStateException("no callable found");
        }
        callable.checkParamsNumber(argsContext.arg().size());
        int index = 0;
        for (GrammarParser.ArgContext argContext : argsContext.arg()) {
            Parameter parameter = callable.getParameters().get(index);
            StrongType argType;
            if (parameter.getType() instanceof LambdaType) {
                expectedLambdaContext.enterContext((LambdaType) parameter.getType());
                argType = ((ExpressionResult) visit(argContext.expr())).getStrongType();
                expectedLambdaContext.exitContext();
            } else {
                argType = ((ExpressionResult) visit(argContext.expr())).getStrongType();
            }
            if (!(argType instanceof CanBeProvideForParameter)) {
                throw new IllegalStateException("can't be provide for parameter");
            }
            parameter.getType().checkIsCompatibleWithArgument((CanBeProvideForParameter) argType);
            index++;
        }
        if (runnableScopeContext != null) {
            callableContext.enterContext(callable);
            callable.setCallableDefinition(new CallableDefinition(this, runnableScopeContext));
            callableContext.exitContext();
        }
        return new ExpressionResult(topType, resultMap, topContext);
    }

    private Result manageBinaryBooleanOperation(GrammarParser.ExprContext op1, GrammarParser.ExprContext op2, GrammarParser.ExprContext topContext) {
        assertIsBoolean(((ExpressionResult) visit(op1)).getStrongType());
        assertIsBoolean(((ExpressionResult) visit(op2)).getStrongType());
        return new ExpressionResult(booleanType, resultMap, topContext);
    }

    private RunnableScopeOrStatResult manageBooleanRunnableScope(GrammarParser.ExprContext exprContext, GrammarParser.RunnableScopeContext runnableScopeContext) {
        ExpressionResult expressionResult = (ExpressionResult) visit(exprContext);
        assertIsBoolean(expressionResult.getStrongType());
        return (RunnableScopeOrStatResult) visit(runnableScopeContext);
    }

    private void assertIsBoolean(StrongType strongType) {
        booleanType.assertIsSame(strongType);
    }

    private boolean assertIsNumberAndReturnTrueIsFloat(StrongType strongType) {
        if (intType.isSame(strongType)) {
            return false;
        }
        if (floatType.isSame(strongType)) {
            return true;
        }
        throw new NonOperableException("expected number");
    }

    private boolean isNumber(StrongType strongType) {
        if (strongType == null) {
            return false;
        }
        return intType.isSame(strongType) || floatType.isSame(strongType);
    }

    public void checkTypeCompatibility(CanAppearInReturnStat strongType, GrammarParser.TypeContext typeContext) {
        if (typeContext == null) {
            return;
        }
        CanBeReturnedType expectedType = strongTypeDirectory.getStrongType(typeContext);
        expectedType.assertIsCompatibleWithReturn(strongType);
    }

    public ExpressionResult assertCanBeComparedAndReturnLeftExpressionResult(GrammarParser.ExprContext op1, GrammarParser.ExprContext op2) {
        ExpressionResult leftExpressionResult = (ExpressionResult) visit(op1);
        StrongType strongType1 = leftExpressionResult.getStrongType();
        StrongType strongType2 = ((ExpressionResult) visit(op2)).getStrongType();
        if (!(strongType1 instanceof CanAppearInReturnStat)) {
            throw new IllegalStateException("unapropriate type " + strongType1);
        }
        if (!(strongType2 instanceof CanAppearInReturnStat)) {
            throw new IllegalStateException("unapropriate type " + strongType2);
        }
        CanAppearInReturnStat type1 = (CanAppearInReturnStat) strongType1;
        CanAppearInReturnStat type2 = (CanAppearInReturnStat) strongType2;
        if (type1 instanceof CanBeReturnedType && type2 instanceof CanBeReturnedType) {
            // non null types
            ((CanBeReturnedType) type1).assertIsCompatibleWithReturn(type2);
        }
        return leftExpressionResult;
    }

    public Map<ParserRuleContext, Result> getResultMap() {
        return resultMap;
    }
}
