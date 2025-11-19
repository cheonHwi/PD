package SourceParser.Tokenizer;

public class Token {
    private final TokenType type;
    private final String value;
    private final int line;
    private final int column;

    public Token(TokenType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return String.format("Token(Type: %s, Keyword: %s, Position: %s:%s)", type, value, line, column);
    }
}
