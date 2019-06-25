package com.code.generation.v1_3.elements.type.custom.callables.simples;

import com.code.generation.v1_3.elements.type.custom.callables.ICallable;

public interface ISimpleCallable extends ICallable {
    Parameter getParameter(int index);
}
