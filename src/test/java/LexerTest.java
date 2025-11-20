import SourceParser.Lexer.Lexer;
import SourceParser.Tokenizer.Token;
import SourceParser.Tokenizer.TokenType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    @DisplayName("렉서 토큰 순회 검증")
    public void testLexerTraversal() {
        String code = "public void test() {}";
        Lexer lexer = new Lexer(code);

        assertNotNull(lexer);
        assertFalse(lexer.isAtEnd());

        assertEquals(TokenType.PUBLIC, lexer.getCurrentToken().getType());
        lexer.moveForward();

        assertEquals(TokenType.VOID, lexer.getCurrentToken().getType());
        lexer.moveForward();

        assertEquals(TokenType.UNKNOWN, lexer.getCurrentToken().getType());
        assertEquals("test", lexer.getCurrentToken().getValue());
        lexer.moveForward();

        assertEquals(TokenType.LPAREN, lexer.getCurrentToken().getType());
    }

    @Test
    @DisplayName("렉서 check 메서드 검증")
    public void testLexerCheck() {
        String code = "public static void";
        Lexer lexer = new Lexer(code);

        assertTrue(lexer.check(TokenType.PUBLIC));
        assertFalse(lexer.check(TokenType.PRIVATE));
        lexer.moveForward();

        assertTrue(lexer.check(TokenType.STATIC));
        assertFalse(lexer.check(TokenType.PUBLIC));
        lexer.moveForward();

        assertTrue(lexer.check(TokenType.VOID));
    }

    @Test
    @DisplayName("렉서 match 메서드 검증")
    public void testLexerMatch() {
        String code = "public static void";
        Lexer lexer = new Lexer(code);

        assertTrue(lexer.match(TokenType.PUBLIC, TokenType.PRIVATE));
        assertTrue(lexer.match(TokenType.STATIC));
        assertFalse(lexer.match(TokenType.PRIVATE));
        assertTrue(lexer.match(TokenType.VOID));
    }

    @Test
    @DisplayName("렉서 matchesAny 메서드 검증")
    public void testLexerMatchesAny() {
        String code = "public static final";
        Lexer lexer = new Lexer(code);

        Token token = lexer.matchesAny(TokenType.PUBLIC, TokenType.PRIVATE, TokenType.PROTECTED);
        assertNotNull(token);
        assertEquals(TokenType.PUBLIC, token.getType());

        lexer.moveForward();
        token = lexer.matchesAny(TokenType.STATIC, TokenType.FINAL);
        assertNotNull(token);
        assertEquals(TokenType.STATIC, token.getType());

        lexer.moveForward();
        token = lexer.matchesAny(TokenType.STATIC, TokenType.FINAL);
        assertNotNull(token);
        assertEquals(TokenType.FINAL, token.getType());
    }

    @Test
    @DisplayName("렉서 skipAnyOf 메서드 검증")
    public void testLexerSkipAnyOf() {
        String code = "public static final void";
        Lexer lexer = new Lexer(code);

        lexer.skipAnyOf(TokenType.PUBLIC, TokenType.STATIC, TokenType.FINAL);

        assertEquals(TokenType.VOID, lexer.getCurrentToken().getType());
    }

    @Test
    @DisplayName("렉서 isAtEnd 메서드 검증")
    public void testLexerIsAtEnd() {
        String code = "public void";
        Lexer lexer = new Lexer(code);

        assertFalse(lexer.isAtEnd());

        lexer.moveForward();
        assertFalse(lexer.isAtEnd());

        lexer.moveForward();
        assertTrue(lexer.isAtEnd());
    }

    @Test
    @DisplayName("렉서 getLastToken 메서드 검증")
    public void testLexerGetLastToken() {
        String code = "public static";
        Lexer lexer = new Lexer(code);

        lexer.moveForward();
        Token lastToken = lexer.getLastToken();

        assertNotNull(lastToken);
        assertEquals(TokenType.PUBLIC, lastToken.getType());
        assertEquals("public", lastToken.getValue());

        lexer.moveForward();
        lastToken = lexer.getLastToken();
        assertEquals(TokenType.STATIC, lastToken.getType());
    }
}