package com.code.generation.v1_3.inference.rules;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.standard.Operable;

import java.util.ArrayList;
import java.util.List;

public abstract class Rule {
    private List<Typable> parameterTypables;
    private List<Typable> targetTypables;

    public Rule(List<Typable> parameterTypables, List<Typable> targetTypables) {
        this.parameterTypables = parameterTypables;
        this.targetTypables = targetTypables;
    }

    public List<Typable> getParameterTypables() {
        return parameterTypables;
    }

    public List<Typable> getTargetTypables() {
        return targetTypables;
    }

    public List<Deduction> apply() {
        List<Operable> operables = getResult();
        if (operables == null) {
            return null;
        }
        if (operables.size() != targetTypables.size()) {
            throw new IllegalStateException();
        }
        List<Deduction> result = new ArrayList<>(operables.size());
        int index = 0;
        for (Operable operable : operables) {
            result.add(new Deduction(targetTypables.get(index), operable));
            index++;
        }
        return result;
    }

    protected abstract List<Operable> getResult();
}
