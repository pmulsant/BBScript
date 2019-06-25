package com.code.generation.v1_3.elements.symbols;

import com.code.generation.v1_3.elements.scope.CallableScope;
import com.code.generation.v1_3.elements.scope.GlobalScope;
import com.code.generation.v1_3.elements.scope.ProgScope;
import com.code.generation.v1_3.elements.scope.Scope;
import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.exception.VariableCantBeProvideException;
import com.code.generation.v1_3.exception.VariableReadBeforeInitializationException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.generated.GrammarParser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.IdentityHashMap;
import java.util.Map;

public class Variable extends Typable {
    private Scope scope;
    private String name;
    private Use firstUse;
    protected Map<ParserRuleContext, Use> uses = new IdentityHashMap<>();
    private boolean isProvided;

    public Variable(TypeInferenceMotor typeInferenceMotor, Scope scope, String name, Position firstPosition) {
        super(typeInferenceMotor);
        this.scope = scope;
        this.name = name;
        if (firstPosition != null) {
            this.firstUse = new Use(firstPosition);
            uses.put(firstPosition.getContext(), firstUse);
        }
        initialize();
    }

    public void setProvided() {
        ProgScope progScope = scope.searchProgScope();
        if (progScope == null) {
            throw new VariableCantBeProvideException(this);
        }
        scope.removeVariable(name);
        scope = progScope;
        scope.defineVariable(this);
        isProvided = true;
    }

    public void checkInitializationCoherence() {
        if (isProvided || isParameter()) {
            return;
        }
        if (!(scope instanceof GlobalScope) && !firstUse.isWrite()) {
            throw new VariableReadBeforeInitializationException(this);
        }
    }

    public void addUseExpression(Position position) {
        uses.put(position.getContext(), new Use(position));
    }

    public void addWritingExpression(GrammarParser.ExprContext exprContext) {
        uses.get(exprContext).setWriteIndication();
    }

    public Map<ParserRuleContext, Use> getUses() {
        return uses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefinedHere(GrammarParser.IdentifierContext identifierContext){
        if(firstUse == null){
            return false;
        }
        return firstUse.getPosition().getContext() == identifierContext;
    }

    public boolean isProvided() {
        return isProvided;
    }

    @Override
    public String toString() {
        return "variable typable (" + getName() + ", " + getType().toString() + ")";
    }

    public boolean isParameter(){
        return scope instanceof CallableScope;
    }
}
