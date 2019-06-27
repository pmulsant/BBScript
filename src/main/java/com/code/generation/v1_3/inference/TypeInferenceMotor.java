package com.code.generation.v1_3.inference;

import com.code.generation.v1_3.elements.scope.NormalCallableScope;
import com.code.generation.v1_3.elements.symbols.Variable;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericConstructor;
import com.code.generation.v1_3.elements.type.custom.callables.complex.GenericMethod;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Constructor;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Function;
import com.code.generation.v1_3.elements.type.custom.callables.simples.Method;
import com.code.generation.v1_3.elements.type.standard.StandardKnowledges;
import com.code.generation.v1_3.elements.type.standard.StandardTypable;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.elements.type.standard.StandardTypeDirectory;
import com.code.generation.v1_3.elements.type.standard.callables.functions.StandardFunction;
import com.code.generation.v1_3.elements.type.standard.simple_types.NullStandardType;
import com.code.generation.v1_3.exception.CantBeOfNullTypeTypable;
import com.code.generation.v1_3.exception.WrongParamNumberException;
import com.code.generation.v1_3.inference.fusion.FusionMotor;
import com.code.generation.v1_3.inference.rules.OperablesRuleMotor;
import com.code.generation.v1_3.inference.rules.Rule;
import com.generated.GrammarParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;
import java.util.stream.Collectors;

public class TypeInferenceMotor {
    private StandardTypeDirectory standardTypeDirectory;

    Map<GrammarParser.ExprContext, NormalCallableScope> normalCallableScopes = new IdentityHashMap<>();

    private Map<GrammarParser.IdentifierContext, Variable> variables = new IdentityHashMap<>();
    private Map<GrammarParser.ComplexIdContext, Variable> variableParameters = new IdentityHashMap<>();
    private Map<String, Function> functions = new HashMap<>();
    private FusionMotor fusionMotor;
    private Map<GrammarParser.ExprContext, TypableExpression> typableExpressions = new IdentityHashMap<>();

    public TypeInferenceMotor() {
        fusionMotor = new FusionMotor(this);
        standardTypeDirectory = new StandardTypeDirectory(this);
    }

