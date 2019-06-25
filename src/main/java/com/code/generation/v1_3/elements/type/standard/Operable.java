package com.code.generation.v1_3.elements.type.standard;

public enum Operable {
    INT(StandardKnowledges.INT_TYPE_NAME),
    FLOAT(StandardKnowledges.FLOAT_TYPE_NAME),
    CHAR(StandardKnowledges.CHAR_TYPE_NAME),
    BOOLEAN(StandardKnowledges.BOOLEAN_TYPE_NAME),
    STRING(StandardKnowledges.STRING_TYPE_NAME);


    Operable(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public boolean isNumber() {
        return this.equals(INT) || this.equals(FLOAT);
    }

    public static Operable getFromName(String simpleName) {
        for (Operable operable : values()) {
            if (operable.getName().equals(simpleName)) {
                return operable;
            }
        }
        return null;
    }
}
