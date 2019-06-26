package com.code.generation.tests.run;

import com.code.generation.tests.exception.WrongArgNumberException;
import com.code.generation.tests.exception.WrongArgsException;
import com.code.generation.tests.exception.WrongPasswordException;

import java.io.IOException;

public class BaseTestModifierCaller {
    public static void main(String[] args) throws WrongPasswordException, WrongArgsException, WrongArgNumberException, IOException {
        //BaseTestModifier.main(new String[]{"all"});
        //BaseTestModifier.main(new String[]{"rebuild", "failed"});
        /*BaseTestModifier.main(new String[]{
                "rebuild",

                "failed/operating_expressions_wrong_types",
                "or_with_float"});*/
        BaseTestModifier.main(new String[]{"replace",

                "success/simple_statement/variable_errors/typed_variables",
                "regex_initialization_with_int",

                "failed/simple_statement/variable_errors/typed_variables",
                "regex_initialization_with_int"});
    }
}
