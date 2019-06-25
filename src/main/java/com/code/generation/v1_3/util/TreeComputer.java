package com.code.generation.v1_3.util;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeComputer<Tree, Result> {
    private Tree tree;
    private Function<Tree, Boolean> isTerminalNodeFunction;
    private Function<Tree, Result> computeForTerminalNodeFunction;
    private Function<Tree, List<Tree>> subTreesFunction;
    private BiFunction<Tree, List<Result>, Result> aggregationFunction;

    public TreeComputer(Tree tree,
                        Function<Tree, Boolean> isTerminalNodeFunction,
                        Function<Tree, Result> computeForTerminalNodeFunction,
                        Function<Tree, List<Tree>> subTreesFunction,
                        BiFunction<Tree, List<Result>, Result> aggregationFunction){
        this.tree = tree;
        this.isTerminalNodeFunction = isTerminalNodeFunction;
        this.computeForTerminalNodeFunction = computeForTerminalNodeFunction;
        this.subTreesFunction = subTreesFunction;
        this.aggregationFunction = aggregationFunction;
    }

    public Result compute(){
        return compute(tree);
    }

    private Result compute(Tree tree) {
        if(isTerminalNodeFunction.apply(tree)){
            return computeForTerminalNodeFunction.apply(tree);
        }
        List<Tree> subTrees = subTreesFunction.apply(tree);
        List<Result> subResults = subTrees.stream().map(aTree -> compute(aTree)).collect(Collectors.toList());
        return aggregationFunction.apply(tree, subResults);
    }
}
