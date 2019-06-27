package com.code.generation.v1_3.elements.strong_type.callables;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;
import com.code.generation.v1_3.elements.strong_type.VoidType;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.exception.ManyDefinitionForSameCallableFoundException;
import com.code.generation.v1_3.exception.NotAlwaysReturnException;
import com.code.generation.v1_3.exception.WrongParamNumberException;
import com.code.generation.v1_3.exception.for_type_checker.CantBeReturnedTypeException;
import com.code.generation.v1_3.visitors.after_deduced.result.ExpressionResult;
import com.code.generation.v1_3.visitors.after_deduced.result.RunnableScopeOrStatResult;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Callable implements ICallable {
    private StrongTypeDirectory strongTypeDirectory;
    protected List<Parameter> parameters;
    private CallableDefinition callableDefinition;

    public Callable(StrongTypeDirectory strongTypeDirectory, List<Parameter> parameters) {
        this.strongTypeDirectory = strongTypeDirectory;
        this.parameters = parameters;
    }

    @Override
    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public void checkParamsNumber(int paramsNumber) {
        if (paramsNumber != parameters.size()) {
            throw new WrongParamNumberException(paramsNumber, parameters.size());
        }
    }

    @Override
    public CallableDefinition getCallableDefinition() {
        return callableDefinition;
    }

    @Override
    public void setCallableDefinition(CallableDefinition callableDefinition) {
        if (this.callableDefinition != null && this.callableDefinition != callableDefinition) {
            throw new ManyDefinitionForSameCallableFoundException(this);
        }
        this.callableDefinition = callableDefinition;
        checkReturnCompatibility(callableDefinition);
    }

    @Override
    public final void checkReturnCompatibility(CallableDefinition callableDefinition) {
        CanAppearInReturnStat canAppearInReturnStat;
        RunnableScopeOrStatResult runnableScopeOrStatResult = null;
        if(callableDefinition.getRunnableScopeContext() != null) {
            runnableScopeOrStatResult = (RunnableScopeOrStatResult) callableDefinition.getTypeCheckerVisitor().visit(callableDefinition.getRunnableScopeContext());
            canAppearInReturnStat = runnableScopeOrStatResult.getReturnInterruption() != null ? runnableScopeOrStatResult.getReturnInterruption().getCanAppearInReturnStat() : null;
        } else {
            ExpressionResult expressionResult = (ExpressionResult) callableDefinition.getTypeCheckerVisitor().visit(callableDefinition.getExprContext());
            if(!(expressionResult.getStrongType() instanceof CanAppearInReturnStat)){
                throw new CantBeReturnedTypeException(expressionResult.getStrongType());
            }
            canAppearInReturnStat = (CanAppearInReturnStat) expressionResult.getStrongType();
        }
        if (canAppearInReturnStat == null || canAppearInReturnStat.isVoid()) {
            checkReturnCompatibility(strongTypeDirectory.getVoidStrongType());
            return;
        }
        if (runnableScopeOrStatResult != null && !runnableScopeOrStatResult.isAlwaysThrowsOrReturn()) {
            throw new NotAlwaysReturnException(this);
        }
        checkReturnCompatibility(canAppearInReturnStat);
    }

    protected String getParamsString() {
        List<String> params = getParameters().stream().map(aParam -> aParam.toString()).collect(Collectors.toList());
        return String.join(", ", params);
    }
}
