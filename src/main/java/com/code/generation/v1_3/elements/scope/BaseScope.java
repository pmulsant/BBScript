package com.code.generation.v1_3.elements.scope;

import com.code.generation.v1_3.elements.symbols.Position;
import com.code.generation.v1_3.elements.symbols.Variable;
import com.code.generation.v1_3.exception.VariableAlreadyDefinedException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseScope implements Scope {
    private Scope parentScope;

    private Map<String, Variable> variables = new HashMap<>();

    public BaseScope(Scope parentScope) {
        this.parentScope = parentScope;
    }

    @Override
    public Scope getParent() {
        return parentScope;
    }

    @Override
    public ProgScope searchProgScope() {
        if (this instanceof ProgScope) {
            return (ProgScope) this;
        }
        return parentScope != null ? parentScope.searchProgScope() : null;
    }

    @Override
    public void defineVariable(Variable variable) {
        if (resolveVariable(variable.getName()) != null) {
            throw new VariableAlreadyDefinedException(variable);
        }
        variables.put(variable.getName(), variable);
    }

    @Override
    public Variable defineVariable(TypeInferenceMotor typeInferenceMotor, String variableName, Position firstPosition) {
        if (resolveVariable(variableName) != null) {
            throw new VariableAlreadyDefinedException(variableName);
        }
        Variable variable = new Variable(typeInferenceMotor, this, variableName, firstPosition);
        putVariableNoError(variable);
        return variable;
    }

    protected void putVariableNoError(Variable variable){
        variables.put(variable.getName(), variable);
    }

    @Override
    public Variable resolveVariable(String name) {
        Variable variable = variables.get(name);
        if (variable == null && parentScope != null) {
            variable = parentScope.resolveVariable(name);
        }
        return variable;
    }

    @Override
    public void removeVariable(String variableName) {
        variables.remove(variableName);
    }
}
