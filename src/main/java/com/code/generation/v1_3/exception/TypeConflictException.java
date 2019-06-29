package com.code.generation.v1_3.exception;

import com.code.generation.RuntimeCustomException;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.standard.StandardType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TypeConflictException extends RuntimeCustomException {
    public TypeConflictException(){
        super("type conflict");
    }

    public TypeConflictException(Type type1, Type type2){
        this(Arrays.asList(type1.toString(), type2.toString()), String.CASE_INSENSITIVE_ORDER);
    }

    public TypeConflictException(String name1, String name2){
        this(Arrays.asList(name1, name2), String.CASE_INSENSITIVE_ORDER);
    }

    public TypeConflictException(List<? extends Type> typeConflicts) {
        this(typeConflicts.stream().map(standardType -> standardType.toString()).collect(Collectors.toList()), String.CASE_INSENSITIVE_ORDER);
    }

    public TypeConflictException(List<String> typeStrs, Comparator<String> comparator){
        super(String.join(" vs ", getSortedList(typeStrs, comparator)));
    }

    private static List<String> getSortedList(List<String> strs, Comparator<String> comparator){
        strs.sort(comparator);
        return strs;
    }
}
