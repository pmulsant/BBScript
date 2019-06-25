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

                "failed/operating_expressions_wrong_types",
                "add_booleans"});
        /*BaseTestModifier.main(new String[]{"replace",

                "failed/simple_statement/non_assignable_errors",
                "non_assignable_one_init_symbols_variable_at_right",

                "failed/simple_statement/non_assignable_errors",
                "non_assignable_one_init_symbols_variable_non_initialized_at_right"});*/
    }
}
