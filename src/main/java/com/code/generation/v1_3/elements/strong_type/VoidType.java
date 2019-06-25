package com.code.generation.v1_3.elements.strong_type;

import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.exception.WrongTypeFormatException;

public class VoidType implements StrongType, CanBeReturnedType {
    public static final VoidType INSTANCE = new VoidType();

    private VoidType() {
    }

    @Override
    public void build() {
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public void assertIsCompatibleWithReturn(CanAppearInReturnStat canAppearInReturnStat) {
        if (!canAppearInReturnStat.isVoid()) {
            throw new WrongTypeFormatException(canAppearInReturnStat, "not compatible with return");
        }
    }

    @Override
    public String getComplexName() {
        return StandardKnowledges.VOID_TYPE_NAME;
    }
}
