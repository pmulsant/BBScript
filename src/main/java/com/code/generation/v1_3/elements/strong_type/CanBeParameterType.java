package com.code.generation.v1_3.elements.strong_type;

public interface CanBeParameterType extends CanBeProvideForParameter {
    void checkIsCompatibleWithArgument(CanBeProvideForParameter canBeProvideForParameter);
}
