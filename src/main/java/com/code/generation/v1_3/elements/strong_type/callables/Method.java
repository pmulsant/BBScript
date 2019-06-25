package com.code.generation.v1_3.elements.strong_type.callables;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;
import com.code.generation.v1_3.elements.strong_type.CanBeReturnedType;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;

import java.util.List;

public class Method extends Callable implements NamedCallable, CanReturn, LinkedToType {
    private NormalType normalType;
    private String name;
    private CanBeReturnedType returnedType;

    public Method(NormalType normalType, String name, CanBeReturnedType returnedType, List<Parameter> parameters) {
        super(parameters);
        this.normalType = normalType;
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
    public NormalType getNormalType() {
        return normalType;
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
        return "method " + returnedType.getComplexName() + " " + normalType.getComplexName() + "." + name + "(" + getParamsString() + ")";
    }
}
