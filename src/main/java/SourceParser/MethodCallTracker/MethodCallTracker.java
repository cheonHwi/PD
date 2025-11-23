package MethodCallTracker;

import SourceParser.Lexer.Lexer;
import SourceParser.Model.MethodCall;
import SourceParser.Tokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class MethodCallTracker {
    private final Lexer lexer;

    public MethodCallTracker(Lexer lexer) {
        this.lexer = lexer;
    }

    public List<MethodCall> trackMethodCalls() {
        List<MethodCall> methodCalls = new ArrayList<>();

        if (!lexer.check(TokenType.LBRACE)) {
            return methodCalls;
        }

        int braceDepth = 1;
        lexer.moveForward();

        while (braceDepth > 0 && !lexer.isAtEnd()) {
            if (lexer.check(TokenType.LBRACE)) {
                braceDepth++;
                lexer.moveForward();
            } else if (lexer.check(TokenType.RBRACE)) {
                braceDepth--;
                lexer.moveForward();
            } else if (isMethodCallPattern()) {
                MethodCall call = parseMethodCall();
                if (call != null) {
                    methodCalls.add(call);
                }
            } else {
                lexer.moveForward();
            }
        }

        return methodCalls;
    }

    private boolean isMethodCallPattern() {
        int savedPosition = lexer.getPosition();
        boolean isCall = false;

        if (lexer.getCurrentToken().getType() == TokenType.UNKNOWN) {
            lexer.moveForward();

            if (lexer.check(TokenType.DOT)) {
                lexer.moveForward();

                if (lexer.getCurrentToken().getType() == TokenType.UNKNOWN) {
                    lexer.moveForward();

                    if (lexer.check(TokenType.LPAREN)) {
                        isCall = true;
                    }
                }
            }
            else if (lexer.check(TokenType.LPAREN)) {
                isCall = true;
            }
        }

        lexer.setPosition(savedPosition);
        return isCall;
    }

    private MethodCall parseMethodCall() {
        int lineNumber = lexer.getCurrentToken().getLine();

        String first = lexer.getCurrentToken().getValue();
        lexer.moveForward();

        if (lexer.check(TokenType.DOT)) {
            lexer.moveForward();

            // 메서드명
            String methodName = lexer.getCurrentToken().getValue();
            lexer.moveForward();

            // 괄호 건너뛰기
            skipParentheses();

            return new MethodCall(first, methodName, lineNumber);
        }
        else if (lexer.check(TokenType.LPAREN)) {
            skipParentheses();

            return new MethodCall(null, first, lineNumber);
        }

        return null;
    }

    private void skipParentheses() {
        if (!lexer.check(TokenType.LPAREN)) {
            return;
        }

        int parenDepth = 1;
        lexer.moveForward();

        while (parenDepth > 0 && !lexer.isAtEnd()) {
            if (lexer.check(TokenType.LPAREN)) {
                parenDepth++;
            } else if (lexer.check(TokenType.RPAREN)) {
                parenDepth--;
            }
            lexer.moveForward();
        }
    }
}