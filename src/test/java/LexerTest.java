import SourceParser.Lexer.Lexer;
import SourceParser.Tokenizer.TokenType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {
    @Test
    @DisplayName("렉서 토큰 순회 검증")
    public void testLexerTraversal() {
        String code = "public static void main() {}";
        Lexer lexer = new Lexer(code);

        assertFalse(lexer.isAtEnd());
        assertEquals(TokenType.PUBLIC, lexer.getCurrentToken().getType());

        lexer.moveForward();
        assertEquals(TokenType.STATIC, lexer.getCurrentToken().getType());

        lexer.moveForward();
        assertEquals(TokenType.VOID, lexer.getCurrentToken().getType());

        lexer.moveForward();
        assertEquals("main", lexer.getCurrentToken().getValue());

        lexer.moveForward();
        assertEquals(TokenType.LPAREN, lexer.getCurrentToken().getType());
        lexer.moveForward();
        assertEquals(TokenType.RPAREN, lexer.getCurrentToken().getType());

        lexer.moveForward();
        assertEquals(TokenType.LBRACE, lexer.getCurrentToken().getType());
        lexer.moveForward();
        assertEquals(TokenType.RBRACE, lexer.getCurrentToken().getType());
    }
}
