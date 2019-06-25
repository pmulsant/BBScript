package com.code.generation.v1_3.elements.strong_type;

import com.code.generation.v1_3.elements.symbols.Variable;

public class StrongVariable implements Assignable {
    private Variable variable;

    private String name;
    private NormalType normalType;

    public StrongVariable(Variable variable, NormalType normalType) {
        this.variable = variable;
        this.name = variable.getName();
        this.normalType = normalType;
    }

    public String getNameWithDollar() {
        return (variable.isProvided() ? "$" : "") + name;
    }

    public NormalType getNormalType() {
        return normalType;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public String toDefinitionStatementString() {
        return normalType.getComplexName() + " " + getNameWithDollar() + ";";
    }
}
