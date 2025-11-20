import SourceParser.Lexer.Lexer;
import SourceParser.Model.MethodInfo;
import SourceParser.Model.Parameter;
import SourceParser.Parser.Parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class MethodParserTest {

    @Test
    @DisplayName("파서 간단한 메서드 시그니처 파싱 검증")
    public void testParseSimpleMethod() {
        String code = "public void test(int arg) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("test", method.getMethodName());
        assertEquals("void", method.getReturnType());
        assertEquals(1, method.getParameters().size());
        assertEquals("int", method.getParameters().getFirst().getType());
        assertEquals("arg", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 static 메서드 파싱 검증")
    public void testParseStaticMethod() {
        String code = "public static int calculate(double x) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("calculate", method.getMethodName());
        assertEquals("int", method.getReturnType());
        assertEquals(1, method.getParameters().size());
        assertEquals("double", method.getParameters().getFirst().getType());
        assertEquals("x", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 여러 파라미터 메서드 파싱 검증")
    public void testParseMultipleParameters() {
        String code = "public void process(String name, int age, double salary) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("process", method.getMethodName());
        assertEquals("void", method.getReturnType());
        assertEquals(3, method.getParameters().size());

        Parameter param1 = method.getParameters().getFirst();
        assertEquals("String", param1.getType());
        assertEquals("name", param1.getName());

        Parameter param2 = method.getParameters().get(1);
        assertEquals("int", param2.getType());
        assertEquals("age", param2.getName());

        Parameter param3 = method.getParameters().get(2);
        assertEquals("double", param3.getType());
        assertEquals("salary", param3.getName());
    }

    @Test
    @DisplayName("파서 파라미터 없는 메서드 파싱 검증")
    public void testParseNoParameters() {
        String code = "private void execute() {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("execute", method.getMethodName());
        assertEquals("void", method.getReturnType());
        assertEquals(0, method.getParameters().size());
    }

    @Test
    @DisplayName("파서 배열 타입 파라미터 파싱 검증")
    public void testParseArrayParameter() {
        String code = "public static void main(String[] args) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("main", method.getMethodName());
        assertEquals("void", method.getReturnType());
        assertEquals(1, method.getParameters().size());
        assertEquals("String[]", method.getParameters().getFirst().getType());
        assertEquals("args", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 다차원 배열 타입 파라미터 파싱 검증")
    public void testParseMultiDimensionalArray() {
        String code = "public void process(int[][] matrix, String[] names) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("process", method.getMethodName());
        assertEquals(2, method.getParameters().size());
        assertEquals("int[][]", method.getParameters().getFirst().getType());
        assertEquals("matrix", method.getParameters().getFirst().getName());
        assertEquals("String[]", method.getParameters().get(1).getType());
        assertEquals("names", method.getParameters().get(1).getName());
    }

    @Test
    @DisplayName("파서 제네릭 타입 파라미터 파싱 검증")
    public void testParseGenericParameter() {
        String code = "public void process(List<String> items) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("process", method.getMethodName());
        assertEquals("void", method.getReturnType());
        assertEquals(1, method.getParameters().size());
        assertEquals("List<String>", method.getParameters().getFirst().getType());
        assertEquals("items", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 다중 제네릭 타입 파라미터 파싱 검증")
    public void testParseMultipleGenericParameters() {
        String code = "public void update(Map<String, Integer> data) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("update", method.getMethodName());
        assertEquals(1, method.getParameters().size());
        assertEquals("Map<String, Integer>", method.getParameters().getFirst().getType());
        assertEquals("data", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 중첩 제네릭 타입 파라미터 파싱 검증")
    public void testParseNestedGeneric() {
        String code = "public void complex(List<List<String>> nested) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("complex", method.getMethodName());
        assertEquals(1, method.getParameters().size());
        assertEquals("List<List<String>>", method.getParameters().getFirst().getType());
        assertEquals("nested", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 와일드카드 extends 제네릭 파싱 검증")
    public void testParseWildcardExtends() {
        String code = "public void bounded(List<? extends Number> numbers) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("bounded", method.getMethodName());
        assertEquals(1, method.getParameters().size());
        assertEquals("List<? extends Number>", method.getParameters().getFirst().getType());
        assertEquals("numbers", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 와일드카드 super 제네릭 파싱 검증")
    public void testParseWildcardSuper() {
        String code = "public void add(List<? super Integer> list) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("add", method.getMethodName());
        assertEquals(1, method.getParameters().size());
        assertEquals("List<? super Integer>", method.getParameters().getFirst().getType());
        assertEquals("list", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 복잡한 제네릭 조합 파싱 검증")
    public void testParseComplexGenerics() {
        String code = "public Map<String, List<Integer>> transform(Map<String, List<? extends Number>> input) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("transform", method.getMethodName());
        assertEquals("Map<String, List<Integer>>", method.getReturnType());
        assertEquals(1, method.getParameters().size());
        assertEquals("Map<String, List<? extends Number>>", method.getParameters().getFirst().getType());
        assertEquals("input", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 제네릭 배열 타입 파싱 검증")
    public void testParseGenericArrayType() {
        String code = "public void arrays(List<String>[] items) {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("arrays", method.getMethodName());
        assertEquals(1, method.getParameters().size());
        assertEquals("List<String>[]", method.getParameters().getFirst().getType());
        assertEquals("items", method.getParameters().getFirst().getName());
    }

    @Test
    @DisplayName("파서 복합 수식어 메서드 파싱 검증")
    public void testParseComplexModifiers() {
        String code = "protected static final int getValue() {}";
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);

        MethodInfo method = parser.getMethodInfo();

        assertNotNull(method);
        assertEquals("getValue", method.getMethodName());
        assertEquals("int", method.getReturnType());
        assertEquals(0, method.getParameters().size());
    }
}