package com.code.generation.v1_3.elements.type.standard.callables.for_regex;

import com.code.generation.v1_3.elements.strong_type.CanBeProvideForParameter;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.callables.Constructor;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;
import com.code.generation.v1_3.elements.type.standard.Operable;
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

    @Override
    public Constructor makeStrongConstructor(NormalType topType, List<CanBeProvideForParameter> arguments) {
        StrongTypeDirectory strongTypeDirectory = topType.getStrongTypeDirectory();
        CustomType stringType = strongTypeDirectory.getStrongType(Operable.STRING);
        return new Constructor(topType.getStrongTypeDirectory(), topType, Collections.singletonList(new Parameter(null, stringType)));
    }
}
