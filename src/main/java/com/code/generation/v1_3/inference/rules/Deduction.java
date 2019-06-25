package com.code.generation.v1_3.inference.rules;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;

public class Deduction {
    private Typable typable;
    private Operable operable;

    public Deduction(Typable typable, Operable operable) {
        this.typable = typable;
        this.operable = operable;
    }

    public Typable getTypable() {
        return typable;
    }

    public Operable getOperable() {
        return operable;
    }
}
