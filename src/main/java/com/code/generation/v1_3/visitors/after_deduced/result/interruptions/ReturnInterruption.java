package com.code.generation.v1_3.visitors.after_deduced.result.interruptions;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;

public class ReturnInterruption implements Interruption {
    private CanAppearInReturnStat canAppearInReturnStat;

    public ReturnInterruption(CanAppearInReturnStat canAppearInReturnStat) {
        this.canAppearInReturnStat = canAppearInReturnStat;
    }

    public CanAppearInReturnStat getCanAppearInReturnStat() {
        return canAppearInReturnStat;
    }
}
