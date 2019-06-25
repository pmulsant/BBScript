package com.code.generation.v1_3.visitors.after_deduced.result.interruptions;

public class LoopInterruption implements Interruption {
    public static final LoopInterruption BREAK_INTERRUPTION = new LoopInterruption(true);
    public static final LoopInterruption CONTINUE_INTERRUPTION = new LoopInterruption(false);
    private boolean isBreak;

    private LoopInterruption(boolean isBreak) {
        this.isBreak = isBreak;
    }

    public boolean isBreak() {
        return isBreak;
    }
}
