package com.code.generation.v1_3.listeners;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.simples.ISimpleCallable;

public class TopCallableContext {
    private Typable innerTypable;
    private ISimpleCallable callable;

    public TopCallableContext(ISimpleCallable callable) {
        this(null, callable);
    }

    public TopCallableContext(Typable innerTypable, ISimpleCallable callable) {
        this.innerTypable = innerTypable;
        this.callable = callable;
    }

    public Typable getInnerTypable() {
        return innerTypable;
    }

    public ISimpleCallable getCallable() {
        return callable;
    }
}
