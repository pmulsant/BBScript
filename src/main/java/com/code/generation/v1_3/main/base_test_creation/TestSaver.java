package com.code.generation.v1_3.main.base_test_creation;

import com.code.generation.tests.exception.WrongArgNumberException;
import com.code.generation.tests.exception.WrongArgsException;
import com.code.generation.tests.exception.WrongPasswordException;
import com.code.generation.tests.run.BaseTestModifier;

import java.io.IOException;

public class TestSaver {
    private static final String PREFIX = "failed/simple_statement/with_method";
    private static final String CODE_NAME = "a_method_with_uninitialized_variable_argument";
    public static void main(String[] args) throws WrongPasswordException, WrongArgsException, WrongArgNumberException, IOException {
        BaseTestModifier.main(new String[]{"add", PREFIX, CODE_NAME});
        System.out.println("test saved");
    }
}
