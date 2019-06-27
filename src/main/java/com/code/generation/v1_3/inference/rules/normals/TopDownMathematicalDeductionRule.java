package com.code.generation.v1_3.inference.rules.normals;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.inference.rules.NormalRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TopDownMathematicalDeductionRule extends NormalRule {
    private static Function<List<Operable>, List<Operable>> OPERABLE_FUNCTION = operables -> {
        return operables.get(0).equals(Operable.INT) ? Arrays.asList(Operable.INT, Operable.INT) : null;
    };

    public TopDownMathematicalDeductionRule(Typable topTypable, Typable typable1, Typable typable2) {
        super(Collections.singletonList(topTypable), Arrays.asList(typable1, typable2), OPERABLE_FUNCTION);
    }
}
