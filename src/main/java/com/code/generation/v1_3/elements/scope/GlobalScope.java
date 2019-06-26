package com.code.generation.v1_3.elements.scope;

import com.code.generation.v1_3.elements.build.BuiltData;
import com.code.generation.v1_3.elements.build.EmptyBuildData;
import com.code.generation.v1_3.elements.strong_type.builder.StrongTypeDirectory;
import com.code.generation.v1_3.elements.symbols.StandardVariable;
import com.code.generation.v1_3.exception.FileException;
import com.code.generation.v1_3.exception.ParsingFailedException;
import com.code.generation.v1_3.inference.TypeInferenceMotor;
import com.code.generation.v1_3.listeners.DeductionListener;
import com.code.generation.v1_3.visitors.after_deduced.checking.TypeCheckerVisitor;
import com.code.generation.v1_3.visitors.after_deduced.result.Result;
import com.code.generation.v1_3.visitors.for_variables.VariableAndParametersVisitor;
import com.generated.GrammarLexer;
import com.generated.GrammarParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GlobalScope extends BaseScope {
    private static final String CODE_EXTENSION_NAME = ".auto";
    public static final String GENERATED_EXTENSION_NAME = ".generated";
    public static final String UTIL = "Util";


    private static final String GLOBAL_VARIABLE_NAME = "global";
    public static final String GLOBAL_TYPE_NAME = makeTypeName(GLOBAL_VARIABLE_NAME);
    private static final String MODEL_VARIABLE_NAME = "model";
    public static final String MODEL_TYPE_NAME = makeTypeName(MODEL_VARIABLE_NAME);

    public static String makeTypeName(String name){
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private TypeInferenceMotor typeInferenceMotor = new TypeInferenceMotor();

    private File codeDirectory;
    private List<Code> codes = new LinkedList<>();

    private StrongTypeDirectory strongTypeDirectory;
    private Map<ParserRuleContext, Result> resultMap = new IdentityHashMap<>();

    public GlobalScope(File codeDirectory) {
        super(null);
        if(!codeDirectory.isDirectory()){
            throw new FileException(codeDirectory, " is not a directory");
        }
        this.codeDirectory = codeDirectory;
    }

    public void buildAndCompile(File targetFolder) throws IOException {
        BuiltData builtData = build();
        builtData.compile(targetFolder);
    }

    public BuiltData build() throws IOException {
        if(codeDirectory.listFiles().length == 0){
            return new EmptyBuildData();
        }
        buildModel();
        addCodeTrees();
        visitCodeTreesForTypeInference();

        strongTypeDirectory = new StrongTypeDirectory(typeInferenceMotor);
        visitCodeTreesTypeChecking();
        return new BuiltData(resultMap, strongTypeDirectory, codes);
    }

    private void buildModel() {
        defineStandardVariable(new StandardVariable(typeInferenceMotor, this, GLOBAL_VARIABLE_NAME, GLOBAL_TYPE_NAME));
        defineStandardVariable(new StandardVariable(typeInferenceMotor, this, MODEL_VARIABLE_NAME, MODEL_TYPE_NAME));
    }

    private void defineStandardVariable(StandardVariable variable) {
        putVariableNoError(variable);
    }

    private void addCodeTrees() throws IOException {
        for (File file : codeDirectory.listFiles()) {
            if (file.getName().endsWith(CODE_EXTENSION_NAME)) {
                String codeName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                GrammarParser.ProgContext progContext = parseCode(file);
                codes.add(new Code(codeName, progContext));
            }
        }
    }

    private boolean parsingFailded(GrammarLexer lexer, GrammarParser parser, GrammarParser.ProgContext progContext) {
        if(parser.getNumberOfSyntaxErrors() != 0){
            return true;
        }
        return progContext == null || progContext.getChildCount() == 0;
    }

    private GrammarParser.ProgContext parseCode(File file) throws IOException {
        GrammarLexer lexer = new GrammarLexer(CharStreams.fromStream(new FileInputStream(file)));
        lexer.removeErrorListeners();
        TokenStream tokenStream = new CommonTokenStream(lexer);
        GrammarParser parser = new GrammarParser(tokenStream);
        parser.removeErrorListeners();
        GrammarParser.ProgContext prog = parser.prog();
        if(parsingFailded(lexer, parser, prog)){
            throw new ParsingFailedException(file);
        }
        return prog;
    }

    public void visitCodeTreesForTypeInference() {
        for (Code code : codes) {
            VariableAndParametersVisitor variableAndParametersVisitor = new VariableAndParametersVisitor(typeInferenceMotor, this);
            variableAndParametersVisitor.visit(code.getProgContext());
        }
        typeInferenceMotor.buildVariablesAndVariableParametersMap();
        for (Code code : codes) {
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new DeductionListener(typeInferenceMotor), code.getProgContext());
        }
        typeInferenceMotor.infer();
        typeInferenceMotor.checkAllTypableAreDeducedAndCoherent();
    }

    private void visitCodeTreesTypeChecking() {
        for (Code code : codes) {
            TypeCheckerVisitor typeCheckerVisitor = new TypeCheckerVisitor(typeInferenceMotor, strongTypeDirectory, resultMap);
            typeCheckerVisitor.visit(code.getProgContext());
        }
        strongTypeDirectory.check();
    }
}
