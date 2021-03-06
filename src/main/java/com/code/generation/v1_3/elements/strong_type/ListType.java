package com.code.generation.v1_3.elements.strong_type;

import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.type.Type;

public class ListType extends NormalType {
    private NormalType innerType;
    private int power;
    private String deepestCustomTypeName;

    public ListType(StrongTypeDirectory strongTypeDirectory, Type initialType, NormalType innerType) {
        super(strongTypeDirectory, initialType);
        this.innerType = innerType;
        if (innerType instanceof CustomType) {
            power = 1;
            deepestCustomTypeName = ((CustomType) innerType).getName();
            return;
        }
        ListType innerListType = (ListType) innerType;
        power = innerListType.getPower() + 1;
        deepestCustomTypeName = innerListType.getDeepestCustomTypeName();
    }

    @Override
    public boolean isSame(StrongType otherType) {
        if (!(otherType instanceof ListType)) {
            return false;
        }
        return innerType.isSame(((ListType) otherType).innerType);
    }

    @Override
    public void build() {

    }

    @Override
    public String getComplexName() {
        return "List<" + innerType.getComplexName() + ">";
    }

    public NormalType getInnerType() {
        return innerType;
    }

    public int getPower() {
        return power;
    }

    public String getDeepestCustomTypeName() {
        return deepestCustomTypeName;
    }
}
