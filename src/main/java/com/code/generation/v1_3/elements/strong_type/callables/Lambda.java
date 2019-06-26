package com.code.generation.v1_3.elements.strong_type.callables;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;
import com.code.generation.v1_3.elements.strong_type.CanBeReturnedType;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;

import java.util.List;

public class Lambda extends Callable implements CanReturn {
    private CanBeReturnedType returnedType;

    public Lambda(StrongTypeDirectory strongTypeDirectory, CanBeReturnedType returnedType, List<Parameter> parameters, CallableDefinition callableDefinition) {
        super(strongTypeDirectory, parameters);
        this.returnedType = returnedType;
        this.setCallableDefinition(callableDefinition);
    }

    @Override
    public CanBeReturnedType getReturnedType() {
        return returnedType;
    }

    @Override
    public void checkReturnCompatibility(CanAppearInReturnStat canAppearInReturnStat) {
        returnedType.assertIsCompatibleWithReturn(canAppearInReturnStat);
    }

    @Override
    public boolean isVoid() {
        return returnedType.isVoid();
    }

    @Override
    public String toString() {
        return "lambda (" + getParamsString() + ") -> " + returnedType.getComplexName();
    }
}
