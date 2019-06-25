package com.code.generation.v1_3.elements.strong_type.custom;

import com.code.generation.v1_3.elements.strong_type.Assignable;
import com.code.generation.v1_3.elements.strong_type.NormalType;

public class Attribute implements Assignable {
    private String name;
    private NormalType normalType;
    private boolean isStrong;

    public Attribute(String name, NormalType normalType, boolean isStrong) {
        this.name = name;
        this.normalType = normalType;
        this.isStrong = isStrong;
    }

    public String getName() {
        return name;
    }

    public NormalType getNormalType() {
        return normalType;
    }

    public boolean isStrong() {
        return isStrong;
    }

    @Override
    public String toDefinitionStatementString() {
        return (isStrong ? "(strong) " : "") + normalType.getComplexName() + " " + name;
    }
}
