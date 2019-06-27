package com.code.generation.v1_3.inference.rules;

import com.code.generation.v1_3.elements.type.Typable;
import com.code.generation.v1_3.elements.type.Type;
import com.code.generation.v1_3.elements.type.standard.Operable;
import com.code.generation.v1_3.elements.type.standard.StandardTypable;
import com.code.generation.v1_3.elements.type.standard.StandardType;
import com.code.generation.v1_3.exception.NonOperableException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.inference.fusion.FusionDeclaration;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class OperablesRuleMotor {
    private TypeInferenceMotor typeInferenceMotor;

    private Set<Rule> rules;
    private Set<Typable> deducedTypables = new HashSet<>();
    private Set<FusionDeclaration> fusionDeclarations = new HashSet<>();

    public OperablesRuleMotor(TypeInferenceMotor typeInferenceMotor, Set<Rule> rules){
        this.typeInferenceMotor = typeInferenceMotor;
        this.rules = rules;
    }

    public Set<FusionDeclaration> infer() {
        initDeducedTypables();
        while (canInfer()) ;
        return fusionDeclarations;
    }

    private boolean canInfer() {
        boolean progress = false;
        List<Rule> toRemoves = new LinkedList<>();
        for (Rule rule : rules) {
            if (isAllKnownTypables(rule.getTargetTypables())) {
                toRemoves.add(rule);
                continue;
            }
            if (rule instanceof NormalRule && !isAllKnownTypables(rule.getParameterTypables())) {
                continue;
            }
            List<Deduction> deductions = rule.apply();
            if (deductions == null) {
                continue;
            }
            progress = true;
            for (Deduction deduction : deductions) {
                Operable operable = deduction.getOperable();
                Typable standardTypable = typeInferenceMotor.getStandardTypable(operable.getName());
                fusionDeclarations.add(new FusionDeclaration(typeInferenceMotor, deduction.getTypable(), standardTypable));
                deduction.getTypable().getType().setName(operable);
                deducedTypables.add(deduction.getTypable());
            }
        }
        rules.removeAll(toRemoves);
        return progress;
    }

    private void initDeducedTypables() {
        for (Rule rule : rules) {
            for (Typable typable : rule.getParameterTypables()) {
                manageTypable(typable);
            }
            for (Typable typable : rule.getTargetTypables()) {
                manageTypable(typable);
            }
        }
    }

    private void manageTypable(Typable typable) {
        Type type = typable.getType();
        if (type.isList() || type.isLambda() || type.isVoid()) {
            throw new NonOperableException("operation on list");
        }
        if (type.getSimpleName() != null) {
            deducedTypables.add(typable);
        }
    }

    private boolean isAllKnownTypables(List<Typable> typables) {
        for (Typable typable : typables) {
            if (!deducedTypables.contains(typable)) {
                return false;
            }
        }
        return true;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }
}
