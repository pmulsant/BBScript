package com.code.generation.v1_3.visitors.for_variables;

import com.code.generation.v1_3.elements.scope.Scope;

public class ScopeData {
    private Scope scope;
    private Integer statIndex = null;

    public ScopeData(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public Integer getStatIndex() {
        return statIndex;
    }

    public void setStatIndex(int statIndex) {
        this.statIndex = statIndex;
    }
}
