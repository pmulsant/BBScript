package com.code.generation.v1_3.elements.type.standard;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Constructor;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Method;
import com.code.generation.v1_3.exception.UnConformedStandardTypeException;
import com.code.generation.v1_3.exception.for_callables.NotStandardCallableForThisTypeException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;

import java.util.HashMap;
import java.util.Map;

public abstract class StandardType extends Type {
    protected StandardTypeDirectory standardTypeDirectory;
    private StandardTypable standardTypable;

    private Map<Integer, GenericConstructor> genericConstructorMap = new HashMap<>();
    private Map<String, GenericMethod> genericMethodMap = new HashMap<>();

    public StandardType(StandardTypeDirectory standardTypeDirectory, TypeInferenceMotor typeInferenceMotor, String name) {
        super(typeInferenceMotor);
        this.standardTypeDirectory = standardTypeDirectory;
        isVoid = false;
        simpleName = name;
        this.standardTypable = new StandardTypable(typeInferenceMotor, this);
    }

    public void build() {
    }

    public void addConstructor(GenericConstructor constructor) {
        genericConstructorMap.put(constructor.getParamsNumber(), constructor);
        standardTypeDirectory.register(constructor);
    }

    public void addMethod(GenericMethod method) {
        genericMethodMap.put(method.getName(), method);
        standardTypeDirectory.register(method);
    }

    public Map<Integer, GenericConstructor> getGenericConstructorMap() {
        return genericConstructorMap;
    }

    public Map<String, GenericMethod> getGenericMethodMap() {
        return genericMethodMap;
    }

    @Override
    public boolean canBeReplaced(){
        return false;
    }

    public Typable getOriginalTypable() {
        return standardTypable;
    }

    @Override
    public void checkDeducedAndCoherence() {
        checkAppearanceCoherenceWithSimpleName();
    }

    public void checkIsRespectedByTypeAndReplace(Type type){
        checkIsRespectedByType(type);
        type.replaceBy(this);
    }

    public void checkIsRespectedByType(Type type) {
        if (type == this) {
            return;
        }
        if (type.isList()) {
            throw new UnConformedStandardTypeException(this, type);
        }
        if ((type.isVoid() != null) && (type.isVoid() != isVoid)) {
            throw new UnConformedStandardTypeException(this, type);
        }
        if (type.getSimpleName() != null && !type.getSimpleName().equals(simpleName)) {
            throw new UnConformedStandardTypeException(this, type);
        }
        if (!type.getAttributes().isEmpty()) {
            throw new UnConformedStandardTypeException(this, type);
        }
        for (Constructor constructor : type.getConstructors().values()) {
            GenericConstructor standardConstructor = genericConstructorMap.get(constructor.getParamsNumber());
            if(standardConstructor == null){
                throw new NotStandardCallableForThisTypeException(this, constructor);
            }
            standardConstructor.checkIsRespectedByConstructor(constructor);
        }
        for (Method method : type.getMethods().values()) {
            GenericMethod standardMethod = genericMethodMap.get(method.getName());
            if(standardMethod == null){
                standardMethod = standardTypeDirectory.getObjectMethod(method.getName());
            }
            if (standardMethod == null) {
                throw new NotStandardCallableForThisTypeException(this, method);
            }
            standardMethod.checkIsRespectedByMethod(method);
        }
    }
}
