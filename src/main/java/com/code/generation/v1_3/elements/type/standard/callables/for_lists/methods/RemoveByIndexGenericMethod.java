package com.code.generation.v1_3.elements.type.standard.callables.for_lists.methods;

import com.code.generation.v1_3.elements.strong_type.*;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.callables.Method;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class RemoveByIndexGenericMethod extends GenericMethod {
    public static final String METHOD_NAME = "removeByIndex";

    public RemoveByIndexGenericMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, false, METHOD_NAME, null, standardTypeDirectory.getStandardType(StandardKnowledges.VOID_TYPE_NAME), Collections.singletonList(standardTypeDirectory.getStandardType(StandardKnowledges.INT_TYPE_NAME)));
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        innerTypable.getType().setList();
    }

    @Override
    public Method makeStrongMethod(NormalType innerNormalType, CanBeReturnedType returned, List<CanBeProvideForParameter> arguments) {
        StrongTypeDirectory strongTypeDirectory = innerNormalType.getStrongTypeDirectory();
        CustomType intType = strongTypeDirectory.getStrongType(Operable.INT);
        Parameter parameter = new Parameter(null, intType);
        return new Method(innerNormalType.getStrongTypeDirectory(), true, innerNormalType, getName(), strongTypeDirectory.getVoidStrongType(), Collections.singletonList(parameter));
    }
}
