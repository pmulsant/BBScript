package com.code.generation.v1_3.elements.strong_type;

public interface CanBeReturnedType extends CanAppearInReturnStat {
    void assertIsCompatibleWithReturn(CanAppearInReturnStat canAppearInReturnStat);
    String getComplexName();
}
