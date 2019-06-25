package com.code.generation.v1_3.elements.scope;

import com.code.generation.v1_3.elements.symbols.Variable;

public abstract class CallableScope extends BaseScope {
    public CallableScope(Scope parentScope) {
        super(parentScope);
    }
}
