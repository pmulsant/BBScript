package com.code.generation.v1_3.util;

import java.util.Set;

public class Util {
    public static <T> T getOneFromSet(Set<T> set){
        for (T obj : set) {
            return obj;
        }
        return null;
    }
}
