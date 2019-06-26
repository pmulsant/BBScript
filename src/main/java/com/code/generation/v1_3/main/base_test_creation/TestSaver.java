package com.code.generation.v1_3.main.base_test_creation;

import com.code.generation.tests.exception.WrongArgNumberException;
import com.code.generation.tests.exception.WrongArgsException;
import com.code.generation.tests.exception.WrongPasswordException;
import com.code.generation.tests.run.BaseTestModifier;

import java.io.IOException;

public class TestSaver {
    private static final String PREFIX = "success/many_statements/list_manipulation";
    private static final String CODE_NAME = "add_a_int_and_a_casted_int_to_list";

    public static void main(String[] args) throws WrongPasswordException, WrongArgsException, WrongArgNumberException, IOException {
        BaseTestModifier.main(new String[]{"add", PREFIX, CODE_NAME});
        System.out.println("test saved");
    }
}
