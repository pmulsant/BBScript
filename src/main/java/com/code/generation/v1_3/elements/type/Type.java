package com.code.generation.v1_3.elements.type;

import com.code.generation.v1_3.elements.type.custom.Attribute;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Constructor;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Lambda;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Method;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.exception.CantFindTypeException;
import com.code.generation.v1_3.exception.TypeConflictException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.inference.fusion.TypeSet;
import com.generated.GrammarParser;

import java.util.*;

public class Type {
    private boolean isDeducedAndCoherent = false;

    protected TypeInferenceMotor typeInferenceMotor;
    private TypeSet typeSet;
    private List<Typable> typables = new LinkedList<>();
    private List<Typable> containerTypables = new LinkedList<>();

    protected Boolean isVoid;
    private boolean appearOnNumberSpecificOperation;
    private boolean appearOnNumberOrStringOperation;

    protected Typable innerTypable;
    protected Lambda lambda;
    protected String simpleName;
    protected Map<String, Attribute> attributes = new HashMap<>();
    protected Map<Integer, Constructor> constructors = new HashMap<>();
    protected Map<String, Method> methods = new HashMap<>();

    public Type(TypeInferenceMotor typeInferenceMotor, Typable typable) {
        this(typeInferenceMotor);
        addTypable(typable);
    }

    public Type(TypeInferenceMotor typeInferenceMotor) {
        this.typeInferenceMotor = typeInferenceMotor;
        typeSet = new TypeSet(typeInferenceMotor, this);
    }

    public Type(TypeInferenceMotor typeInferenceMotor, TypeSet typeSets) {
        this.typeInferenceMotor = typeInferenceMotor;
        this.typeSet = typeSets;
    }


    public void setName(GrammarParser.TypeContext typeContext) {
        if (typeContext == null) {
            return;
        }
        setName(typeContext.POW().size(), typeContext.ID().getText());
    }

    public void setName(Operable operable) {
        setName(0, operable.getName());
    }

    private void setName(int powNumber, String simpleName) {
        if (powNumber == 0) {
            if (simpleName.equals(StandardKnowledges.VOID_TYPE_NAME)) {
                setVoid(true);
                return;
            }
            setSimpleName(simpleName);
            return;
        }
        setList();
        innerTypable.getType().setName(powNumber - 1, simpleName);
    }

    public void setSimpleName(String simpleName) {
        if (this.simpleName != null && !this.simpleName.equals(simpleName)) {
            throw new TypeConflictException(this.simpleName, simpleName);
        }
        this.simpleName = simpleName;
    }

    public Typable setList() {
        if (innerTypable == null) {
            innerTypable = new InnerListTypable(typeInferenceMotor, this);
        }
        return innerTypable;
    }

    public boolean isList() {
        return innerTypable != null;
    }

    public void setVoid(boolean isVoid) {
        if (this.isVoid != null && this.isVoid != isVoid) {
            throw new TypeConflictException(StandardKnowledges.VOID_TYPE_NAME, "non " + StandardKnowledges.VOID_TYPE_NAME);
        }
        this.isVoid = isVoid;
        if (isVoid) {
            setSimpleName(StandardKnowledges.VOID_TYPE_NAME);
        }
    }

    public Boolean isVoid() {
        return isVoid;
    }

    public boolean isLambda() {
        return lambda != null;
    }

    public Attribute getAttribute(String attributeName, boolean isStrong) {
        Attribute attribute = attributes.get(attributeName);
        if (attribute == null) {
            attribute = new Attribute(typeInferenceMotor, attributeName);
            attributes.put(attributeName, attribute);
        }
        if (isStrong) {
            attribute.setStrong();
        }
        return attribute;
    }

    public Constructor getConstructor(int paramsNumber) {
        Constructor constructor = constructors.get(paramsNumber);
        if (constructor == null) {
            constructor = new Constructor(typeInferenceMotor, paramsNumber);
            constructors.put(paramsNumber, constructor);
        }
        return constructor;
    }

