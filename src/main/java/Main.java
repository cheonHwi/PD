import SourceParser.Tokenizer.Token;
import SourceParser.Tokenizer.Tokenizer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String code = "public static void main(String[] args) {}";
        Tokenizer tokenizer = new Tokenizer(code);
        List<Token> tokens = tokenizer.tokenize();

        for (Token token : tokens) {
            System.out.println(token.getTokenPositionInfo());
        }
    }
}
