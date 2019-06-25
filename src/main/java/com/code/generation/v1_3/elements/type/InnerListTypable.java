package com.code.generation.v1_3.elements.type;

import com.code.generation.v1_3.inference.TypeInferenceMotor;

public class InnerListTypable extends Typable {
    public InnerListTypable(TypeInferenceMotor typeInferenceMotor, Type containerType) {
        super(typeInferenceMotor);
        containerType.addContainerTypable(this);
        initialize();
    }
}
