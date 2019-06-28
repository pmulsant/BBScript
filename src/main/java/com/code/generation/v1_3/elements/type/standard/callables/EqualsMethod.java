package com.code.generation.v1_3.elements.type.standard.callables;

import com.code.generation.v1_3.elements.strong_type.CanBeParameterType;
import com.code.generation.v1_3.elements.strong_type.CanBeProvideForParameter;
import com.code.generation.v1_3.elements.strong_type.CanBeReturnedType;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.callables.Method;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.exception.TypeConflictException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.Collections;
import java.util.List;

public class EqualsMethod extends GenericMethod {
    private static final String METHOD_NAME = "equals";

    public EqualsMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor) {
        super(standardTypeDirectory, typeInferenceMotor, true, METHOD_NAME, null, standardTypeDirectory.getStandardType(StandardKnowledges.BOOLEAN_TYPE_NAME), Collections.singletonList(null));
    }

    @Override
    protected void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        typeInferenceMotor.addFusionOfTypesDeclaration(innerTypable, typableArguments.get(0));
    }

    @Override
    public Method makeStrongMethod(NormalType innerNormalType, CanBeReturnedType returned, List<CanBeProvideForParameter> arguments) {
        StrongTypeDirectory strongTypeDirectory = innerNormalType.getStrongTypeDirectory();
        CustomType booleanType = strongTypeDirectory.getStrongType(Operable.BOOLEAN);
        if(arguments.size() != 1){
            throw new IllegalStateException();
        }

        CanBeProvideForParameter canBeProvideForParameter = arguments.get(0);
        if(!innerNormalType.isSame(canBeProvideForParameter)){
            throw new TypeConflictException(innerNormalType.getComplexName(), canBeProvideForParameter.toString());
        }
        Parameter parameter = new Parameter(null, (CanBeParameterType) canBeProvideForParameter);
        return new Method(strongTypeDirectory, true, innerNormalType, getName(), booleanType, Collections.singletonList(parameter));
    }
}
