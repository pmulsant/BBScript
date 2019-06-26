package com.code.generation.v1_3.elements.strong_type.callables;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;
import com.code.generation.v1_3.elements.strong_type.CanBeReturnedType;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;

import java.util.List;
import java.util.stream.Collectors;

public class Function extends Callable implements NamedCallable, CanReturn {
    private String name;
    private CanBeReturnedType returnedType;

    public Function(StrongTypeDirectory strongTypeDirectory, String name, CanBeReturnedType returnedType, List<Parameter> parameters) {
        super(strongTypeDirectory, parameters);
        this.name = name;
        this.returnedType = returnedType;
    }

    @Override
    public CanBeReturnedType getReturnedType() {
        return returnedType;
    }

    @Override
    public String getName() {
        return name;
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
        return "function " + returnedType.getComplexName() + " " + name + "(" + getParamsString() + ")";
    }
}
