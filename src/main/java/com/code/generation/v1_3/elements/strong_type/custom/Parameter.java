package com.code.generation.v1_3.elements.strong_type.custom;

import com.code.generation.v1_3.elements.strong_type.CanBeParameterType;

public class Parameter {
    private String name;
    private CanBeParameterType type;

    public Parameter(String name, CanBeParameterType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public CanBeParameterType getType() {
        return type;
    }
}
