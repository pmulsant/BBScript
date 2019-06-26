package com.code.generation.v1_3.elements.type.standard.callables;

import com.code.generation.v1_3.elements.strong_type.CanBeProvideForParameter;
import com.code.generation.v1_3.elements.strong_type.CanBeReturnedType;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.strong_type.StrongType;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.callables.Method;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class StandardMethod extends GenericMethod {
    protected StandardTypeDirectory standardTypeDirectory;

    public StandardMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor, String name, StandardType standardInnerType, StandardType standardReturnedType, List<StandardType> standardTypeParameters) {
        super(standardTypeDirectory, typeInferenceMotor, name, standardInnerType, standardReturnedType, standardTypeParameters);
    }

    @Override
    protected final void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
    }

    @Override
    public Method makeStrongMethod(NormalType innerNormalType, CanBeReturnedType returned, List<CanBeProvideForParameter> arguments) {
        StrongTypeDirectory strongTypeDirectory = innerNormalType.getStrongTypeDirectory();
        CustomType innerType = (CustomType) strongTypeDirectory.getStrongType(standardInnerType);
        if(!innerNormalType.isSame(innerType)){
            throw new IllegalStateException();
        }
        CustomType returnedType = (CustomType) strongTypeDirectory.getStrongType(standardReturnedType);
        if(!returnedType.isSame(returned)){
            throw new IllegalStateException();
        }
        if(arguments.size() != standardTypeParameters.size()){
            throw new IllegalStateException();
        }
        List<Parameter> parameters = new ArrayList<>(arguments.size());
        for (int index = 0; index < arguments.size(); index++) {
            CanBeProvideForParameter canBeProvideForParameter = arguments.get(index);
            CustomType standardParameterType = (CustomType) strongTypeDirectory.getStrongType(standardTypeParameters.get(index));
            if(!standardParameterType.isSame(canBeProvideForParameter)){
                throw new IllegalStateException();
            }
            parameters.add(new Parameter(null, standardParameterType));
        }
        return new Method(innerNormalType.getStrongTypeDirectory(), innerNormalType, getName(), returnedType, parameters);
    }
}
