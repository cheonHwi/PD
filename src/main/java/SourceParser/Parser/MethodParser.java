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

        skipAccessModifiers();

        skipModifiers();

        Token returnTypeToken = lexer.getCurrentToken();
        methodInfo.setReturnType(returnTypeToken.getValue());
        methodInfo.setLineNumber(returnTypeToken.getLine());
        lexer.moveForward();

        Token methodNameToken = lexer.getCurrentToken();
        methodInfo.setMethodName(methodNameToken.getValue());
        lexer.moveForward();

        parseParameters(methodInfo);

        return methodInfo;
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
            StringBuilder typeBuilder = new StringBuilder();
            Token typeToken = lexer.getCurrentToken();
            typeBuilder.append(typeToken.getValue());
            lexer.moveForward();

            while (lexer.check(TokenType.LBRACKET)) {
                typeBuilder.append("[");
                lexer.moveForward();

                if (lexer.check(TokenType.RBRACKET)) {
                    typeBuilder.append("]");
                    lexer.moveForward();
                } else {
                    throw new RuntimeException("Expected ']' after '[' at line " + lexer.getCurrentToken().getLine());
                }
            }

            String paramType = typeBuilder.toString();

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
}
