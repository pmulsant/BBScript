package com.code.generation.v1_3.elements.strong_type.builder;

import com.code.generation.v1_3.elements.strong_type.*;
import com.code.generation.v1_3.elements.strong_type.callables.Callable;
import com.code.generation.v1_3.elements.strong_type.callables.Constructor;
import com.code.generation.v1_3.elements.strong_type.callables.Function;
import com.code.generation.v1_3.elements.strong_type.callables.Method;
import com.code.generation.v1_3.elements.strong_type.custom.CustomType;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Lambda;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.elements.type.standard.callables.functions.StandardFunction;
import com.code.generation.v1_3.elements.type.standard.simple_types.NullStandardType;
import com.code.generation.v1_3.elements.type.standard.simple_types.VoidStandardType;
import com.code.generation.v1_3.exception.ConstructorCoherenceException;
import com.code.generation.v1_3.exception.MissingCallableDefinitionException;
import com.code.generation.v1_3.exception.WrongTypeFormatException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.generated.GrammarParser;

import java.util.*;
import java.util.stream.Collectors;

public class StrongTypeDirectory {
    private StandardTypeDirectory standardTypeDirectory;

    private VoidType voidType;

    private Map<String, CustomType> customTypes = new HashMap<>();
    private Map<String, Map<Integer, ListType>> listTypes = new HashMap<>();
    private Map<Type, StrongType> typeStrongTypeMap = new IdentityHashMap<>();
    private Map<GrammarParser.ExprContext, StrongType> exprContextStrongTypeMap = new IdentityHashMap<>();
    private Map<String, Function> functionMap = new HashMap<>();

    public StrongTypeDirectory(TypeInferenceMotor typeInferenceMotor) {
        standardTypeDirectory = typeInferenceMotor.getStandardTypeDirectory();
        voidType = new VoidType(this);

        for (Typable typable : typeInferenceMotor.getTypables()) {
            if(typable instanceof TypeInferenceMotor.TypableExpression){
                GrammarParser.ExprContext exprContext = ((TypeInferenceMotor.TypableExpression) typable).getExprContext();
                exprContextStrongTypeMap.computeIfAbsent(exprContext, key -> getStrongType(typable.getType()));
            }
            Type type = typable.getType();
            typeStrongTypeMap.computeIfAbsent(type, aType -> {
                if(aType instanceof NullStandardType){
                    return null;
                }
                return getStrongType(type);
            });
        }
        for (StrongType strongType : typeStrongTypeMap.values()) {
            strongType.build();
        }
        for (com.code.generation.v1_3.elements.type.custom.callables.simples.Function function : typeInferenceMotor.getFunctions().values()) {
            functionMap.put(function.getName(), buildFunction(function));
        }
    }

    public VoidType getVoidStrongType() {
        return voidType;
    }

    public CustomType getStrongTypeRegex() {
        return (CustomType) getStrongType(0, StandardKnowledges.REGEX_TYPE_NAME);
    }

    public CustomType getStrongType(Operable operable) {
        return (CustomType) getStrongType(0, operable.getName());
    }

    public StrongType getStrongType(GrammarParser.ExprContext exprContext){
        return exprContextStrongTypeMap.get(exprContext);
    }

    public NormalType getStrongType(GrammarParser.TypeContext constructorType) {
        CanBeReturnedType strongType = getStrongType(constructorType.POW().size(), constructorType.ID().getText());
        if (!(strongType instanceof NormalType)) {
            throw new ConstructorCoherenceException(strongType);
        }
        return (NormalType) strongType;
    }

    private CanBeReturnedType getStrongType(int powNumber, String simpleTypeName) {
        if (powNumber == 0) {
            if (simpleTypeName.equals("void")) {
                return voidType;
            }
            return customTypes.get(simpleTypeName);
        }
        return listTypes.get(simpleTypeName).get(powNumber);
    }

    public StrongType getStrongType(Type type) {
        StrongType strongType = typeStrongTypeMap.get(type);
        if (strongType != null) {
            return strongType;
        }
        if (type instanceof VoidStandardType) {
            return voidType;
        }
        if(type instanceof NullStandardType){
            return NullType.INSTANCE;
        }
        if (type.isList()) {
            return getListType(type);
        }
        if (type.isLambda()) {
            return getLambdaType(type);
        }
        return getCustomType(type);
    }

