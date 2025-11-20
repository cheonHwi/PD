package SourceParser.Parser;

import SourceParser.Lexer.Lexer;
import SourceParser.Model.ClassInfo;

import java.util.List;

public class ClassParser {
    private final Lexer lexer;

    public ClassParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public ClassInfo parseClass() {
    }

    private String parsePackage() {
    }

    private List<String> parseImports() {
    }
}
