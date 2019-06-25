package com.code.generation.v1_3.inference.rules.normals;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.inference.rules.NormalRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TopOneBrowserKnownMulMathematicalDeductionRule extends NormalRule {
    public static Function<List<Operable>, List<Operable>> OPERABLE_FUNCTION = operables -> {
        Operable topOperable = operables.get(0);
        Operable childOperable = operables.get(1);
        if (!topOperable.isNumber() || !childOperable.isNumber()) {
            throw new IllegalStateException();
        }
        if (topOperable.equals(Operable.INT)) {
            return Collections.singletonList(Operable.INT);
        }
        if (childOperable.equals(Operable.INT)) {
            return Collections.singletonList(Operable.FLOAT);
        }
        return null;
    };

    public TopOneBrowserKnownMulMathematicalDeductionRule(Typable topTypable, Typable knownTypable, Typable targetTypable) {
        super(Arrays.asList(topTypable, knownTypable), Collections.singletonList(targetTypable), OPERABLE_FUNCTION);
    }
}
