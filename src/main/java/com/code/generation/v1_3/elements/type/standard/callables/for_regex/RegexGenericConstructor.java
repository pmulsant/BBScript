package com.code.generation.v1_3.elements.type.standard.callables.for_regex;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class RegexGenericConstructor extends GenericConstructor {
    public RegexGenericConstructor(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, standardTypeDirectory.getStandardType(StandardKnowledges.REGEX_TYPE_NAME), Collections.singletonList(standardTypeDirectory.getStandardType(StandardKnowledges.REGEX_TYPE_NAME)));
    }

    @Override
    protected void processLinksSpecial(Typable topTypable, List<? extends Typable> typableArguments) {
    }
}
