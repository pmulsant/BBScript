package com.code.generation.v1_3.inference.rules.normals;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.exception.NonOperableException;
import com.code.generation.v1_3.inference.rules.NormalRule;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class OneDownTopMulMathematicalDeductionRule extends NormalRule {
    private static Function<List<Operable>, List<Operable>> OPERABLE_FUNCTION = operables -> {
        Operable operable = operables.get(0);
        if (!operable.isNumber()) {
            throw new NonOperableException("mul a non number");
        }
        return operable.equals(Operable.FLOAT) ? Collections.singletonList(Operable.FLOAT) : null;
    };

    public OneDownTopMulMathematicalDeductionRule(Typable childTypable, Typable topTypable) {
        super(Collections.singletonList(childTypable), Collections.singletonList(topTypable), OPERABLE_FUNCTION);
    }
}