    private ListType getListType(Type type) {
        StrongType innerType = getStrongType(type.getInnerTypable().getType());
        if (!(innerType instanceof NormalType)) {
            throw new WrongTypeFormatException(innerType, "list can't contain a non normal type");
        }
        if (innerType instanceof ListType) {
            ListType innerListType = (ListType) innerType;
            Map<Integer, ListType> allListTypes = listTypes.computeIfAbsent(innerListType.getDeepestCustomTypeName(), key -> new HashMap<>());
            return allListTypes.computeIfAbsent(innerListType.getPower() + 1, key -> new ListType(this, type, innerListType));
        }
        CustomType innerCustomType = (CustomType) innerType;
        Map<Integer, ListType> allListTypes = listTypes.computeIfAbsent(innerCustomType.getName(), key -> new HashMap<>());
        return allListTypes.computeIfAbsent(1, key -> new ListType(this, type, innerCustomType));
    }

    private LambdaType getLambdaType(Type type) {
        Lambda lambda = type.getLambda();
        StrongType returnedType = getStrongType(lambda.getReturnedTypable().getType());
        if (!(returnedType instanceof CanBeReturnedType)) {
            throw new WrongTypeFormatException(returnedType, "return type of lambda can't be lambda");
        }
        List<NormalType> parameterStrongTypes = new ArrayList<>(lambda.getParamsNumber());
        for (int index = 0; index < lambda.getParamsNumber(); index++) {
            StrongType aStrongType = getStrongType(lambda.getParameter(index).getType());
            if (!(aStrongType instanceof NormalType)) {
                throw new WrongTypeFormatException(aStrongType, "parameter of lambda can't be void");
            }
            parameterStrongTypes.add((NormalType) aStrongType);
        }
        return new LambdaType(this, (CanBeReturnedType) returnedType, parameterStrongTypes);
    }

    private CustomType buildCustomType(Type type) {
        return new CustomType(this, type);
    }

    private CustomType getCustomType(Type type) {
        CustomType customType = customTypes.get(type.getSimpleName());
        if (customType == null) {
            customType = buildCustomType(type);
            customTypes.put(customType.getName(), customType);
        }
        return customType;
    }

    private Function buildFunction(com.code.generation.v1_3.elements.type.custom.callables.simples.Function function) {
        List<Parameter> parameters = new ArrayList<>(function.getParamsNumber());
        for (int index = 0; index < function.getParamsNumber(); index++) {
            StrongType parameterStrongType = getStrongType(function.getParameter(index).getType());
            if (!(parameterStrongType instanceof CanBeParameterType)) {
                throw new WrongTypeFormatException(parameterStrongType, "parameter can't be void");
            }
            parameters.add(new Parameter(function.getParameter(index).getName(), (CanBeParameterType) parameterStrongType));
        }
        StrongType returnedStrongType = getStrongType(function.getReturnedTypable().getType());
        if (!(returnedStrongType instanceof CanBeReturnedType)) {
            throw new WrongTypeFormatException(returnedStrongType, "returned type can't be lambda");
        }
        return new Function(this, function.getName(), (CanBeReturnedType) returnedStrongType, parameters);
    }

    public Function getFunction(String functionName) {
        return functionMap.get(functionName);
    }

    public NullType getNullType() {
        return NullType.INSTANCE;
    }

    public Map<String, CustomType> getCustomTypes() {
        return customTypes;
    }

    public void check(){
        for (Function function : functionMap.values()) {
            assertGotCallableDefinition(function);
        }
        for (CustomType customType : customTypes.values()) {
            for (Constructor constructor : customType.getConstructors().values()) {
                assertGotCallableDefinition(constructor);
            }
            for (Method method : customType.getMethods().values()) {
                if(!method.isStandard()) {
                    assertGotCallableDefinition(method);
                }
            }
        }
    }

    private void assertGotCallableDefinition(Callable callable){
        if(callable.getCallableDefinition() == null){
            throw new MissingCallableDefinitionException(callable);
        }
    }

    public GenericMethod getGenericMethod(String methodName) {
        return standardTypeDirectory.getMethod(methodName);
    }

    public GenericConstructor getGenericConstructor(GrammarParser.TypeContext typeContext, int paramsNumber) {
        Map<Integer, GenericConstructor> constructorMap = standardTypeDirectory.getConstructorMap(typeContext);
        if(constructorMap == null){
            return null;
        }
        GenericConstructor genericConstructor = constructorMap.get(paramsNumber);
        if(genericConstructor == null){
            throw new IllegalStateException();
        }
        return genericConstructor;
    }

    public StandardFunction getStandardFunction(String functionName) {
        return standardTypeDirectory.getStandardFunction(functionName);
    }

    public List<Function> getNonStandardFunctions() {
        return new ArrayList<>(functionMap.values());
    }
}
