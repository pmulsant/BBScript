package com.code.generation.v1_3.elements.strong_type;

import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;

public interface CanBeReturnedType extends CanAppearInReturnStat {
    StrongTypeDirectory getStrongTypeDirectory();
    void assertIsCompatibleWithReturn(CanAppearInReturnStat canAppearInReturnStat);
    String getComplexName();
}
