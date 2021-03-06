package com.code.generation.tests.run;

import com.code.generation.tests.exception.WrongArgNumberException;
import com.code.generation.tests.exception.WrongArgsException;
import com.code.generation.tests.exception.WrongPasswordException;

import java.io.IOException;

public class BaseTestModifierCaller {
    public static void main(String[] args) throws WrongPasswordException, WrongArgsException, WrongArgNumberException, IOException {
        //BaseTestModifier.main(new String[]{"all"});
        //BaseTestModifier.main(new String[]{"rebuild", "failed"});
        BaseTestModifier.main(new String[]{
                "rebuild",

                "failed/callables/functions/standard_functions",
                "parse_float_two_arguments"});
        /*BaseTestModifier.main(new String[]{"remove",

                "success/rules_tests/simple_down_top_inference",
                "mul_two_floats"
        });*/
        /*BaseTestModifier.main(new String[]{"replace",

                "success/null_tests/many_statements",
                "add_a_always_null_variable_with_int",

                "failed/null_tests/many_statements",
                "add_a_always_null_variable_with_int"});*/
    }
}
