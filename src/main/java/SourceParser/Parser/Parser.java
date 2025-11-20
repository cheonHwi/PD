package SourceParser.Parser;

import SourceParser.Lexer.Lexer;
import SourceParser.Model.MethodInfo;

public class Parser {
    private final MethodParser methodParser;

    public Parser(Lexer lexer) {
        this.methodParser = new MethodParser(lexer);
    }

    public MethodInfo getMethodInfo() {
        return methodParser.parserMethodSignature();
    }
}
