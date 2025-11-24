package SourceParser.Tokenizer;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final String source;
    private int position;
    private int line;
    private int column;
    private final List<Token> tokens;

    public Tokenizer(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
        this.position = 0;
        this.line = 1;
        this.column = 1;
    }

    public List<Token> tokenize() {
        while (!isAtEnd()) {
            tokenizeNext();
        }
        tokens.add(new Token(TokenType.EOF, "", line, column));
        return tokens;
    }

    private void tokenizeNext() {
        char c = nextChar();

        switch (c) {
            case '(': addToken(TokenType.LPAREN, "("); break;
            case ')': addToken(TokenType.RPAREN, ")"); break;
            case '{': addToken(TokenType.LBRACE, "{"); break;
            case '}': addToken(TokenType.RBRACE, "}"); break;
            case '[': addToken(TokenType.LBRACKET, "["); break;
            case ']': addToken(TokenType.RBRACKET, "]"); break;
            case '<': addToken(TokenType.LT, "<"); break;
            case '>': addToken(TokenType.GT, ">"); break;
            case ',': addToken(TokenType.COMMA, ","); break;
            case ';': addToken(TokenType.SEMICOLON, ";"); break;
            case ':': addToken(TokenType.COLON, ":"); break;
            case '+': addToken(TokenType.PLUS, "+"); break;
            case '-': addToken(TokenType.MINUS, "-"); break;
            case '*': addToken(TokenType.STAR, "*"); break;
            case '%': addToken(TokenType.PERCENT, "%"); break;
            case '=': addToken(TokenType.ASSIGN, "="); break;
            case '/': addToken(TokenType.SLASH, "/"); break;
            case '?': addToken(TokenType.QUESTION, "?"); break;
            case '@': addToken(TokenType.AT, "@"); break;

            case '.':
                if (peek() == '.' && peekNext() == '.') {
                    addToken(TokenType.VARARGS, "...");
                    nextChar();
                    nextChar();
                } else {
                    addToken(TokenType.DOT, ".");
                }
                break;


            case ' ':
            case '\r':
            case '\t':
                // 공백 무시
                break;

            case '\n':
                line++;
                column = 1;
                break;

            default:
                if (isChar(c)) {
                    tokenizeWord();
                } else {
                    addToken(TokenType.UNKNOWN, String.valueOf(c));
                }
                break;
        }
    }

    private void tokenizeWord() {
        int start = position - 1;
        int startColumn = column - 1;

        while (!isAtEnd() && isCharNumber(peek())) {
            nextChar();
        }

        String word = source.substring(start, position);
        TokenType type = KeywordMap.getType(word);

        tokens.add(new Token(type, word, line, startColumn));
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(position);
    }

    private char peekNext() {
        if (position + 1 >= source.length()) return '\0';
        return source.charAt(position + 1);
    }

    private boolean isAtEnd() {
        return position >= source.length();
    }

    private char nextChar() {
        column++;
        return source.charAt(position++);
    }

    private boolean isChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isCharNumber(char c) {
        return isChar(c) || (c >= '0' && c <= '9');
    }

    private void addToken(TokenType type, String value) {
        tokens.add(new Token(type, value, line, column-value.length()));
    }
}
