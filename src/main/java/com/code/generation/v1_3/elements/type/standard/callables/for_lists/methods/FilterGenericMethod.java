package com.code.generation.v1_3.elements.type.standard.callables.for_lists.methods;

import com.code.generation.v1_3.elements.strong_type.*;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.callables.Method;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Lambda;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class FilterGenericMethod extends GenericMethod {
    public static final String METHOD_NAME = "filter";

    public FilterGenericMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, false, METHOD_NAME, null, null, Collections.EMPTY_LIST);
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        Typable innerInnerTypeListTypable = innerTypable.getType().setList();
        Typable innerReturnedListTypable = returnedTypable.getType().setList();
        Lambda lambda = typableArguments.get(0).getType().setLambda(1);
        typeInferenceMotor.addFusionOfTypesDeclaration(innerReturnedListTypable, innerInnerTypeListTypable);
        typeInferenceMotor.addFusionOfTypesDeclaration(lambda.getParameter(0), innerInnerTypeListTypable);
        StandardType booleanType = standardTypeDirectory.getStandardType(StandardKnowledges.BOOLEAN_TYPE_NAME);
        fusionIfNonNull(lambda.getReturnedTypable(), booleanType);
    }

    @Override
    public Method makeStrongMethod(NormalType innerNormalType, CanBeReturnedType returned, List<CanBeProvideForParameter> arguments) {
        if(!innerNormalType.isSame(returned)){
            throw new IllegalStateException();
        }
        StrongTypeDirectory strongTypeDirectory = innerNormalType.getStrongTypeDirectory();
        NormalType lambdaParameter = ((ListType) innerNormalType).getInnerType();
        LambdaType lambdaType = new LambdaType(strongTypeDirectory, strongTypeDirectory.getStrongType(Operable.BOOLEAN), Collections.singletonList(lambdaParameter));
        Parameter finalParameter = new Parameter(null, lambdaType);
        return new Method(innerNormalType.getStrongTypeDirectory(), true, innerNormalType, getName(), innerNormalType, Collections.singletonList(finalParameter));
    }
}
