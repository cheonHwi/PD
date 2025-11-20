package SourceParser.Parser;

import SourceParser.Lexer.Lexer;
import SourceParser.Model.MethodInfo;
import SourceParser.Model.Parameter;
import SourceParser.Tokenizer.Token;
import SourceParser.Tokenizer.TokenType;

public class MethodParser {
    private final Lexer lexer;

    public MethodParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public MethodInfo parserMethodSignature() {
        MethodInfo methodInfo = new MethodInfo();

//        skipAccessModifiers();
        methodInfo.setMethodAccessModifier(getAccessModifier());

        skipModifiers();

        String returnType = parseType();
        methodInfo.setReturnType(returnType);
        methodInfo.setLineNumber(lexer.getCurrentToken().getLine());

        Token methodNameToken = lexer.getCurrentToken();
        methodInfo.setMethodName(methodNameToken.getValue());
        lexer.moveForward();

        parseParameters(methodInfo);

        return methodInfo;
    }

    private String getAccessModifier() {
        String accessModifier = lexer.matchesAny(TokenType.PUBLIC, TokenType.PRIVATE, TokenType.PROTECTED).getValue();
        lexer.moveForward();
        return accessModifier;
    }

    private void skipAccessModifiers() {
        lexer.skipAnyOf(TokenType.PUBLIC, TokenType.PRIVATE, TokenType.PROTECTED);
    }

    private void skipModifiers() {
        lexer.skipAnyOf(TokenType.STATIC, TokenType.FINAL, TokenType.ABSTRACT);
    }

    private void parseParameters(MethodInfo method) {
        if (!lexer.check(TokenType.LPAREN)) {
            throw new RuntimeException("Expected '(' after method name at line "
                    + lexer.getCurrentToken().getLine());
        }
        lexer.moveForward();

        while (!lexer.check(TokenType.RPAREN)) {
            String paramType = parseType();

            Token nameToken = lexer.getCurrentToken();
            String paramName = nameToken.getValue();
            lexer.moveForward();

            method.getParameters().add(new Parameter(paramType, paramName));

            if (lexer.check(TokenType.COMMA)) {
                lexer.moveForward();
            }
        }

        if (!lexer.check(TokenType.RPAREN)) {
            throw new RuntimeException("Expected ')' after parameters at line "
                    + lexer.getCurrentToken().getLine());
        }
        lexer.moveForward();
    }

    private String parseType() {
        StringBuilder methodReturnType = new StringBuilder();

        Token returnTypeToken = lexer.getCurrentToken();
        methodReturnType.append(returnTypeToken.getValue());
        lexer.moveForward();

        if (lexer.check(TokenType.LT)) {
            methodReturnType.append(parseGeneric());
        }

        if(lexer.check(TokenType.LBRACKET)){
            methodReturnType.append(parseArray());
        }

        return methodReturnType.toString();
    }

    private String parseGeneric() {
        StringBuilder genericSignature = new StringBuilder();

        genericSignature.append("<");
        lexer.moveForward();

        while (!lexer.check(TokenType.GT)) {
            if (lexer.check(TokenType.QUESTION)) {
                genericSignature.append("?");
                lexer.moveForward();

                if (lexer.check(TokenType.EXTENDS)) {
                    genericSignature.append(" extends ");
                    lexer.moveForward();
                    genericSignature.append(parseType());
                } else if (lexer.check(TokenType.SUPER)) {
                    genericSignature.append(" super ");
                    lexer.moveForward();
                    genericSignature.append(parseType());
                }
            } else {
                genericSignature.append(parseType());
            }
            if (lexer.check(TokenType.COMMA)) {
                genericSignature.append(", ");
                lexer.moveForward();
            }
        }

        if (!lexer.check(TokenType.GT)) {
            throw new RuntimeException("Expected '>' at line "
                    + lexer.getCurrentToken().getLine());
        }
        genericSignature.append(">");
        lexer.moveForward();

        return genericSignature.toString();
    }

    private String parseArray() {
        StringBuilder arraySignature = new StringBuilder();

        while (lexer.check(TokenType.LBRACKET)) {
            arraySignature.append("[");
            lexer.moveForward();

            if (lexer.check(TokenType.RBRACKET)) {
                arraySignature.append("]");
                lexer.moveForward();
            } else {
                throw new RuntimeException("Expected ']' after '[' at line "
                        + lexer.getCurrentToken().getLine());
            }
        }

        return arraySignature.toString();
    }
}
