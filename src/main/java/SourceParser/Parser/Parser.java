package SourceParser.Parser;

import SourceParser.Lexer.Lexer;
import SourceParser.Model.MethodInfo;

public class Parser {
    private final MethodParser methodParser;
    private final ClassParser classParser;

    public Parser(Lexer lexer) {
        this.methodParser = new MethodParser(lexer);
        this.classParser = new ClassParser(lexer);
    }

    public MethodInfo getMethodInfo() {
        return methodParser.parserMethodSignature();
    }
}