    public void buildVariablesAndVariableParametersMap() {
        for (Typable typable : fusionMotor.getTypables()) {
            if (typable instanceof Variable) {
                Variable variable = (Variable) typable;
                variable.checkInitializationCoherence();
                for (ParserRuleContext ruleContext : variable.getUses().keySet()) {
                    if (ruleContext instanceof GrammarParser.IdentifierContext) {
                        this.variables.put((GrammarParser.IdentifierContext) ruleContext, variable);
                    } else if (ruleContext instanceof GrammarParser.ComplexIdContext) {
                        this.variableParameters.put((GrammarParser.ComplexIdContext) ruleContext, variable);
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
        }
    }

    public void infer() {
        fusionMotor.fusion();
    }

    public void checkAllTypableAreDeducedAndCoherent() {
        Set<Type> allTypes = fusionMotor.getTypables().stream().map(typable -> typable.getType()).collect(Collectors.toSet());
        for (Type type : allTypes) {
            type.checkDeducedAndCoherence();
        }
        for (Typable typable : fusionMotor.getTypables()) {
            if(typable.getType() instanceof NullStandardType && !typable.canBeNull()){
                throw new CantBeOfNullTypeTypable(typable);
            }
        }
    }

    public void addFusionOfTypesDeclaration(Typable typable1, Typable typable2) {
        if(typable1 == null || typable2 == null){
            throw new IllegalStateException();
        }
        if(typable1 == typable2){
            throw new IllegalStateException();
        }
        if(typable1 instanceof StandardTypable && typable2 instanceof StandardTypable) {
            if (!typable1.getType().getSimpleName().equals(typable2.getType().getSimpleName())){
                throw new IllegalStateException();
            }
            return;
        }
        fusionMotor.addFusion(typable1, typable2);
    }

    public void addTypable(Typable typable) {
        fusionMotor.addTypable(typable);
        if (typable instanceof TypableExpression) {
            TypableExpression typableExpression = (TypableExpression) typable;
            if (typableExpressions.containsKey(typableExpression.getExprContext())) {
                throw new IllegalStateException();
            }
            typableExpressions.put(typableExpression.getExprContext(), typableExpression);
        }
    }

    public Typable getTypableExpressionFromExpr(GrammarParser.ExprContext exprContext, Boolean isVoid) {
        TypableExpression typable = typableExpressions.computeIfAbsent(exprContext, key -> new TypableExpression(this, key));
        if (isVoid != null) {
            typable.getType().setVoid(isVoid);
        }
        return typable;
    }


    public Variable getVariable(GrammarParser.IdentifierContext identifierContext) {
        return variables.get(identifierContext);
    }

    public Variable getVariableParameter(GrammarParser.ComplexIdContext complexIdContext) {
        return variableParameters.get(complexIdContext);
    }

    public Function getFunction(String functionName, int paramsNumber) {
        Function function = functions.get(functionName);
        if (function == null) {
            function = new Function(this, functionName, paramsNumber);
            if(standardTypeDirectory.getStandardFunction(functionName) == null) {
                functions.put(functionName, function);
            }
        }
        function.assertRightParamsNumber(paramsNumber);
        return function;
    }

    public void addRule(Rule rule) {
        fusionMotor.addRule(rule);
    }

    public List<Typable> getTypables() {
        return fusionMotor.getTypables();
    }

    public Map<GrammarParser.IdentifierContext, Variable> getVariables() {
        return variables;
    }

    public Map<String, Function> getFunctions() {
        return functions;
    }

    public void processLinkIfStandardConstructor(Typable topTypable, Constructor constructor, boolean isDefine, GrammarParser.TypeContext typeContext) {
        Map<Integer, GenericConstructor> standardConstructorMap = standardTypeDirectory.getConstructorMap(typeContext);
        if (standardConstructorMap != null) {
            assertCallableIsNotDefined(isDefine);
            GenericConstructor genericConstructor = standardConstructorMap.get(constructor.getParamsNumber());
            if (genericConstructor == null) {
                throw new WrongParamNumberException(genericConstructor, constructor.getParamsNumber());
            }
            genericConstructor.processLink(topTypable, constructor);
        }
    }

    public void processLinkIfStandardMethod(Typable innerTypable, Method method, boolean isDefine) {
        GenericMethod genericMethod = standardTypeDirectory.getMethod(method.getName());
        if (genericMethod != null) {
            assertCallableIsNotDefined(isDefine);
            genericMethod.processLinks(innerTypable, method);
        }
    }

    public void processLinkIfStandardFunction(Function function, boolean isDefine) {
        StandardFunction standardFunction = standardTypeDirectory.getStandardFunction(function.getName());
        if (standardFunction != null) {
            assertCallableIsNotDefined(isDefine);
            standardFunction.processLinks(function);
        }
    }

    public StandardTypeDirectory getStandardTypeDirectory() {
        return standardTypeDirectory;
    }

    public Typable getStandardTypable(String typeName){
        StandardType standardType = standardTypeDirectory.getStandardType(typeName);
        if(standardType == null){
            throw new IllegalStateException();
        }
        return standardType.getOriginalTypable();
    }

    private void assertCallableIsNotDefined(boolean isDefine) {
        if (isDefine) {
            throw new IllegalStateException("can't define standard callable");
        }
    }

    public void putNormalCallableScope(GrammarParser.ExprContext topDefContext, NormalCallableScope normalCallableScope) {
        normalCallableScopes.put(topDefContext, normalCallableScope);
    }

    public Variable getThisVariable(GrammarParser.ExprContext topDefContext) {
        return normalCallableScopes.get(topDefContext).resolveVariable(StandardKnowledges.THIS_VARIABLE_NAME);
    }

    public static class TypableExpression extends Typable {
        private GrammarParser.ExprContext exprContext;

        private TypableExpression(TypeInferenceMotor typeInferenceMotor, GrammarParser.ExprContext exprContext) {
            super(typeInferenceMotor);
            this.exprContext = exprContext;
            initialize();
        }

        public GrammarParser.ExprContext getExprContext() {
            return exprContext;
        }

        @Override
        public String toString() {
            return "expression (" + exprContext.getClass().getSimpleName() + ", " + getType().toString() + ")";
        }

        @Override
        public boolean canBeNull() {
            return true;
        }
    }
}
