package SourceParser.Tokenizer;

import java.util.HashMap;
import java.util.Map;

public class KeywordMap {
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();

    static {
        // 접근 제한자
        KEYWORDS.put("public", TokenType.PUBLIC);
        KEYWORDS.put("private", TokenType.PRIVATE);
        KEYWORDS.put("protected", TokenType.PROTECTED);

        // 수식어
        KEYWORDS.put("static", TokenType.STATIC);
        KEYWORDS.put("final", TokenType.FINAL);
        KEYWORDS.put("abstract", TokenType.ABSTRACT);

        // 클래스 키워드
        KEYWORDS.put("class", TokenType.CLASS);
        KEYWORDS.put("interface", TokenType.INTERFACE);
        KEYWORDS.put("enum", TokenType.ENUM);
        KEYWORDS.put("extends", TokenType.EXTENDS);
        KEYWORDS.put("implements", TokenType.IMPLEMENTS);

        // 타입
        KEYWORDS.put("int", TokenType.INT);
        KEYWORDS.put("long", TokenType.LONG);
        KEYWORDS.put("float", TokenType.FLOAT);
        KEYWORDS.put("double", TokenType.DOUBLE);
        KEYWORDS.put("char", TokenType.CHAR);
        KEYWORDS.put("boolean", TokenType.BOOLEAN);
        KEYWORDS.put("void", TokenType.VOID);

        // 제어문
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("return", TokenType.RETURN);

        // 예외 처리
        KEYWORDS.put("try", TokenType.TRY);
        KEYWORDS.put("catch", TokenType.CATCH);
        KEYWORDS.put("finally", TokenType.FINALLY);
        KEYWORDS.put("throw", TokenType.THROW);
        KEYWORDS.put("throws", TokenType.THROWS);

        // 기타
        KEYWORDS.put("package", TokenType.PACKAGE);
        KEYWORDS.put("import", TokenType.IMPORT);
    }

    public static TokenType getType(String word) {
        return KEYWORDS.getOrDefault(word, TokenType.UNKNOWN);
    }

    public static boolean isKeyword(String word) {
        return KEYWORDS.containsKey(word);
    }
}