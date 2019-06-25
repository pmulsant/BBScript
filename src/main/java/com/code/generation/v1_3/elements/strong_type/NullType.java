package com.code.generation.v1_3.elements.strong_type;

import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;

public class NullType implements CanAppearInReturnStat, CanBeProvideForParameter {
    public static final NullType INSTANCE = new NullType();

    private NullType() {
    }

    @Override
    public void build() {
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String toString() {
        return StandardKnowledges.NULL_KEY_WORD;
    }
}
