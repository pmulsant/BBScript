package com.code.generation.tests.run;

import com.code.generation.RuntimeCustomException;
import com.code.generation.tests.exception.WrongArgNumberException;
import com.code.generation.v1_3.elements.build.BuiltData;
import com.code.generation.v1_3.elements.scope.GlobalScope;

import java.io.*;

public class Run {
    public static final String BUILD = "build";
    public static final String COMPILE = "compile";
    public static final String ILLEGAL = "illegal";

    protected File sourceFolder;
    protected File resultFolder;

    public static void main(String[] args) {
        try {
            checkArgs(args);
            File sourceFolder = new File(args[0]);
            File resultFolder = new File(args[1]);
            if(!resultFolder.exists()){
                resultFolder.mkdirs();
            }
            Run run = new Run(sourceFolder, resultFolder);
            run.run();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected static void checkArgs(String[] args) throws WrongArgNumberException {
        if(args.length != 2){
            throw new WrongArgNumberException(args);
        }
    }

    public Run(File sourceFolder, File resultFolder) {
        this.sourceFolder = sourceFolder;
        this.resultFolder = resultFolder;
    }

    protected void run() throws IOException {
        BuiltData builtData = buildAndWriteErrors();
        compileAndWriteErrors(builtData);
    }

    private BuiltData buildAndWriteErrors() throws IOException {
        BuiltData build = null;
        try {
            build = new GlobalScope(sourceFolder).build();
        } catch (RuntimeCustomException e) {
            writeBuildError(e);
        } catch (Throwable e){
            writeIllegalError(e);
        }
        return build;
    }

    private void compileAndWriteErrors(BuiltData builtData) throws IOException {
        if(builtData == null){
            return;
        }
        try {
            builtData.compile(resultFolder);
        } catch (Throwable e) {
            writeCompileError(e);
        }
    }

    private void writeBuildError(RuntimeCustomException e) throws IOException {
        writeError(e, Run.BUILD);
    }

    private void writeCompileError(Throwable e) throws IOException {
        writeError(e, Run.COMPILE);
    }

    private void writeIllegalError(Throwable e) throws IOException {
        writeError(e, Run.ILLEGAL);
    }

    private void writeError(Throwable e, String errorType) throws IOException {
        BufferedOutputStream errorOutStream = new BufferedOutputStream(new FileOutputStream(new File(resultFolder.getAbsolutePath() + "/" + errorType + ".error")));
        PrintWriter printWriter = new PrintWriter(errorOutStream);
        if(errorType.equals(ILLEGAL)){
            e.printStackTrace(printWriter);
        } else {
            printWriter.println(e);
        }
        printWriter.close();
        errorOutStream.close();
    }
}
