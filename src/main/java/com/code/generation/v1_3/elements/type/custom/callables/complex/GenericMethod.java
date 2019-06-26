package com.code.generation.v1_3.elements.type.custom.callables.complex;

import com.code.generation.v1_3.elements.strong_type.CanBeProvideForParameter;
import com.code.generation.v1_3.elements.strong_type.CanBeReturnedType;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.IMethod;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Method;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.List;

public abstract class GenericMethod extends GenericCallable implements IMethod {
    protected StandardType standardInnerType;
    protected StandardType standardReturnedType;
    private String name;

    public GenericMethod(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor, String name, StandardType standardInnerType, StandardType standardReturnedType, List<StandardType> standardTypeParameters) {
        super(standardTypeDirectory, typeInferenceMotor, standardTypeParameters);
        this.standardInnerType = standardInnerType;
        this.standardReturnedType = standardReturnedType;
        this.name = name;
    }

    @Override
    protected void register() {
        standardTypeDirectory.register(this);
    }

    public final void processLinks(Typable innerTypable, Method method) {
        processLinks(innerTypable, method.getReturnedTypable(), method.getParameters());
    }

    private final void processLinks(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments) {
        boolean allDeduced = processLinkStandardTypeParameters(typableArguments);
        if (!fusionIfNonNull(innerTypable, standardInnerType)) {
            allDeduced = false;
        }
        if (!fusionIfNonNull(returnedTypable, standardReturnedType)) {
            allDeduced = false;
        }
        if (allDeduced) {
            return;
        }
        processLinksSpecial(innerTypable, returnedTypable, typableArguments);
    }

    protected abstract void processLinksSpecial(Typable innerTypable, Typable returnedTypable, List<? extends Typable> typableArguments);

    public String getName() {
        return name;
    }

    public void checkIsRespectedByMethod(Method method) {
        method.assertRightParamsNumber(getParamsNumber());
    }

    public abstract com.code.generation.v1_3.elements.strong_type.callables.Method makeStrongMethod(NormalType innerNormalType, CanBeReturnedType returned, List<CanBeProvideForParameter> arguments);
}
