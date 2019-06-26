package com.code.generation.v1_3.elements.type.standard.callables.functions;

import com.code.generation.v1_3.elements.strong_type.CanBeProvideForParameter;
import com.code.generation.v1_3.elements.strong_type.CanBeReturnedType;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericCallable;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Function;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.List;
import java.util.stream.Collectors;

public abstract class StandardFunction extends GenericCallable {
    private String name;
    private StandardType standardReturnedType;

    public StandardFunction(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor, String name, StandardType standardReturnedType, List<StandardType> standardTypeParameters) {
        super(standardTypeDirectory, typeInferenceMotor, standardTypeParameters);
        this.name = name;
        this.standardReturnedType = standardReturnedType;
    }

    @Override
    protected void register() {
        standardTypeDirectory.register(this);
    }

    public String getName() {
        return name;
    }

    public void processLinks(Function function) {
        fusionIfNonNull(function.getReturnedTypable(), standardReturnedType);
        processLinkStandardTypeParameters(function.getParameters());
    }

    public com.code.generation.v1_3.elements.strong_type.callables.Function makeStrongFunction(CanBeReturnedType returned, List<CanBeProvideForParameter> arguments){
        StrongTypeDirectory strongTypeDirectory = returned.getStrongTypeDirectory();
        CustomType returnedType = (CustomType) strongTypeDirectory.getStrongType(standardReturnedType);
        List<Parameter> parameters = standardTypeParameters.stream().map(standardType -> new Parameter(null,
                (CustomType) strongTypeDirectory.getStrongType(standardType))).collect(Collectors.toList());
        return new com.code.generation.v1_3.elements.strong_type.callables.Function(strongTypeDirectory, name, returnedType, parameters);
    }
}
