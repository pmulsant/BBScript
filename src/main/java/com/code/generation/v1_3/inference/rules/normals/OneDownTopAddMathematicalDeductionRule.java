package com.code.generation.v1_3.inference.rules.normals;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.inference.rules.NormalRule;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class OneDownTopAddMathematicalDeductionRule extends NormalRule {
    private static Function<List<Operable>, List<Operable>> OPERABLE_FUNCTION = operables -> {
        if (operables.get(0).isNumber()) {
            return null;
        }
        return Collections.singletonList(Operable.STRING);
    };

    public OneDownTopAddMathematicalDeductionRule(Typable childTypable, Typable topTypable) {
        super(Collections.singletonList(childTypable), Collections.singletonList(topTypable), OPERABLE_FUNCTION);
    }
}
