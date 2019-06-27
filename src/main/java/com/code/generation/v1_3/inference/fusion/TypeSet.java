package com.code.generation.v1_3.inference.fusion;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.custom.Attribute;
import com.code.generation.v1_3.elements.type.custom.callables.simples.*;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.simple_types.NullStandardType;
import com.code.generation.v1_3.exception.TypeConflictException;
import com.code.generation.v1_3.exception.WrongParamNumberException;
import com.code.generation.v1_3.exception.for_callables.WrongArgumentConventionException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class TypeSet {
    private TypeInferenceMotor typeInferenceMotor;

    private Set<Type> types = new HashSet<>();

    public TypeSet(TypeInferenceMotor typeInferenceMotor, Type type) {
        this.typeInferenceMotor = typeInferenceMotor;
        types.add(type);
    }

    public TypeSet(TypeInferenceMotor typeInferenceMotor, Set<TypeSet> typeSets) {
        this.typeInferenceMotor = typeInferenceMotor;
        for (TypeSet typeSet : typeSets) {
            for (Type type : typeSet.types) {
                types.add(type);
            }
        }
    }

    public List<FusionDeclaration> fusionAndClean() {
        checkTypeSetOfTypesCoherence();
        if (types.size() <= 1) {
            return Collections.emptyList();
        }
        StandardType standardType = getNonNullStandardType();
        if (standardType != null) {
            assertStandardTypeIsRespected(standardType);
            cleanTypeSetWithNewType(standardType);
            return Collections.EMPTY_LIST;
        }
        List<FusionDeclaration> fusionDeclarations = new LinkedList<>();
        cleanTypeSetWithNewType(fusion(fusionDeclarations));
        return fusionDeclarations;
    }

    public void checkTypeSetOfTypesCoherence() {
        for (Type type : types) {
            if (type.getTypeSet() != this) {
                throw new IllegalStateException();
            }
        }
    }

    private StandardType getNonNullStandardType() {
        Set<StandardType> nonNullStandardTypes = new HashSet<>();
        for (Type type : types) {
            if (type instanceof StandardType && !(type instanceof NullStandardType)) {
                nonNullStandardTypes.add((StandardType) type);
            }
        }
        if (nonNullStandardTypes.size() > 1) {
            ArrayList<StandardType> typeConflicts = new ArrayList<>(nonNullStandardTypes);
            throw new TypeConflictException(typeConflicts);
        }
        for (StandardType standardType : nonNullStandardTypes) {
            return standardType;
        }
        return null;
    }

    private void assertStandardTypeIsRespected(StandardType standardType) {
        for (Type type : types) {
            standardType.checkIsRespectedByType(type);
        }
    }

    private Type fusion(List<FusionDeclaration> fusionDeclarations) {
        Type newType = new Type(typeInferenceMotor, this);
        fusionName(newType);
        fusionVoid(newType);

        fusionDeclarations.addAll(fusionLambda(newType));

        fusionAppearance(newType);
        FusionDeclaration aFusionDeclaration = fusionList(newType);
        FusionDeclaration containerTypablesFusionDeclaration = fusionContainerTypables(newType);
        if (containerTypablesFusionDeclaration != null) {
            fusionDeclarations.add(containerTypablesFusionDeclaration);
        }
        if (aFusionDeclaration != null) {
            fusionDeclarations.add(aFusionDeclaration);
        }
        fusionDeclarations.addAll(fusionAttributes(newType));
        fusionDeclarations.addAll(fusionConstructors(newType));
        fusionDeclarations.addAll(fusionMethods(newType));
        return newType;
    }

    private void cleanTypeSetWithNewType(Type newType) {
        ArrayList<Type> oldTypes = new ArrayList<>(this.types);
        for (Type oldType : oldTypes) {
            if (oldType.canBeReplaced()) {
                oldType.replaceBy(newType);
            }
        }
        if (types.size() != 1) {
            throw new IllegalStateException();
        }
    }

    private void fusionName(Type newType) {
        String simpleName = null;
        for (Type type : types) {
            if (type.getSimpleName() != null) {
                if (simpleName != null) {
                    if (!simpleName.equals(type.getSimpleName())) {
                        throw new TypeConflictException(simpleName, type.getSimpleName());
                    }
                    continue;
                }
                simpleName = type.getSimpleName();
            }
        }
        if (simpleName != null) {
            newType.setSimpleName(simpleName);
        }
    }

    private void fusionVoid(Type newType) {
        Boolean aVoid = null;
        for (Type type : types) {
            if (aVoid == null) {
                aVoid = type.isVoid();
                continue;
            }
            if (type.isVoid() != null && !aVoid.equals(type.isVoid())) {
                throw new TypeConflictException(typeInferenceMotor.getStandardTypable(StandardKnowledges.VOID_TYPE_NAME).getType(), type);
            }
        }
        if (aVoid != null) {
            newType.setVoid(aVoid);
        }
    }

    private List<FusionDeclaration> fusionLambda(Type newType) {
        List<FusionDeclaration> result = new LinkedList<>();
        LinkedList<Lambda> lambdas = new LinkedList<>();
        for (Type type : types) {
            if (type.getLambda() != null) {
                lambdas.add(type.getLambda());
            }
        }
        if (!lambdas.isEmpty()) {
            Lambda firstLambda = lambdas.getFirst();
            Lambda newLambda = newType.setLambda(firstLambda.getParamsNumber());
            result.addAll(fusionCanReturns(newLambda, lambdas));
        }
        return result;
    }

    private void fusionAppearance(Type newType) {
        for (Type type : types) {
            if (type.isAppearOnNumberSpecificOperation()) {
                newType.setAppearOnNumberSpecificOperation();
                break;
            }
        }
        for (Type type : types) {
            if (type.isAppearOnNumberOrStringOperation()) {
                newType.setAppearOnNumberOrStringOperation();
                break;
            }
        }
    }

    private FusionDeclaration fusionList(Type newType) {
        boolean isList = false;
        for (Type type : types) {
            if (type.isList()) {
                isList = type.isList();
                break;
            }
        }
        if (isList) {
            newType.setList();
            Set<Typable> innerListTypables = types.stream().map(type -> type.getInnerTypable()).filter(Objects::nonNull).collect(Collectors.toSet());
            innerListTypables.add(newType.getInnerTypable());
            return new FusionDeclaration(typeInferenceMotor, innerListTypables);
        }
        return null;
    }

    private FusionDeclaration fusionContainerTypables(Type newType) {
        Set<Typable> containerTypables = new HashSet<>();
        for (Type type : types) {
            containerTypables.addAll(type.getContainerTypables());
        }
        if (containerTypables.isEmpty()) {
            return null;
        }
        return new FusionDeclaration(typeInferenceMotor, containerTypables);
    }

    private Collection<? extends FusionDeclaration> fusionAttributes(Type newType) {
        List<FusionDeclaration> result = new LinkedList<>();
        Set<String> attributeNames = new HashSet<>();
        for (Type type : types) {
            attributeNames.addAll(type.getAttributes().keySet());
        }
        for (String attributeName : attributeNames) {
            Set<Typable> attributes = new HashSet<>();
            for (Type type : types) {
                Attribute attribute = type.getAttributeNoCreate(attributeName);
                if (attribute != null) {
                    attributes.add(attribute);
                }
            }
            if (!attributes.isEmpty()) {
                boolean isStrong = attributes.stream().anyMatch(attribute -> ((Attribute) attribute).isStrong());
                attributes.add(newType.getAttribute(attributeName, isStrong));
                result.add(new FusionDeclaration(typeInferenceMotor, attributes));
            }
        }
        return result;
    }

    private Collection<? extends FusionDeclaration> fusionMethods(Type newType) {
        List<FusionDeclaration> result = new LinkedList<>();
        Set<String> methodNames = new HashSet<>();
        for (Type type : types) {
            methodNames.addAll(type.getMethods().keySet());
        }
        for (String methodName : methodNames) {
            List<Method> methods = new LinkedList<>();
            for (Type type : types) {
                Method method = type.getMethodNoCreate(methodName);
                if (method != null) {
                    methods.add(method);
                }
            }
            if (!methods.isEmpty()) {
                int paramNumbers = fusionParamsNumber(methodName, methods);
                Method newMethod = newType.getMethod(methodName, paramNumbers);
                result.addAll(fusionParams(newMethod, methods));
                result.addAll(fusionCanReturns(newMethod, methods));
            }
        }
        return result;
    }

    private int fusionParamsNumber(String methodName, List<Method> methods) {
        Set<Integer> paramsNumbers = methods.stream().map(method -> method.getParamsNumber()).collect(Collectors.toSet());
        if (paramsNumbers.size() != 1) {
            throw new WrongParamNumberException("more than one params number possibilities for callable " + methodName);
        }
        return Util.getOneFromSet(paramsNumbers);
    }

    private Collection<? extends FusionDeclaration> fusionConstructors(Type newType) {
        List<FusionDeclaration> result = new LinkedList<>();
        Set<Integer> constructorParamsNumbers = new HashSet<>();
        for (Type type : types) {
            constructorParamsNumbers.addAll(type.getConstructors().keySet());
        }
        for (Integer constructorParamsNumber : constructorParamsNumbers) {
            List<Constructor> constructors = new LinkedList<>();
            for (Type type : types) {
                Constructor constructor = type.getConstructorNoCreate(constructorParamsNumber);
                if (constructor != null) {
                    constructors.add(constructor);
                }
            }
            if (!constructors.isEmpty()) {
                Constructor newConstructor = newType.getConstructor(constructorParamsNumber);
                result.addAll(fusionParams(newConstructor, constructors));
            }
        }
        return result;
    }

    private List<FusionDeclaration> fusionCanReturns(CanReturn newCanReturn, List<? extends CanReturn> canReturns) {
        List<FusionDeclaration> result = new LinkedList<>();
        for (CanReturn canReturn : canReturns) {
            canReturn.assertRightParamsNumber(newCanReturn.getParamsNumber());
        }
        Set<Typable> returnedTypables = canReturns.stream().map(canReturn -> canReturn.getReturnedTypable()).collect(Collectors.toSet());
        returnedTypables.add(newCanReturn.getReturnedTypable());
        result.add(new FusionDeclaration(typeInferenceMotor, returnedTypables));
        result.addAll(fusionParams(newCanReturn, canReturns));
        return result;
    }

    private List<FusionDeclaration> fusionParams(ISimpleCallable callable, List<? extends ISimpleCallable> callables) {
        List<FusionDeclaration> result = new ArrayList<>(callable.getParamsNumber());
        for (int index = 0; index < callable.getParamsNumber(); index++) {
            int finalIndex = index;
            Set<Typable> paramTypables = callables.stream().map(aCallable -> aCallable.getParameter(finalIndex)).collect(Collectors.toSet());
            Parameter newParameter = callable.getParameter(finalIndex);
            paramTypables.add(newParameter);

            setNameForParameter(newParameter, finalIndex, callables);

            result.add(new FusionDeclaration(typeInferenceMotor, paramTypables));
        }
        return result;
    }

    private void setNameForParameter(Parameter newParameter, int parameterIndex, List<? extends ISimpleCallable> callables) {
        Set<String> paramNames = callables.stream().map(aCallable -> aCallable.getParameter(parameterIndex).getName()).filter(Objects::nonNull).collect(Collectors.toSet());
        if (paramNames.size() > 1) {
            throw new WrongArgumentConventionException("more than one name for parameter");
        }
        String paramName = Util.getOneFromSet(paramNames);
        if (paramName != null) {
            newParameter.setName(paramName);
        }
    }

    public void replaceType(Type oldType, Type newType) {
        types.remove(oldType);
        types.add(newType);
    }

    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public String toString() {
        return "typeSet : " + String.join(" , ", types.stream().map(type -> type.toString()).collect(Collectors.toList()));
    }
}
