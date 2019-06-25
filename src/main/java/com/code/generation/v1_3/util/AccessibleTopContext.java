package com.code.generation.v1_3.util;

import java.util.Stack;

public class AccessibleTopContext<T> {
    private T currentContext;
    private Stack<T> previousContexts = new Stack<>();

    public void enterContext(T context) {
        previousContexts.push(currentContext);
        currentContext = context;
    }

    public void exitContext() {
        if (currentContext == null) {
            throw new IllegalStateException();
        }
        currentContext = previousContexts.pop();
    }

    public T getCurrentContext() {
        return currentContext;
    }
}
