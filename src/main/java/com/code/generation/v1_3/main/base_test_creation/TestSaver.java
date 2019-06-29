package com.code.generation.v1_3.main.base_test_creation;

import com.code.generation.tests.exception.WrongArgNumberException;
import com.code.generation.tests.exception.WrongArgsException;
import com.code.generation.tests.exception.WrongPasswordException;
import com.code.generation.tests.run.BaseTestModifier;

import java.io.IOException;

public class TestSaver {
    private static final String PREFIX = "success/callables/lambdas/block_lambda";
    private static final String CODE_NAME = "one_statement_lambda";

    public static void main(String[] args) throws WrongPasswordException, WrongArgsException, WrongArgNumberException, IOException {
        BaseTestModifier.main(new String[]{"add", PREFIX, CODE_NAME});
        System.out.println("test saved");
    }
}
