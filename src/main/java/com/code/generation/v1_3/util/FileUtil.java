package com.code.generation.v1_3.util;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FileUtil {
    public static <Result> Result processFileOrFolder(Class<Result> returnType, File fileOrFolder, Function<File, Result> fileFunction, Function<File, Result> folderFunction) {
        if (fileOrFolder.isFile()) {
            return fileFunction.apply(fileOrFolder);
        }
        return folderFunction.apply(fileOrFolder);
    }

    public static <Result> Result processFileOrFolderSubElements(Class<Result> returnType, File fileOrFolder, Function<File, Result> fileFunction, Function<List<File>, Result> subElsFunction) {
        if (fileOrFolder.isFile()) {
            return fileFunction.apply(fileOrFolder);
        }
        return subElsFunction.apply(getSubEls(fileOrFolder));
    }

    public static <Result> Result processFileOrFolderRecursive(Class<Result> returnType, File fileOrFolder, Function<File, Result> oneFileFunction, BiFunction<File, List<Result>, Result> aggregationFunction) {
        TreeComputer<File, Result> treeComputer = new TreeComputer<>(fileOrFolder,
                File::isFile,
                aFile -> oneFileFunction.apply(aFile),
                aFolder -> getSubEls(aFolder),
                (folder, resultsOfChildren) -> aggregationFunction.apply(folder, resultsOfChildren));
        return treeComputer.compute();
    }

    public static List<File> getSubEls(File aFolder) {
        if(aFolder.isFile()){
            throw new IllegalStateException();
        }
        File[] subEls = aFolder.listFiles();
        return subEls == null ? Collections.EMPTY_LIST : Arrays.asList(subEls);
    }
}
