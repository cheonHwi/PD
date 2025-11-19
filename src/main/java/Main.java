import SourceParser.Lexer.Lexer;
import SourceParser.Tokenizer.Token;
import SourceParser.Tokenizer.Tokenizer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String code = "public static void main(String[] args) {}";

        Lexer lexer = new Lexer(code);

        while (!lexer.isAtEnd()) {
            Token token = lexer.getCurrentToken();
            System.out.println(token);
            lexer.moveForward();
        }
    }
}
