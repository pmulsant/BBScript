package com.code.generation.v1_3.elements.strong_type.callables;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;
import com.code.generation.v1_3.elements.strong_type.VoidType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.exception.ManyDefinitionForSameCallableFoundException;
import com.code.generation.v1_3.exception.NotAlwaysReturnException;
import com.code.generation.v1_3.exception.WrongParamNumberException;
import com.code.generation.v1_3.visitors.after_deduced.result.RunnableScopeOrStatResult;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Callable implements ICallable {
    protected List<Parameter> parameters;
    private CallableDefinition callableDefinition;

    public Callable(List<Parameter> parameters) {
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
        RunnableScopeOrStatResult runnableScopeOrStatResult = (RunnableScopeOrStatResult) callableDefinition.getTypeCheckerVisitor().visit(callableDefinition.getRunnableScopeContext());
        CanAppearInReturnStat canAppearInReturnStat = runnableScopeOrStatResult.getReturnInterruption() != null ? runnableScopeOrStatResult.getReturnInterruption().getCanAppearInReturnStat() : null;
        if (canAppearInReturnStat == null || canAppearInReturnStat.isVoid()) {
            checkReturnCompatibility(VoidType.INSTANCE);
            return;
        }
        if (!runnableScopeOrStatResult.isAlwaysThrowsOrReturn()) {
            throw new NotAlwaysReturnException(this);
        }
        checkReturnCompatibility(canAppearInReturnStat);
    }

    protected String getParamsString() {
        List<String> params = getParameters().stream().map(aParam -> aParam.toString()).collect(Collectors.toList());
        return String.join(", ", params);
    }
}
