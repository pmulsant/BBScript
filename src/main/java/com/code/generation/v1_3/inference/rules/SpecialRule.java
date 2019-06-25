package com.code.generation.v1_3.inference.rules;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.standard.Operable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpecialRule extends Rule {

    private Function<List<Type>, List<Operable>> operablesFunction;

    public SpecialRule(List<Typable> parameterTypables, List<Typable> targetTypables, Function<List<Type>, List<Operable>> operablesFunction) {
        super(parameterTypables, targetTypables);
        this.operablesFunction = operablesFunction;
    }

    @Override
    protected List<Operable> getResult() {
        return operablesFunction.apply(getParameterTypables().stream().map(typable -> typable.getType()).collect(Collectors.toList()));
    }
}
