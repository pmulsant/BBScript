package com.code.generation.v1_3.elements.scope;

import com.code.generation.v1_3.elements.symbols.Position;
import com.code.generation.v1_3.elements.symbols.Variable;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

public interface Scope {

    CallableScope searchCallableScope();

    ProgScope searchProgScope();

    void defineVariable(Variable variable);

    Variable defineVariable(TypeInferenceMotor typeInferenceMotor, String variableName, Position firstPosition);

    void removeVariable(String name);

    Variable resolveVariable(String name);
}
