package com.code.generation.v1_3.elements.strong_type;

import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.strong_type.callables.Constructor;
import com.code.generation.v1_3.elements.strong_type.callables.Method;
import com.code.generation.v1_3.elements.strong_type.custom.Attribute;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.exception.WrongTypeException;
import com.code.generation.v1_3.exception.WrongTypeFormatException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class NormalType implements CanBeReturnedType, CanBeParameterType {
    private StrongTypeDirectory strongTypeDirectory;
    protected Type initialType;

    private Map<String, Attribute> attributes = new HashMap<>();
    private Map<Integer, Constructor> constructors = new HashMap<>();
    private Map<String, Method> methods = new HashMap<>();

    public NormalType(StrongTypeDirectory strongTypeDirectory, Type initialType) {
        this.strongTypeDirectory = strongTypeDirectory;
        this.initialType = initialType;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public Map<Integer, Constructor> getConstructors() {
        return constructors;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public void assertIsSame(StrongType normalType) {
        if (!isSame(normalType)) {
            throw new WrongTypeException(this, normalType);
        }
    }

    public abstract boolean isSame(StrongType strongType);

    @Override
    public void checkIsCompatibleWithArgument(CanBeProvideForParameter canBeProvideForParameter) {
        if (canBeProvideForParameter.isNull()) {
            return;
        }
        assertIsSame(canBeProvideForParameter);
    }

    @Override
    public void assertIsCompatibleWithReturn(CanAppearInReturnStat canAppearInReturnStat) {
        if(canAppearInReturnStat.isNull()){
            return;
        }
        CanBeReturnedType canBeReturnedType = (CanBeReturnedType) canAppearInReturnStat;
        if (canBeReturnedType instanceof VoidType) {
            throw new WrongTypeFormatException(canBeReturnedType, "not compatible with return");
        }
        NormalType normalType = (NormalType) canBeReturnedType;
        if(!isSame(normalType)){
            throw new WrongTypeFormatException(normalType, "not compatible with return");
        }
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public void build() {
        buildAttributes();
        buildConstructors();
        buildMethods();
    }

    public void buildAttributes() {
        for (com.code.generation.v1_3.elements.type.custom.Attribute attribute : initialType.getAttributes().values()) {
            StrongType strongType = strongTypeDirectory.getStrongType(attribute.getType());
            if (!(strongType instanceof NormalType)) {
                throw new WrongTypeFormatException(strongType, "attribute must be a normal type");
            }
            addAttribute(new Attribute(attribute.getName(), (NormalType) strongType, attribute.isStrong()));
        }
    }

    public void buildConstructors() {
        for (com.code.generation.v1_3.elements.type.custom.callables.simples.Constructor constructor : initialType.getConstructors().values()) {
            addConstructor(new Constructor(this, getStrongParameterTypes(constructor)));
        }
    }

    public void buildMethods() {
        for (com.code.generation.v1_3.elements.type.custom.callables.simples.Method method : initialType.getMethods().values()) {
            StrongType returnedType = strongTypeDirectory.getStrongType(method.getReturnedTypable().getType());
            if (!(returnedType instanceof CanBeReturnedType)) {
                throw new WrongTypeFormatException(returnedType, "can't be returned type");
            }
            addMethod(new Method(this, method.getName(), (CanBeReturnedType) returnedType, getStrongParameterTypes(method)));
        }
    }

    private List<Parameter> getStrongParameterTypes(com.code.generation.v1_3.elements.type.custom.callables.simples.ISimpleCallable callable) {
        List<Parameter> parameters = new ArrayList<>(callable.getParamsNumber());
        for (int index = 0; index < callable.getParamsNumber(); index++) {
            com.code.generation.v1_3.elements.type.custom.callables.simples.Parameter parameter = callable.getParameter(index);
            StrongType strongType = strongTypeDirectory.getStrongType(parameter.getType());
            if (!(strongType instanceof CanBeParameterType)) {
                throw new WrongTypeFormatException(strongType, "parameter must be a parameter type");
            }
            parameters.add(new Parameter(parameter.getName(), (CanBeParameterType) strongType));
        }
        return parameters;
    }

    public void addAttribute(Attribute attribute) {
        attributes.put(attribute.getName(), attribute);
    }

    public void addConstructor(Constructor constructor) {
        constructors.put(constructor.getParameters().size(), constructor);
    }

    public void addMethod(Method method) {
        methods.put(method.getName(), method);
    }

    @Override
    public String toString() {
        return getComplexName();
    }
}
