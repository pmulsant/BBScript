package com.code.generation.v1_3.elements.scope;

public class LocalScope extends BaseScope {
    private Integer statIndexInParentScope;

    public LocalScope(Scope parentScope, Integer statIndexInParentScope) {
        super(parentScope);
        this.statIndexInParentScope = statIndexInParentScope;
    }
}
