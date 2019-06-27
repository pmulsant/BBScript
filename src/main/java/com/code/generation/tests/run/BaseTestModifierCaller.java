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

                "failed/rules_tests/simple_down_top_inference",
                "add_a_string_and_a_regex"});
        /*BaseTestModifier.main(new String[]{"replace",

                "success/simple_statement/list_manipulation/with_function",
                "assign_with_standard_function",

                "success/simple_statement/with_function",
                "assign_with_standard_function"});*/
    }
}
