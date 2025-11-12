package SourceParser.Tokenizer;

public class Token {
    TokenType type;
    String value;
    int line;
    int column;

    public Token(TokenType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }
}
