package com.code.generation.v1_3.visitors.after_deduced.result;

import com.code.generation.v1_3.elements.strong_type.CanAppearInReturnStat;
import com.code.generation.v1_3.elements.strong_type.CanBeReturnedType;
import com.code.generation.v1_3.elements.strong_type.NormalType;
import com.code.generation.v1_3.visitors.after_deduced.result.interruptions.Interruption;
import com.code.generation.v1_3.visitors.after_deduced.result.interruptions.LoopInterruption;
import com.code.generation.v1_3.visitors.after_deduced.result.interruptions.ReturnInterruption;
import com.code.generation.v1_3.visitors.after_deduced.result.interruptions.ThrowInterruption;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;
import java.util.stream.Collectors;

public class RunnableScopeOrStatResult extends Result {
    private boolean isAlwaysInterrupt;
    private boolean isAlwaysThrowsOrReturn;
    private boolean isBreak;
    private boolean isContinue;
    private List<ThrowInterruption> throwInterruptions;
    private ReturnInterruption returnInterruption;

    public RunnableScopeOrStatResult(Map<ParserRuleContext, Result> resultMap, ParserRuleContext ctx) {
        this(Collections.EMPTY_LIST, false, false, resultMap, ctx);
    }

    public RunnableScopeOrStatResult(Interruption interruption, Map<ParserRuleContext, Result> resultMap, ParserRuleContext ctx) {
        this(Collections.singletonList(interruption), true,
                interruption instanceof ThrowInterruption || interruption instanceof ReturnInterruption, resultMap, ctx);
    }

    public RunnableScopeOrStatResult(List<Interruption> interruptions, boolean isAlwaysInterrupt, boolean isAlwaysThrowsOrReturn, Map<ParserRuleContext, Result> resultMap, ParserRuleContext ctx) {
        super(resultMap, ctx);
        this.isAlwaysInterrupt = isAlwaysInterrupt;
        this.isAlwaysThrowsOrReturn = isAlwaysThrowsOrReturn;
        manageInterruptions(interruptions);
        minimizeThrowInterruption();
    }

    private void manageInterruptions(List<Interruption> interruptions) {
        throwInterruptions = new LinkedList<>();
        CanAppearInReturnStat canAppearInReturnStat = null;
        for (Interruption interruption : interruptions) {
            if (interruption instanceof LoopInterruption) {
                boolean isBreakInterruption = ((LoopInterruption) interruption).isBreak();
                if (isBreakInterruption) {
                    isBreak = true;
                } else {
                    isContinue = true;
                }
                continue;
            }
            if (interruption instanceof ThrowInterruption) {
                throwInterruptions.add((ThrowInterruption) interruption);
                continue;
            }
            ReturnInterruption returnInterruption = (ReturnInterruption) interruption;
            if (canAppearInReturnStat == null || canAppearInReturnStat.isNull()) {
                canAppearInReturnStat = returnInterruption.getCanAppearInReturnStat();
                continue;
            }
            ((CanBeReturnedType) canAppearInReturnStat).assertIsCompatibleWithReturn(canAppearInReturnStat);
        }
        if (canAppearInReturnStat != null) {
            returnInterruption = new ReturnInterruption(canAppearInReturnStat);
        }
    }

    private void minimizeThrowInterruption() {
        Set<NormalType> normalTypes = new HashSet<>();
        for (ThrowInterruption throwInterruption : throwInterruptions) {
            if (normalTypes.stream().noneMatch(normalType -> normalType.isSame(throwInterruption.getNormalType()))){
                normalTypes.add(throwInterruption.getNormalType());
            }
        }
        throwInterruptions = normalTypes.stream().map(normalType -> new ThrowInterruption(normalType)).collect(Collectors.toList());
    }

    public List<Interruption> getInterruptions() {
        List<Interruption> interruptions = new ArrayList<>(throwInterruptions);
        if (isBreak) {
            interruptions.add(LoopInterruption.BREAK_INTERRUPTION);
        }
        if (isContinue) {
            interruptions.add(LoopInterruption.CONTINUE_INTERRUPTION);
        }
        if (returnInterruption != null) {
            interruptions.add(returnInterruption);
        }
        return interruptions;
    }

    public ReturnInterruption getReturnInterruption() {
        return returnInterruption;
    }

    public boolean isAlwaysInterrupt() {
        return isAlwaysInterrupt || isAlwaysThrowsOrReturn;
    }

    public boolean isAlwaysThrowsOrReturn() {
        return isAlwaysThrowsOrReturn;
    }
}
