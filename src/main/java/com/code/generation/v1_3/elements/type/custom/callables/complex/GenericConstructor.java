package com.code.generation.v1_3.elements.type.custom.callables.complex;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.custom.callables.IConstructor;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Constructor;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.List;

public abstract class GenericConstructor extends GenericCallable implements IConstructor {
    private StandardType innerStandardType;

    public GenericConstructor(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor, StandardType innerStandardType, List<StandardType> standardTypeParameters) {
        super(standardTypeDirectory, typeInferenceMotor, standardTypeParameters);
        this.innerStandardType = innerStandardType;
    }

    @Override
    protected void register() {
        standardTypeDirectory.register(this);
    }

    public final void processLink(Typable topTypable, Constructor constructor) {
        if (!fusionIfNonNull(topTypable, innerStandardType)) {
            topTypable.getType().setList();
        }
        if (!processLinkStandardTypeParameters(constructor.getParameters())) {
            processLinksSpecial(topTypable, constructor.getParameters());
        }
    }

    protected abstract void processLinksSpecial(Typable topTypable, List<? extends Typable> typableArguments);

    public StandardType getInnerStandardType() {
        return innerStandardType;
    }

    public void checkIsRespectedByConstructor(Constructor constructor) {
        // type have already be liked
        constructor.assertRightParamsNumber(getParamsNumber());
    }
}
