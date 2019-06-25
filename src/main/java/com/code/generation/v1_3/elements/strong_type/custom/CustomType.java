package com.code.generation.v1_3.elements.strong_type.custom;

import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.StrongType;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.simple_types.NullStandardType;
import com.code.generation.v1_3.elements.type.standard.simple_types.VoidStandardType;

public class CustomType extends NormalType {
    private String name;
    private boolean isOperable;

    public CustomType(StrongTypeDirectory strongTypeDirectory, Type initialType) {
        super(strongTypeDirectory, initialType);
        if(initialType instanceof NullStandardType || initialType instanceof VoidStandardType){
            throw new IllegalStateException();
        }
        this.name = initialType.getSimpleName();
        this.isOperable = Operable.getFromName(name) != null;
    }

    @Override
    public boolean isSame(StrongType otherType) {
        return this == otherType;
    }

    @Override
    public String getComplexName() {
        return name;
    }

    public String getName() {
        return name;
    }

    public boolean isOperable() {
        return isOperable;
    }

    public boolean isStandard(){
        return initialType instanceof StandardType;
    }
}
