package com.code.generation.v1_3.exception.for_type_checker.for_interruptions;

import com.code.generation.RuntimeCustomException;

public class CantContinueAfterAnAlwaysInterruptStatement extends RuntimeCustomException {
    public CantContinueAfterAnAlwaysInterruptStatement() {
        super("error can't continue after a stat which always interrupt");
    }
}
