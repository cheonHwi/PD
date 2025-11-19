import SourceParser.Tokenizer.Tokenizer;
import SourceParser.Tokenizer.Token;
import SourceParser.Tokenizer.TokenType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TokenizerTest {

    @Test
    @DisplayName("토크나이저 접근 제한자 토큰화 검증")
    public void testTokenizeAccessModifier() {
        String code = "private protected public access";
        Tokenizer tokenizer = new Tokenizer(code);

        List<Token> tokens = tokenizer.tokenize();

        assertNotNull(tokens);
        assertFalse(tokens.isEmpty());
        assertEquals(5, tokens.size());

        assertEquals(TokenType.PRIVATE, tokens.get(0).getType());
        assertEquals(TokenType.PROTECTED, tokens.get(1).getType());
        assertEquals(TokenType.PUBLIC, tokens.get(2).getType());
        assertEquals(TokenType.UNKNOWN, tokens.get(3).getType());
        assertEquals(TokenType.EOF, tokens.get(4).getType());
    }

    @Test
    @DisplayName("토크나이저 괄호, 특수문자 토큰화 검증")
    public void testTokenizerSymbols() {
        String code = "(){}[]<>,;.";
        Tokenizer tokenizer = new Tokenizer(code);

        List<Token> tokens = tokenizer.tokenize();

        assertNotNull(tokens);
        assertFalse(tokens.isEmpty());
        assertEquals(12, tokens.size());

        assertEquals(TokenType.LPAREN, tokens.get(0).getType());
        assertEquals(TokenType.RPAREN, tokens.get(1).getType());
        assertEquals(TokenType.LBRACE, tokens.get(2).getType());
        assertEquals(TokenType.RBRACE, tokens.get(3).getType());
        assertEquals(TokenType.LBRACKET, tokens.get(4).getType());
        assertEquals(TokenType.RBRACKET, tokens.get(5).getType());
        assertEquals(TokenType.LT, tokens.get(6).getType());
        assertEquals(TokenType.GT, tokens.get(7).getType());
        assertEquals(TokenType.COMMA, tokens.get(8).getType());
        assertEquals(TokenType.SEMICOLON, tokens.get(9).getType());
        assertEquals(TokenType.DOT, tokens.get(10).getType());
        assertEquals(TokenType.EOF, tokens.get(11).getType());
    }

    @Test
    @DisplayName("토크나이저 메서드 토큰화 검증")
    public void testTokenizeSimpleMethod() {
        String code = "public static void main(String[] args) {}";
        Tokenizer tokenizer = new Tokenizer(code);

        List<Token> tokens = tokenizer.tokenize();

        assertNotNull(tokens);
        assertFalse(tokens.isEmpty());
        assertEquals(13, tokens.size());

        assertEquals(TokenType.PUBLIC, tokens.get(0).getType());
        assertEquals("public", tokens.get(0).getValue());
        assertEquals(TokenType.STATIC, tokens.get(1).getType());
        assertEquals(TokenType.VOID, tokens.get(2).getType());
        assertEquals(TokenType.LPAREN, tokens.get(4).getType());
        assertEquals(TokenType.STRING, tokens.get(5).getType());
        assertEquals(TokenType.LBRACKET, tokens.get(6).getType());
        assertEquals(TokenType.RBRACKET, tokens.get(7).getType());
        assertEquals(TokenType.UNKNOWN, tokens.get(8).getType());
        assertEquals(TokenType.RPAREN, tokens.get(9).getType());
        assertEquals(TokenType.LBRACE, tokens.get(10).getType());
        assertEquals(TokenType.RBRACE, tokens.get(11).getType());
        assertEquals(TokenType.EOF, tokens.get(12).getType());
    }


}
