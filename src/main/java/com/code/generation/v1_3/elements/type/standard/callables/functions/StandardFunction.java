package com.code.generation.v1_3.elements.type.standard.callables.functions;

import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericCallable;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Function;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.List;

public class StandardFunction extends GenericCallable {
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
}
