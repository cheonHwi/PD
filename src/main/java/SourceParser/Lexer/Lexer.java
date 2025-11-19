package SourceParser.Lexer;

import SourceParser.Tokenizer.Token;
import SourceParser.Tokenizer.TokenType;
import SourceParser.Tokenizer.Tokenizer;

import java.util.List;

public class Lexer {
    private final List<Token> tokens;
    private int position;

    public Lexer(String code) {
        Tokenizer tokenizer = new Tokenizer(code);
        this.tokens = tokenizer.tokenize();
        this.position = 0;
    }

    public Token getCurrentToken() {
        if (isAtEnd()) return tokens.getLast();
        return tokens.get(position);
    }

    public void moveForward() {
        if (!isAtEnd()) position++;
    }

    public Token getLastToken() {
        return tokens.get(position - 1);
    }

    public boolean isAtEnd() {
        return position >= tokens.size() - 1 && tokens.get(position).getType() == TokenType.EOF;
    }

    public boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return getCurrentToken().getType() == type;
    }


    public boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                moveForward();
                return true;
            }
        }
        return false;
    }
}
