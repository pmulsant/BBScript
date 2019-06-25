package com.code.generation.v1_3.elements.type.custom.callables.complex;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.ICallable;

import java.util.List;

public interface IGenericCallable extends ICallable {
    boolean processLinkStandardTypeParameters(List<? extends Typable> typableArguments);
}
