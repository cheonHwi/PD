import SourceParser.Lexer.Lexer;
import SourceParser.Model.ClassInfo;
import SourceParser.Model.MethodInfo;
import SourceParser.Parser.Parser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassParserTest {

    @Test
    @DisplayName("클래스 파싱 : 패키지명 파싱 검증")
    public void testParseSimplePackageName() {
        String code = """
                package Parser;

                import org.junit.jupiter.api.DisplayName;
                import org.junit.jupiter.api.Test;

                public class ClassParserTest {}
                """;

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        ClassInfo classInfo = parser.getClassInfo();

        assertEquals(classInfo.getPackageName(), "Parser");
    }

    @Test
    @DisplayName("클래스 파싱 : 깊은 디렉토리에 있는 패키지명 파싱 검증")
    public void testParseNestedPackageName() {
        String code = """
                package Parser.SourceParser.Utils;

                import org.junit.jupiter.api.DisplayName;
                import org.junit.jupiter.api.Test;

                public class ClassParserTest {}
                """;

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        ClassInfo classInfo = parser.getClassInfo();

        assertEquals(classInfo.getPackageName(), "Parser.SourceParser.Utils");
    }

    @Test
    @DisplayName("클래스 파싱 : 임포트 목록 파싱 검증")
    public void testParseImports() {
        String code = """
                package SourceParser.Parser;
                
                import SourceParser.Lexer.Lexer;
                import SourceParser.Tokenizer.TokenType;
                
                public class ClassParser {}
                """;

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        ClassInfo classInfo = parser.getClassInfo();

        assertEquals(classInfo.getImports().getFirst(), "SourceParser.Lexer.Lexer");
        assertEquals(classInfo.getImports().get(1), "SourceParser.Tokenizer.TokenType");
    }

    @Test
    @DisplayName("클래스 파싱 : 임포트 목록 파싱 검증")
    public void testParseClassName() {
        String code = """
                package SourceParser.Parser;
                
                import SourceParser.Lexer.Lexer;
                
                public class ClassParser {}
                """;

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        ClassInfo classInfo = parser.getClassInfo();

        assertEquals(classInfo.getClassName(), "ClassParser");
    }

    @Test
    @DisplayName("클래스 파싱 : 클래스 상속 파싱 검증")
    public void testParseExtendClass() {
        String code = """
                package SourceParser.Parser;
                
                import SourceParser.Lexer.Lexer;
                
                public class ClassParser extends BaseParser {}
                """;

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        ClassInfo classInfo = parser.getClassInfo();

        assertEquals(classInfo.getExtendsClass(), "BaseParser");
    }

    @Test
    @DisplayName("클래스 파싱 : 클래스 상속(구현) 파싱 검증")
    public void testParseImplementClass() {
        String code = """
                package SourceParser.Parser;
                
                import SourceParser.Lexer.Lexer;
                
                public class ClassParser implements SourceParser, FileReader {}
                """;

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        ClassInfo classInfo = parser.getClassInfo();

        assertEquals(classInfo.getImplementsList().getFirst(), "SourceParser");
        assertEquals(classInfo.getImplementsList().get(1), "FileReader");
    }

    @Test
    @DisplayName("클래스 파싱 : 메서드 목록 파싱 검증")
    public void testParseMethod() {
        String code = """
                package SourceParser.Parser;
                
                import SourceParser.Lexer.Lexer;
                
                public class Main {
                    public static void main(String[] args) {
                        System.out.println(methodInfo);
                    }
               
                    private int getNumber() { return 1; }
                
                    public List<String> getList() { return null; }
                }
                """;

        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        ClassInfo classInfo = parser.getClassInfo();
        List<MethodInfo> methodInfos = classInfo.getMethods();

        assertEquals(classInfo.getClassName(), "Main");
        assertEquals(classInfo.getAccessModifier(), "public");

        assertEquals(methodInfos.getFirst().toString(), "public void main(String[] args)");
        assertEquals(methodInfos.get(1).toString(), "private int getNumber()");
        assertEquals(methodInfos.get(2).toString(), "public List<String> getList()");
    }
}
