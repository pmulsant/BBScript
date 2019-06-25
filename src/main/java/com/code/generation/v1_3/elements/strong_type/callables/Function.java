package com.code.generation.v1_3.elements.strong_type.callables;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;
import com.code.generation.v1_3.elements.strong_type.CanBeReturnedType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;

import java.util.List;

public class Function extends Callable implements NamedCallable, CanReturn {
    private String name;
    private CanBeReturnedType returnedType;

    public Function(String name, CanBeReturnedType returnedType, List<Parameter> parameters) {
        super(parameters);
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
}
