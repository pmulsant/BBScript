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

public class GetByIndexGenericMethod extends GenericMethod {
    public static final String METHOD_NAME = "get";

    public GetByIndexGenericMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, METHOD_NAME, null, null, Collections.singletonList(standardTypeDirectory.getStandardType(StandardKnowledges.INT_TYPE_NAME)));
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        Typable innerListTypable = innerTypable.getType().setList();
        typeInferenceMotor.addFusionOfTypesDeclaration(returnedTypable, innerListTypable);
    }

    @Override
    public Method makeStrongMethod(NormalType innerNormalType, CanBeReturnedType returned, List<CanBeProvideForParameter> arguments) {
        StrongTypeDirectory strongTypeDirectory = innerNormalType.getStrongTypeDirectory();
        CustomType intType = strongTypeDirectory.getStrongType(Operable.INT);
        Parameter parameter = new Parameter(null, intType);
        NormalType innerListType = ((ListType) innerNormalType).getInnerType();
        return new Method(innerNormalType.getStrongTypeDirectory(), innerNormalType, getName(), innerListType, Collections.singletonList(parameter));
    }
}
