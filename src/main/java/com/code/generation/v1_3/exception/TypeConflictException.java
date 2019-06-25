package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.standard.StandardType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TypeConflictException extends RuntimeCustomException {
    public TypeConflictException(){
        super("type conflict");
    }

    public TypeConflictException(String name1, String name2){
        super(name1 + " vs " + name2);
    }

    public TypeConflictException(Type type1, Type type2){
        super(type1 + " vs " + type2);
    }

    public TypeConflictException(ArrayList<StandardType> typeConflicts) {
        super(String.join(" vs ", typeConflicts.stream().map(standardType -> standardType.toString()).collect(Collectors.toList())));
    }
}
