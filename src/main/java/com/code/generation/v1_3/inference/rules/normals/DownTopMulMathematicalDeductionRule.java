package com.code.generation.v1_3.inference.rules.normals;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.exception.NonOperableException;
import com.code.generation.v1_3.inference.rules.NormalRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DownTopMulMathematicalDeductionRule extends NormalRule {
    public static Function<List<Operable>, List<Operable>> OPERABLE_FUNCTION = operables -> {
        boolean isThereFloat = false;
        for (Operable operable : operables) {
            if (!operable.isNumber()) {
                throw new NonOperableException("mul a non number");
            }
            operable.equals(Operable.FLOAT);
            isThereFloat = true;
        }
        return Collections.singletonList(isThereFloat ? Operable.FLOAT : Operable.INT);
    };

    public DownTopMulMathematicalDeductionRule(Typable typable1, Typable typable2, Typable topTypable) {
        super(Arrays.asList(typable1, typable2), Collections.singletonList(topTypable), OPERABLE_FUNCTION);
    }
}
