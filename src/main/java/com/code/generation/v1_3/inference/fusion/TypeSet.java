package com.code.generation.v1_3.inference.fusion;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.custom.Attribute;
import com.code.generation.v1_3.elements.type.custom.callables.ICallable;
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
            if(isAppearInNumberSpecificOperation()){
                standardType.setAppearInNumberSpecificOperation();
            }
            if(isOperable()){
                standardType.setOperable();
            }
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
        Set<Lambda> lambdas = new HashSet<>();
        for (Type type : types) {
            if (type.getLambda() != null) {
                lambdas.add(type.getLambda());
            }
        }
        if (!lambdas.isEmpty()) {
            newType.setLambdaFromFusion(Util.getOneFromSet(lambdas));
            result.addAll(fusionCanReturns(lambdas));
        }
        return result;
    }

    private void fusionAppearance(Type newType) {
        if(isAppearInNumberSpecificOperation()){
            newType.setAppearInNumberSpecificOperation();
        }
        if(isOperable()){
            newType.setOperable();
        }
    }

    private boolean isAppearInNumberSpecificOperation(){
        for (Type type : types) {
            if (type.isAppearInNumberSpecificOperation()) {
                return true;
            }
        }
        return false;
    }

    private boolean isOperable(){
        for (Type type : types) {
            if (type.isOperable()) {
                return true;
            }
        }
        return false;
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
                newType.setAttributeFromFusion((Attribute) Util.getOneFromSet(attributes), isStrong);
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
            Set<Method> methods = new HashSet<>();
            for (Type type : types) {
                Method method = type.getMethodNoCreate(methodName);
                if (method != null) {
                    methods.add(method);
                }
            }
            if (!methods.isEmpty()) {
                newType.setMethodFromFusion(Util.getOneFromSet(methods));
                result.addAll(fusionCanReturns(methods));
            }
        }
        return result;
    }

    private int fusionParamsNumber(Set<? extends ICallable> callables) {
        Set<Integer> paramsNumbers = callables.stream().map(method -> method.getParamsNumber()).collect(Collectors.toSet());
        if (paramsNumbers.size() != 1) {
            throw new WrongParamNumberException("more than one params number possibilities for callable " + callables);
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
            Set<Constructor> constructors = new HashSet<>();
            for (Type type : types) {
                Constructor constructor = type.getConstructorNoCreate(constructorParamsNumber);
                if (constructor != null) {
                    constructors.add(constructor);
                }
            }
            if (!constructors.isEmpty()) {
                newType.setConstructorFromFusion(Util.getOneFromSet(constructors));
                result.addAll(fusionParams(constructors));
            }
        }
        return result;
    }

    private List<FusionDeclaration> fusionCanReturns(Set<? extends CanReturn> canReturns) {
        CanReturn canReturn = Util.getOneFromSet(canReturns);
        List<FusionDeclaration> result = new LinkedList<>();
        for (CanReturn aCanReturn : canReturns) {
            aCanReturn.assertRightParamsNumber(canReturn.getParamsNumber());
        }
        Set<Typable> returnedTypables = canReturns.stream().map(aCanReturn -> aCanReturn.getReturnedTypable()).collect(Collectors.toSet());
        result.add(new FusionDeclaration(typeInferenceMotor, returnedTypables));
        result.addAll(fusionParams(canReturns));
        return result;
    }

    private List<FusionDeclaration> fusionParams(Set<? extends ISimpleCallable> callables) {
        int paramsNumber = fusionParamsNumber(callables);
        List<FusionDeclaration> result = new ArrayList<>(paramsNumber);
        for (int index = 0; index < paramsNumber; index++) {
            int finalIndex = index;
            Set<Typable> paramTypables = callables.stream().map(aCallable -> aCallable.getParameter(finalIndex)).collect(Collectors.toSet());
            result.add(new FusionDeclaration(typeInferenceMotor, paramTypables));
        }
        return result;
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
