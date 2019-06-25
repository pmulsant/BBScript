package com.code.generation.v1_3.visitors.after_deduced.result.interruptions;

import com.code.generation.v1_3.elements.strong_type.NormalType;

public class ThrowInterruption implements Interruption {
    private NormalType normalType;

    public ThrowInterruption(NormalType normalType) {
        this.normalType = normalType;
    }

    public NormalType getNormalType() {
        return normalType;
    }
}
