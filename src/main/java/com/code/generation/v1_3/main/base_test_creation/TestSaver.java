package com.code.generation.v1_3.main.base_test_creation;

import com.code.generation.tests.exception.WrongArgNumberException;
import com.code.generation.tests.exception.WrongArgsException;
import com.code.generation.tests.exception.WrongPasswordException;
import com.code.generation.tests.run.BaseTestModifier;

import java.io.IOException;

public class TestSaver {
    private static final String PREFIX = "failed/simple_statement/this_use";
    private static final String CODE_NAME = "cant_use_this_outside_object_even_in_constructor_parameter";

    public static void main(String[] args) throws WrongPasswordException, WrongArgsException, WrongArgNumberException, IOException {
        BaseTestModifier.main(new String[]{"add", PREFIX, CODE_NAME});
        System.out.println("test saved");
    }
}
