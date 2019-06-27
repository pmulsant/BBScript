package com.code.generation.v1_3.listeners;

import com.code.generation.v1_3.elements.symbols.Variable;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.custom.Attribute;
import com.code.generation.v1_3.elements.type.custom.callables.ICallable;
import com.code.generation.v1_3.elements.type.custom.callables.simples.*;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.exception.ConstructorCantReturnException;
import com.code.generation.v1_3.exception.UnexpectedReturnStatException;
import com.code.generation.v1_3.exception.WrongUsageOfThisException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.inference.rules.TopOneBrowserIsNumberMathematicalDeductionRule;
import com.code.generation.v1_3.inference.rules.normals.*;
import com.code.generation.v1_3.util.AccessibleTopContext;
import com.generated.GrammarBaseListener;
import com.generated.GrammarParser;

import java.util.List;
import java.util.stream.Collectors;

public class DeductionListener extends GrammarBaseListener {
    private TypeInferenceMotor typeInferenceMotor;
    private AccessibleTopContext<TopCallableContext> topContext = new AccessibleTopContext<>();

    public DeductionListener(TypeInferenceMotor typeInferenceMotor) {
        this.typeInferenceMotor = typeInferenceMotor;
    }

    @Override
    public void enterIntExpr(GrammarParser.IntExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, typeInferenceMotor.getStandardTypable(Operable.INT.getName()));
    }

    @Override
    public void enterFloatExpr(GrammarParser.FloatExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, typeInferenceMotor.getStandardTypable(Operable.FLOAT.getName()));
    }

    @Override
    public void enterCharExpr(GrammarParser.CharExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, typeInferenceMotor.getStandardTypable(Operable.CHAR.getName()));
    }

    @Override
    public void enterBooleanExpr(GrammarParser.BooleanExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, typeInferenceMotor.getStandardTypable(Operable.BOOLEAN.getName()));
    }

    @Override
    public void enterStringExpr(GrammarParser.StringExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, typeInferenceMotor.getStandardTypable(Operable.STRING.getName()));
    }

    @Override
    public void enterRegularExpr(GrammarParser.RegularExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, typeInferenceMotor.getStandardTypable(StandardKnowledges.REGEX_TYPE_NAME));
    }

    @Override
    public void enterNullExpr(GrammarParser.NullExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, typeInferenceMotor.getStandardTypable(StandardKnowledges.NULL_KEY_WORD));
    }

    @Override
    public void enterIdentifier(GrammarParser.IdentifierContext ctx) {
        Typable typable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        if (ctx.complexId().type() != null) {
            typable.getType().setName(ctx.complexId().type());
        }
        typeInferenceMotor.addFusionOfTypesDeclaration(typable, typeInferenceMotor.getVariable(ctx));
        if (ctx.complexId().ID().getText().equals(StandardKnowledges.THIS_VARIABLE_NAME)) {
            Typable currentTypable = topContext.getCurrentContext().getInnerTypable();
            if (currentTypable == null) {
                throw new WrongUsageOfThisException();
            }
            typeInferenceMotor.addFusionOfTypesDeclaration(typable, currentTypable);
        }
    }

    /********************************************************/

    @Override
    public void enterInstantiation(GrammarParser.InstantiationContext ctx) {
        manageInstantiation(ctx, ctx.type(), ctx.args(), null);
    }

    @Override
    public void enterInstantiationCallAndDef(GrammarParser.InstantiationCallAndDefContext ctx) {
        manageInstantiation(ctx, ctx.type(), ctx.args(), ctx.runnableScope());
    }

    @Override
    public void exitInstantiationCallAndDef(GrammarParser.InstantiationCallAndDefContext ctx) {
        topContext.exitContext();
    }

    @Override
    public void enterAttributeCall(GrammarParser.AttributeCallContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        Type innerType = typeInferenceMotor.getTypableExpressionFromExpr(ctx.expr(), false).getType();
        if (ctx.complexId().type() != null) {
            innerType.setName(ctx.complexId().type());
        }
        Attribute attribute = innerType.getAttribute(ctx.complexId().ID().getText(), ctx.STRONG_LINK_OP() != null);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, attribute);
    }

    @Override
    public void enterMethodCall(GrammarParser.MethodCallContext ctx) {
        manageMethodCall(ctx, ctx.expr(),
                ctx.complexId(), ctx.args(), null);
    }

    @Override
    public void enterMethodCallAndDef(GrammarParser.MethodCallAndDefContext ctx) {
        manageMethodCall(ctx, ctx.expr(),
                ctx.complexId(), ctx.args(), ctx.runnableScope());
    }

    @Override
    public void exitMethodCallAndDef(GrammarParser.MethodCallAndDefContext ctx) {
        manageNoReturnCase();
        topContext.exitContext();
    }

    private void manageNoReturnCase(){
        TopCallableContext currentContext = topContext.getCurrentContext();
        if(!currentContext.isReturn()){
            ISimpleCallable callable = currentContext.getCallable();
            if(callable instanceof CanReturn){
                Typable returned = ((CanReturn) callable).getReturnedTypable();
                typeInferenceMotor.addFusionOfTypesDeclaration(returned, typeInferenceMotor.getStandardTypable(StandardKnowledges.VOID_TYPE_NAME));
            }
        }
    }

    @Override
    public void enterFunctionCall(GrammarParser.FunctionCallContext ctx) {
        manageFunctionCall(ctx, ctx.complexId(),
                ctx.args(), null);
    }

    @Override
    public void enterFunctionCallAndDef(GrammarParser.FunctionCallAndDefContext ctx) {
        manageFunctionCall(ctx, ctx.complexId(),
                ctx.args(), ctx.runnableScope());
    }

    @Override
    public void exitFunctionCallAndDef(GrammarParser.FunctionCallAndDefContext ctx) {
        manageNoReturnCase();
        topContext.exitContext();
    }

    @Override
    public void enterReturnStat(GrammarParser.ReturnStatContext ctx) {
        manageReturnProcess(ctx.expr());
    }

    @Override
    public void enterIfChunk(GrammarParser.IfChunkContext ctx) {
        linkExpressionToBooleanStandardType(ctx.expr());
    }

    @Override
    public void enterElseIfChunk(GrammarParser.ElseIfChunkContext ctx) {
        linkExpressionToBooleanStandardType(ctx.expr());
    }

    @Override
    public void enterWhileStat(GrammarParser.WhileStatContext ctx) {
        linkExpressionToBooleanStandardType(ctx.expr());
    }

    /********************************************/

    @Override
    public void enterNegation(GrammarParser.NegationContext ctx) {
        linkExpressionToBooleanStandardType(ctx);
        linkExpressionToBooleanStandardType(ctx.expr());
    }

    @Override
    public void enterMulExpr(GrammarParser.MulExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        topTypable.getType().setAppearInNumberSpecificOperation();
        Typable typable1 = typeInferenceMotor.getTypableExpressionFromExpr(ctx.op1, false);
        typable1.getType().setAppearInNumberSpecificOperation();
        Typable typable2 = typeInferenceMotor.getTypableExpressionFromExpr(ctx.op2, false);
        typable2.getType().setAppearInNumberSpecificOperation();
        typeInferenceMotor.addRule(new TopDownMathematicalDeductionRule(topTypable, typable1, typable2));
        typeInferenceMotor.addRule(new DownTopMulMathematicalDeductionRule(typable1, typable2, topTypable));
        typeInferenceMotor.addRule(new OneDownTopMulMathematicalDeductionRule(typable1, topTypable));
        typeInferenceMotor.addRule(new OneDownTopMulMathematicalDeductionRule(typable2, topTypable));
        typeInferenceMotor.addRule(new TopOneBrowserKnownMulMathematicalDeductionRule(topTypable, typable1, typable2));
        typeInferenceMotor.addRule(new TopOneBrowserKnownMulMathematicalDeductionRule(topTypable, typable2, typable1));
    }

    @Override
    public void enterAddExpr(GrammarParser.AddExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        Typable typable1 = typeInferenceMotor.getTypableExpressionFromExpr(ctx.op1, false);
        Typable typable2 = typeInferenceMotor.getTypableExpressionFromExpr(ctx.op2, false);

        if(ctx.SUB() != null){
            topTypable.getType().setAppearInNumberSpecificOperation();
            typable1.getType().setAppearInNumberSpecificOperation();
            typable2.getType().setAppearInNumberSpecificOperation();
        } else {
            topTypable.getType().setOperable();
            typable1.getType().setOperable();
            typable2.getType().setOperable();
        }

        // add normal rules
        typeInferenceMotor.addRule(new TopDownMathematicalDeductionRule(topTypable, typable1, typable2));
        typeInferenceMotor.addRule(new DownTopAddMathematicalDeductionRule(typable1, typable2, topTypable));
        typeInferenceMotor.addRule(new OneDownTopAddMathematicalDeductionRule(typable1, topTypable));
        typeInferenceMotor.addRule(new OneDownTopAddMathematicalDeductionRule(typable2, topTypable));
        typeInferenceMotor.addRule(new TopOneBrowserKnownAddMathematicalDeductionRule(topTypable, typable1, typable2));
        typeInferenceMotor.addRule(new TopOneBrowserKnownAddMathematicalDeductionRule(topTypable, typable2, typable1));

        // add special rules
        typeInferenceMotor.addRule(new TopOneBrowserIsNumberMathematicalDeductionRule(topTypable, typable1, typable2));
        typeInferenceMotor.addRule(new TopOneBrowserIsNumberMathematicalDeductionRule(topTypable, typable2, typable1));
    }

    @Override
    public void enterCompExpr(GrammarParser.CompExprContext ctx) {
        linkExpressionToBooleanStandardType(ctx);
        typeInferenceMotor.getTypableExpressionFromExpr(ctx.op1, false).getType().setAppearInNumberSpecificOperation();
        typeInferenceMotor.getTypableExpressionFromExpr(ctx.op2, false).getType().setAppearInNumberSpecificOperation();
    }

    @Override
    public void enterEqualsExpr(GrammarParser.EqualsExprContext ctx) {
        linkExpressionToBooleanStandardType(ctx);
        Typable typable1 = typeInferenceMotor.getTypableExpressionFromExpr(ctx.op1, false);
        Typable typable2 = typeInferenceMotor.getTypableExpressionFromExpr(ctx.op2, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(typable1, typable2);
    }

    @Override
    public void enterAndExpr(GrammarParser.AndExprContext ctx) {
        linkExpressionToBooleanStandardType(ctx);
        linkExpressionToBooleanStandardType(ctx.op1);
        linkExpressionToBooleanStandardType(ctx.op2);
    }

    @Override
    public void enterOrExpr(GrammarParser.OrExprContext ctx) {
        linkExpressionToBooleanStandardType(ctx);
        linkExpressionToBooleanStandardType(ctx.op1);
        linkExpressionToBooleanStandardType(ctx.op2);
    }

    @Override
    public void enterInnerExpr(GrammarParser.InnerExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable,
                typeInferenceMotor.getTypableExpressionFromExpr(ctx.expr(), false));
    }

    @Override
    public void enterInitExpr(GrammarParser.InitExprContext ctx) {
        Typable typable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        typeInferenceMotor.addFusionOfTypesDeclaration(typable, typeInferenceMotor.getTypableExpressionFromExpr(ctx.op1, false));
        typeInferenceMotor.addFusionOfTypesDeclaration(typable, typeInferenceMotor.getTypableExpressionFromExpr(ctx.op2, false));
    }

    @Override
    public void enterEnrichedExpr(GrammarParser.EnrichedExprContext ctx) {
        typeInferenceMotor.addFusionOfTypesDeclaration(typeInferenceMotor.getTypableExpressionFromExpr(ctx, null),
                typeInferenceMotor.getTypableExpressionFromExpr(ctx.expr(), null));
    }

    @Override
    public void enterLambdaExpr(GrammarParser.LambdaExprContext ctx) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, false);
        Lambda lambda = topTypable.getType().setLambda(ctx.lambdaArg().complexId().size());
        bindArgsExprToParameters(lambda, null, ctx.lambdaArg().complexId());
        topContext.enterContext(new TopCallableContext(lambda));
        GrammarParser.ExprContext expr = ctx.lambdaProcess().expr();
        if (expr != null) {
            manageReturnProcess(expr);
            return;
        }
    }

    @Override
    public void exitLambdaExpr(GrammarParser.LambdaExprContext ctx) {
        topContext.exitContext();
    }

    @Override
    public void enterArg(GrammarParser.ArgContext ctx) {
        if (ctx.complexId() != null) {
            Variable variable = typeInferenceMotor.getVariableParameter(ctx.complexId());
            if (variable == null) {
                throw new IllegalStateException();
            }
            typeInferenceMotor.addFusionOfTypesDeclaration(typeInferenceMotor.getTypableExpressionFromExpr(ctx.expr(), false), variable);
        }
    }

    /**************************/

    private void manageInstantiation(GrammarParser.ExprContext topDefContext, GrammarParser.TypeContext typeContext, GrammarParser.ArgsContext argsContext, GrammarParser.RunnableScopeContext runnableScopeContext) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(topDefContext, false);
        Type aType = topTypable.getType();
        aType.setName(typeContext);
        Constructor constructor = aType.getConstructor(argsContext.arg().size());
        bindArgsExprToParameters(constructor, argsContext);

        boolean isDefined = runnableScopeContext != null;
        typeInferenceMotor.processLinkIfStandardConstructor(topTypable, constructor, isDefined, typeContext);
        if (isDefined) {
            topContext.enterContext(new TopCallableContext(topTypable, constructor));
            manageThisVariable(topDefContext, topTypable);
        }
    }

    private void manageMethodCall(GrammarParser.ExprContext topDefContext, GrammarParser.ExprContext innerExprContext, GrammarParser.ComplexIdContext complexIdContext, GrammarParser.ArgsContext argsContext, GrammarParser.RunnableScopeContext runnableScopeContext) {
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(topDefContext, null);
        topTypable.getType().setName(complexIdContext.type());
        Typable innerTypable = typeInferenceMotor.getTypableExpressionFromExpr(innerExprContext, false);
        String methodName = complexIdContext.ID().getText();
        Method method = innerTypable.getType().getMethod(methodName, argsContext.arg().size());
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, method.getReturnedTypable());
        bindArgsExprToParameters(method, argsContext);

        boolean isDefined = runnableScopeContext != null;
        typeInferenceMotor.processLinkIfStandardMethod(innerTypable, method, isDefined);
        if (isDefined) {
            topContext.enterContext(new TopCallableContext(innerTypable, method));
            manageThisVariable(topDefContext, innerTypable);
        }
    }

    private void manageThisVariable(GrammarParser.ExprContext topDefContext, Typable innerTypable) {
        Variable thisVariable = typeInferenceMotor.getThisVariable(topDefContext);
        typeInferenceMotor.addFusionOfTypesDeclaration(thisVariable, innerTypable);
    }

    private void manageFunctionCall(GrammarParser.ExprContext ctx, GrammarParser.ComplexIdContext complexIdContext, GrammarParser.ArgsContext argsContext, GrammarParser.RunnableScopeContext runnableScopeContext) {
        Typable typable = typeInferenceMotor.getTypableExpressionFromExpr(ctx, null);
        typable.getType().setName(complexIdContext.type());
        String functionName = complexIdContext.ID().getText();
        Function function = typeInferenceMotor.getFunction(functionName, argsContext.arg().size());
        typeInferenceMotor.addFusionOfTypesDeclaration(typable, function.getReturnedTypable());
        bindArgsExprToParameters(function, argsContext);

        boolean isDefined = runnableScopeContext != null;
        typeInferenceMotor.processLinkIfStandardFunction(function, isDefined);
        if (isDefined) {
            topContext.enterContext(new TopCallableContext(function));
        }
    }

    public void bindArgsExprToParameters(ISimpleCallable simpleCallable, GrammarParser.ArgsContext argsContext) {

        List<GrammarParser.ComplexIdContext> complexIdContexts = argsContext.arg().stream().map(argContext -> argContext.complexId()).collect(Collectors.toList());
        List<GrammarParser.ExprContext> exprContexts = argsContext.arg().stream().map(argContext -> argContext.expr()).collect(Collectors.toList());
        bindArgsExprToParameters(simpleCallable, exprContexts, complexIdContexts);
    }

    private void bindArgsExprToParameters(ISimpleCallable callable, List<GrammarParser.ExprContext> exprContexts, List<GrammarParser.ComplexIdContext> complexIdContexts){
        int paramsNumber = complexIdContexts.size();
        for (int index = 0; index < paramsNumber; index++) {
            Parameter parameter = callable.getParameter(index);
            GrammarParser.ComplexIdContext complexIdContext = complexIdContexts.get(index);
            if (complexIdContext != null) { // there is a Variable
                parameter.setName(complexIdContext.ID().getText());
                Variable variable = typeInferenceMotor.getVariableParameter(complexIdContext);
                typeInferenceMotor.addFusionOfTypesDeclaration(variable, parameter);
                if (complexIdContext.type() != null) {
                    parameter.getType().setName(complexIdContext.type());
                }
            }
            GrammarParser.ExprContext exprContext = exprContexts == null ? null : exprContexts.get(index);
            if(exprContext != null) {
                Typable argumentTypable = typeInferenceMotor.getTypableExpressionFromExpr(exprContext, false);
                typeInferenceMotor.addFusionOfTypesDeclaration(argumentTypable, parameter);
            }
        }
    }

    private void manageReturnProcess(GrammarParser.ExprContext exprContext) {
        TopCallableContext currentContext = topContext.getCurrentContext();
        currentContext.setReturn();
        if (currentContext == null) {
            throw new UnexpectedReturnStatException("not in callable");
        }
        ICallable currentCallable = currentContext.getCallable();
        if (exprContext != null) {
            Typable returnedTypableObject = typeInferenceMotor.getTypableExpressionFromExpr(exprContext,
                    currentCallable instanceof Lambda ? null : false);
            if (!(currentCallable instanceof CanReturn)) {
                throw new ConstructorCantReturnException(currentCallable);
            }
            CanReturn canReturn = (CanReturn) currentCallable;
            typeInferenceMotor.addFusionOfTypesDeclaration(canReturn.getReturnedTypable(), returnedTypableObject);
            return;
        }
        if (currentCallable instanceof CanReturn) {
            ((CanReturn) currentCallable).getReturnedTypable().getType().setVoid(true);
        }
    }

    private void linkExpressionToBooleanStandardType(GrammarParser.ExprContext exprContext){
        Typable topTypable = typeInferenceMotor.getTypableExpressionFromExpr(exprContext, false);
        Typable booleanTypable = typeInferenceMotor.getStandardTypable(StandardKnowledges.BOOLEAN_TYPE_NAME);
        typeInferenceMotor.addFusionOfTypesDeclaration(topTypable, booleanTypable);
    }
}
