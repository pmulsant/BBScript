package com.code.generation.v1_3.elements.scope;

public class NormalCallableScope extends CallableScope {
    private NormalCallableKind normalCallableKind;

    public NormalCallableScope(GlobalScope globalScope, NormalCallableKind normalCallableKind) {
        super(globalScope);
        this.normalCallableKind = normalCallableKind;
    }

    public boolean haveThisVariableDefined() {
        return normalCallableKind.equals(NormalCallableKind.CONSTRUCTOR) || normalCallableKind.equals(NormalCallableKind.METHOD);
    }
}
