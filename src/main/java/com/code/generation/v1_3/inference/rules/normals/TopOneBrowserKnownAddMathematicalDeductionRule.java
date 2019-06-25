package com.code.generation.v1_3.inference.rules.normals;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.inference.rules.NormalRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TopOneBrowserKnownAddMathematicalDeductionRule extends NormalRule {
    private static Function<List<Operable>, List<Operable>> OPERABLE_FUNCTION = operables -> {
        Operable topOperable = operables.get(0);
        Operable childOperable = operables.get(1);
        if (topOperable.isNumber() && childOperable.isNumber()) {
            return TopOneBrowserKnownMulMathematicalDeductionRule.OPERABLE_FUNCTION.apply(operables);
        }
        if (!topOperable.equals(Operable.STRING)) {
            throw new IllegalStateException();
        }
        return childOperable.equals(Operable.STRING) ? null : Collections.singletonList(Operable.STRING);
    };

    public TopOneBrowserKnownAddMathematicalDeductionRule(Typable topTypable, Typable knownTypable, Typable targetTypable) {
        super(Arrays.asList(topTypable, knownTypable), Collections.singletonList(targetTypable), OPERABLE_FUNCTION);
    }
}
