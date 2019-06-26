package com.code.generation.v1_3.inference.rules.normals;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.exception.NonOperableException;
import com.code.generation.v1_3.inference.rules.NormalRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DownTopAddMathematicalDeductionRule extends NormalRule {
    private static final Function<List<Operable>, List<Operable>> OPERABLE_FUNCTION = operables -> {
        Operable operable1 = operables.get(0);
        Operable operable2 = operables.get(1);
        if (operable1.isNumber() && operable2.isNumber()) {
            return DownTopMulMathematicalDeductionRule.OPERABLE_FUNCTION.apply(operables);
        }
        if (operable1.equals(Operable.STRING) || operable2.equals(Operable.STRING)) {
            return Collections.singletonList(Operable.STRING);
        }
        throw new NonOperableException();
    };

    public DownTopAddMathematicalDeductionRule(Typable typable1, Typable typable2, Typable topTypable) {
        super(Arrays.asList(typable1, typable2), Collections.singletonList(topTypable), OPERABLE_FUNCTION);
    }
}
