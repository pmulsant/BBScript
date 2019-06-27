package com.code.generation.v1_3.inference.rules;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TopOneBrowserIsNumberMathematicalDeductionRule extends SpecialRule {
    private static final Function<List<Type>, List<Operable>> OPERABLE_FUNCTION = types -> {
        Type topType = types.get(0);
        Type childType = types.get(1);
        boolean isNotAString = childType.isAppearOnNumberSpecificOperation();
        String topTypeName = topType.getSimpleName();
        if(topTypeName == null){
            return null;
        }
        if (topTypeName.equals(StandardKnowledges.STRING_TYPE_NAME) && isNotAString) {
            return Collections.singletonList(Operable.STRING);
        }
        return null;
    };

    public TopOneBrowserIsNumberMathematicalDeductionRule(Typable topTypable, Typable childTypable, Typable otherTypable) {
        super(Arrays.asList(topTypable, childTypable), Collections.singletonList(otherTypable), OPERABLE_FUNCTION);
    }
}
