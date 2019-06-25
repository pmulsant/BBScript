package com.code.generation.v1_3.elements.strong_type;

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
}
