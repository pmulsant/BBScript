package com.code.generation.v1_3.elements.strong_type.callables;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;

import java.util.List;

public interface ICallable {
    List<Parameter> getParameters();

    void checkParamsNumber(int paramsNumber);

    void checkReturnCompatibility(CallableDefinition callableDefinition);

    void checkReturnCompatibility(CanAppearInReturnStat canAppearInReturnStat);

    boolean isVoid();

    CallableDefinition getCallableDefinition();

    void setCallableDefinition(CallableDefinition callableDefinition);
}
