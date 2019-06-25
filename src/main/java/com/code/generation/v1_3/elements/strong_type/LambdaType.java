package com.code.generation.v1_3.elements.strong_type;

import com.code.generation.v1_3.elements.strong_type.callables.CallableDefinition;
import com.code.generation.v1_3.elements.strong_type.callables.Lambda;
import com.code.generation.v1_3.elements.strong_type.custom.Parameter;
import com.code.generation.v1_3.exception.WrongParamNumberException;
import com.code.generation.v1_3.exception.WrongTypeFormatException;
import com.code.generation.v1_3.visitors.after_deduced.checking.TypeCheckerVisitor;
import com.generated.GrammarParser;

import java.util.ArrayList;
import java.util.List;

public class LambdaType implements CanBeParameterType {
    private CanBeReturnedType returnedType;
    private List<NormalType> parameterTypes;

    public LambdaType(CanBeReturnedType returnedType, List<NormalType> parameterTypes) {
        this.returnedType = returnedType;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public void build() {
    }

    @Override
    public void checkIsCompatibleWithArgument(CanBeProvideForParameter canBeProvideForParameter) {
        if (!(canBeProvideForParameter instanceof LambdaType)) {
            throw new WrongTypeFormatException(canBeProvideForParameter, "parameter wrong type");
        }
        LambdaType otherType = (LambdaType) canBeProvideForParameter;
        if (parameterTypes.size() != otherType.parameterTypes.size()) {
            throw new WrongTypeFormatException(otherType, "not good lambda parameter");
        }
        returnedType.assertIsCompatibleWithReturn(otherType.returnedType);
        int index = 0;
        for (NormalType parameterType : parameterTypes) {
            parameterType.assertIsSame(otherType.parameterTypes.get(index));
            index++;
        }
    }

    public Lambda buildLambda(TypeCheckerVisitor typeCheckerVisitor, CallableDefinition callableDefinition, GrammarParser.LambdaArgContext lambdaArgContext) {
        int numberArguments = lambdaArgContext.complexId().size();
        if (parameterTypes.size() != numberArguments) {
            throw new WrongParamNumberException(parameterTypes.size(), numberArguments);
        }
        List<Parameter> parameters = new ArrayList<>(parameterTypes.size());
        for (int index = 0; index < numberArguments; index++) {
            NormalType lambdaParameterType = parameterTypes.get(index);
            GrammarParser.ComplexIdContext complexIdContext = lambdaArgContext.complexId(index);

            typeCheckerVisitor.checkTypeCompatibility(lambdaParameterType, complexIdContext.type());
            parameters.add(new Parameter(complexIdContext.ID().getText(), lambdaParameterType));
        }
        return new Lambda(returnedType, parameters, callableDefinition);
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
