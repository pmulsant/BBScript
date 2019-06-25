package com.code.generation.v1_3.inference.rules;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.exception.NonOperableException;
import com.code.generation.v1_3.exception.impossible.ImpossibleException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class NormalRule extends Rule {
    private Function<List<Operable>, List<Operable>> operablesFunction;

    public NormalRule(List<Typable> knownTypables, List<Typable> targetTypables, Function<List<Operable>, List<Operable>> operablesFunction) {
        super(knownTypables, targetTypables);
        this.operablesFunction = operablesFunction;
    }

    @Override
    protected List<Operable> getResult() {
        List<Operable> knownOperables = getParameterTypables().stream().map(typable -> Operable.getFromName(typable.getType().getSimpleName())).collect(Collectors.toList());
        if(knownOperables.contains(null)){
            throw new NonOperableException("operation contains a non operable");
        }
        return operablesFunction.apply(knownOperables);
    }
}
