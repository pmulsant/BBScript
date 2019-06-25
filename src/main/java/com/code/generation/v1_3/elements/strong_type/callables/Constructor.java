package com.code.generation.v1_3.elements.strong_type.callables;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.exception.ConstructorCantReturnException;

import java.util.List;

public class Constructor extends Callable implements LinkedToType {
    private NormalType normalType;

    public Constructor(NormalType normalType, List<Parameter> parameters) {
        super(parameters);
        this.normalType = normalType;
    }

    @Override
    public NormalType getNormalType() {
        return normalType;
    }

    @Override
    public void checkReturnCompatibility(CanAppearInReturnStat canAppearInReturnStat) {
        if (!canAppearInReturnStat.isVoid()) {
            throw new ConstructorCantReturnException(this);
        }
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
