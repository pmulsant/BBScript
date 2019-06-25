package com.code.generation.tests;

import com.code.generation.tests.run.Run;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestRunner {
    private static final String JAR_PATH = "target\\final-code-generation-v1-3-1.0-SNAPSHOT.jar";

    private static final int TIME_LIMIT_IN_SECOND = 30;

    public static boolean wantContinue() {
        if(FileUtils.isFileOlder(new File(JAR_PATH), System.currentTimeMillis() - TIME_LIMIT_IN_SECOND * 1000)){
            int response = JOptionPane.showConfirmDialog(null,
                    "the jar file is older than " + TIME_LIMIT_IN_SECOND + " seconds. Do you really want to continue", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return response == JOptionPane.OK_OPTION;
        }
        return true;
    }

    public static RunResult run(File sourceFolder, File resultFolder, boolean careOfOldJar){
        if(!careOfOldJar || wantContinue()){
            run(sourceFolder, resultFolder);
        }
        return null;
    }

    public static RunResult run(File sourceFolder, File resultFolder) {
        return getRunResult(Run.class, new String[]{sourceFolder.getAbsolutePath(), resultFolder.getAbsolutePath()});
    }

    private static RunResult getRunResult(Class<?> mainClass, String[] args){
        String strResult = getResultStringCommand(mainClass, args);
        return new RunResult(strResult);
    }

    private static String getResultStringCommand(Class<?> mainClass, String[] args){
        return getResultStringCommand(makeCommand(mainClass, args), true);
    }

    private static String getResultStringCommand(String command, boolean errorStream){
        Runtime rt = Runtime.getRuntime();
        Process proc;
        try {
            proc = rt.exec(command);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(errorStream ? proc.getErrorStream() : proc.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = stdInput.readLine()) != null){
                stringBuilder.append(line);
            }
            String str = stringBuilder.toString();
            return str.isEmpty() ? null : str;
        } catch (IOException e) {
            return e.toString();
        }
    }

    private static String makeCommand(Class<?> mainClass, String[] args){
        return "java -cp " + JAR_PATH + " " + mainClass.getName() + " " + String.join(" ", args);
    }
}
