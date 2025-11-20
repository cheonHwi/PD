package SourceParser.Parser;

import SourceParser.Lexer.Lexer;
import SourceParser.Model.ClassInfo;
import SourceParser.Model.MethodInfo;
import SourceParser.Tokenizer.Token;
import SourceParser.Tokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;
    private final MethodParser methodParser;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.methodParser = new MethodParser(lexer);
    }

    public MethodInfo getMethodInfo() {
        return methodParser.parserMethodSignature();
    }

    public ClassInfo getClassInfo() {
        ClassInfo classInfo = new ClassInfo();

        if(lexer.check(TokenType.PACKAGE)) {
            classInfo.setPackageName(parsePackage());
        }

        List<String> imports = parseImports();
        imports.forEach(imp -> classInfo.getImports().add(imp));

        classInfo.setAccessModifier(getClassAccessModifier());

        classInfo.setModifiers(getClassModifiers());

        if (!lexer.check(TokenType.CLASS)) {
            throw new RuntimeException("Expected 'class' keyword at line "
                    + lexer.getCurrentToken().getLine());
        }
        lexer.moveForward();

        classInfo.setClassName(lexer.getCurrentToken().getValue());
        lexer.moveForward();

        if (lexer.check(TokenType.EXTENDS)) {
            classInfo.setExtendsClass(parseExtends());
        }

        if (lexer.check(TokenType.IMPLEMENTS)) {
            List<String> implementsList = parseImplements();
            implementsList.forEach(impl -> classInfo.getImplementsList().add(impl));
        }

        List<MethodInfo> methods = parseMethods();
        methods.forEach(method -> classInfo.getMethods().add(method));

        return classInfo;
    }

    private String parsePackage() {
        lexer.moveForward();

        StringBuilder packageName = new StringBuilder();

        while(!lexer.check(TokenType.SEMICOLON)) {
            packageName.append(lexer.getCurrentToken().getValue());
            lexer.moveForward();
        }
        lexer.moveForward();

        return packageName.toString();
    }

    private List<String> parseImports() {
        List<String> imports = new ArrayList<>();

        while (lexer.check(TokenType.IMPORT)) {
            lexer.moveForward();

            StringBuilder importName = new StringBuilder();

            while (!lexer.check(TokenType.SEMICOLON)) {
                importName.append(lexer.getCurrentToken().getValue());
                lexer.moveForward();
            }

            lexer.moveForward();

            imports.add(importName.toString());
        }

        return imports;
    }

    private String getClassAccessModifier() {
        Token modifier = lexer.matchesAny(TokenType.PUBLIC, TokenType.PRIVATE, TokenType.PROTECTED);
        if (modifier != null) {
            lexer.moveForward();
            return modifier.getValue();
        }
        return "default";
    }

    private List<String> getClassModifiers() {
        List<String> modifiers = new ArrayList<>();
        while (true) {
            Token modifier = lexer.matchesAny(TokenType.STATIC, TokenType.FINAL, TokenType.ABSTRACT);
            if (modifier == null) break;
            modifiers.add(modifier.getValue());
            lexer.moveForward();
        }
        return modifiers;
    }

    private String parseExtends() {
        lexer.moveForward();

        String extendsClass = lexer.getCurrentToken().getValue();
        lexer.moveForward();

        return extendsClass;
    }

    private List<String> parseImplements() {
        List<String> implementsList = new ArrayList<>();

        lexer.moveForward();

        while (!lexer.check(TokenType.LBRACE)) {
            if (lexer.check(TokenType.COMMA)) {
                lexer.moveForward();
                continue;
            }

            implementsList.add(lexer.getCurrentToken().getValue());
            lexer.moveForward();
        }

        return implementsList;
    }

    private List<MethodInfo> parseMethods() {
        List<MethodInfo> methods = new ArrayList<>();

        if (!lexer.check(TokenType.LBRACE)) {
            throw new RuntimeException("Expected '{' at line "
                    + lexer.getCurrentToken().getLine());
        }
        lexer.moveForward();

        while (!lexer.check(TokenType.RBRACE) && !lexer.isAtEnd()) {
            Token accessModifier = lexer.matchesAny(
                    TokenType.PUBLIC, TokenType.PRIVATE, TokenType.PROTECTED
            );

            if (accessModifier != null) {
                MethodInfo method = methodParser.parserMethodSignature();
                methods.add(method);

                skipMethodBody();
            } else {
                lexer.moveForward();
            }
        }
        return methods;
    }

    private void skipMethodBody() {
        if (!lexer.check(TokenType.LBRACE)) {
            return;
        }

        int braceCount = 1;
        lexer.moveForward();

        while (braceCount > 0 && !lexer.isAtEnd()) {
            if (lexer.check(TokenType.LBRACE)) {
                braceCount++;
            } else if (lexer.check(TokenType.RBRACE)) {
                braceCount--;
            }
            lexer.moveForward();
        }
    }
}
