package com.code.generation.v1_3.elements.type.standard;

import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.standard.callables.for_lists.constructors.CopyListGenericConstructor;
import com.code.generation.v1_3.elements.type.standard.callables.for_lists.constructors.EmptyListGenericConstructor;
import com.code.generation.v1_3.elements.type.standard.callables.for_lists.methods.*;
import com.code.generation.v1_3.elements.type.standard.callables.functions.StandardFunction;
import com.code.generation.v1_3.elements.type.standard.simple_types.*;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.generated.GrammarParser;

import java.util.HashMap;
import java.util.Map;

public class StandardTypeDirectory {
    private TypeInferenceMotor typeInferenceMotor;

    private Map<String, StandardType> standardTypeMap = new HashMap<>();
    private Map<Integer, GenericConstructor> listConstructorMap = new HashMap<>();
    private Map<String, Map<Integer, GenericConstructor>> genericConstructors = new HashMap<>();
    private Map<String, GenericMethod> genericMethods = new HashMap<>();
    private Map<String, StandardFunction> standardFunctions = new HashMap<>();

    public StandardTypeDirectory(TypeInferenceMotor typeInferenceMotor) {
        this.typeInferenceMotor = typeInferenceMotor;
        addStandardTypes();
        addListElements();
        buildSimpleStandardTypes();
    }

    private void addStandardTypes() {
        addStandardType(new VoidStandardType(this, typeInferenceMotor));
        addStandardType(new NullStandardType(this, typeInferenceMotor));

        addStandardType(new IntStandardType(this, typeInferenceMotor));
        addStandardType(new FloatStandardType(this, typeInferenceMotor));
        addStandardType(new CharStandardType(this, typeInferenceMotor));
        addStandardType(new BooleanStandardType(this, typeInferenceMotor));
        addStandardType(new StringStandardType(this, typeInferenceMotor));
        addStandardType(new RegexStandardType(this, typeInferenceMotor));
    }

    private void addListElements() {
        register(new CopyListGenericConstructor(this, typeInferenceMotor));
        register(new EmptyListGenericConstructor(this, typeInferenceMotor));

        register(new SizeGenericMethod(this, typeInferenceMotor));
        register(new AddGenericMethod(this, typeInferenceMotor));
        register(new GetByIndexGenericMethod(this, typeInferenceMotor));
        register(new RemoveByIndexGenericMethod(this, typeInferenceMotor));

        register(new MapGenericMethod(this, typeInferenceMotor));
        register(new FilterGenericMethod(this, typeInferenceMotor));
    }

    private void buildSimpleStandardTypes() {
        for (StandardType standardType : standardTypeMap.values()) {
            standardType.build();
        }
    }

    private void addStandardType(StandardType standardType) {
        standardTypeMap.put(standardType.getSimpleName(), standardType);
    }

    public StandardType getStandardType(String typeName) {
        return standardTypeMap.get(typeName);
    }

    public GenericMethod getMethod(String methodName) {
        return genericMethods.get(methodName);
    }

    public Map<Integer, GenericConstructor> getConstructorMap(GrammarParser.TypeContext typeContext) {
        if (typeContext.POW().size() != 0) {
            return listConstructorMap;
        }
        return genericConstructors.get(typeContext.ID().getText());
    }

    public void register(GenericConstructor genericConstructor) {
        if (genericConstructor.getInnerStandardType() == null) {
            listConstructorMap.put(genericConstructor.getParamsNumber(), genericConstructor);
            return;
        }
        String standardTypeName = genericConstructor.getInnerStandardType().getSimpleName();
        genericConstructors.computeIfAbsent(standardTypeName, key -> new HashMap<>()).put(genericConstructor.getParamsNumber(), genericConstructor);
    }

    public void register(StandardFunction standardFunction) {
        standardFunctions.put(standardFunction.getName(), standardFunction);
    }

    public void register(GenericMethod genericMethod) {
        genericMethods.put(genericMethod.getName(), genericMethod);
    }

    public StandardFunction getStandardFunction(String functionName) {
        return standardFunctions.get(functionName);
    }
}