    public Method getMethod(String methodName, int paramsNumber) {
        Method method = methods.get(methodName);
        if (method == null) {
            method = new Method(typeInferenceMotor, this, methodName, paramsNumber);
            methods.put(methodName, method);
        }
        method.assertRightParamsNumber(paramsNumber);
        return method;
    }

    public void setAppearOnNumberSpecificOperation() {
        this.appearOnNumberSpecificOperation = true;
    }

    public boolean isAppearOnNumberSpecificOperation() {
        return appearOnNumberSpecificOperation;
    }

    public void setAppearOnNumberOrStringOperation() {
        this.appearOnNumberOrStringOperation = true;
    }

    public boolean isAppearOnNumberOrStringOperation() {
        return appearOnNumberOrStringOperation;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public Typable getInnerTypable() {
        return innerTypable;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public Map<Integer, Constructor> getConstructors() {
        return constructors;
    }

    public TypeSet getTypeSet() {
        return typeSet;
    }

    public void setTypeSet(TypeSet newTypeSet) {
        this.typeSet = newTypeSet;
    }

    public boolean canBeReplaced() {
        return true;
    }

    public void replaceBy(Type newType) {
        for (Typable typable : typables) {
            typable.setType(newType);
        }
        typeSet.replaceType(this, newType);
    }

    public void addTypable(Typable typable) {
        typables.add(typable);
    }

    public Attribute getAttributeNoCreate(String attributeName) {
        return attributes.get(attributeName);
    }

    public Method getMethodNoCreate(String methodName) {
        return methods.get(methodName);
    }

    public Constructor getConstructorNoCreate(int paramsNumber) {
        return constructors.get(paramsNumber);
    }

    public Lambda setLambda(int paramsNumber) {
        if (lambda != null) {
            lambda.assertRightParamsNumber(paramsNumber);
            return lambda;
        }
        lambda = new Lambda(typeInferenceMotor, paramsNumber);
        return lambda;
    }

    public Lambda getLambda() {
        return lambda;
    }

    public List<Typable> getContainerTypables() {
        return containerTypables;
    }

    public void addContainerTypable(Typable containerTypable) {
        containerTypables.add(containerTypable);
    }

    /**************/

    public void checkDeducedAndCoherence() {
        if (isDeducedAndCoherent) {
            return;
        }
        int specialTypesNumber = 0;
        StandardType standardType = null;
        if (isVoid != null && isVoid) {
            specialTypesNumber++;
            standardType = typeInferenceMotor.getStandardTypeDirectory().getStandardType(StandardKnowledges.VOID_TYPE_NAME);
        }
        if (isLambda()) {
            specialTypesNumber++;
        }
        if (isList()) {
            specialTypesNumber++;
        }
        if (specialTypesNumber > 1) {
            throw new TypeConflictException();
        }
        if (specialTypesNumber == 1) {
            if (standardType != null) {
                standardType.checkIsRespectedByTypeAndReplace(this);
                return;
            }
            isDeducedAndCoherent = true;
            return;
        }
        if (simpleName == null) {
            throw new CantFindTypeException(this);
        }
        standardType = typeInferenceMotor.getStandardTypeDirectory().getStandardType(simpleName);
        if (standardType != null) {
            standardType.checkIsRespectedByTypeAndReplace(this);
            return;
        }
        isDeducedAndCoherent = true;
    }

    @Override
    public String toString() {
        return "type : " + (simpleName != null ? (simpleName.equals(StandardKnowledges.NULL_KEY_WORD) ? "the null type" : simpleName) : "unknown");
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = super.equals(obj);
        return result;
    }

    @Override
    public int hashCode() {
        /*if(simpleName == null) {
            return 0;
        }
        return simpleName.hashCode();*/
        return 0;
    }

    public List<Typable> getTypables() {
        return typables;
    }
}
