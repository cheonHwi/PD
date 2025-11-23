import Serializer.JsonSerializer;
import SourceParser.Model.ClassInfo;
import SourceParser.Model.MethodCall;
import SourceParser.Model.MethodInfo;
import SourceParser.Model.Parameter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class JsonSerializerTest {

    private JsonSerializer serializer;

    @BeforeEach
    public void setUp() {
        serializer = new JsonSerializer();
    }

    @Test
    @DisplayName("JSON 직렬화 - 기본 클래스 정보")
    public void testSerializeBasicClass() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("TestClass");
        classInfo.setPackageName("com.example");

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        String json = serializer.serialize(classes);

        assertNotNull(json);
        assertTrue(json.contains("\"className\": \"TestClass\""));
        assertTrue(json.contains("\"package\": \"com.example\""));
    }

    @Test
    @DisplayName("JSON 직렬화 - 메서드 포함")
    public void testSerializeWithMethods() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("TestClass");

        MethodInfo method = new MethodInfo();
        method.setMethodName("testMethod");
        method.setReturnType("void");
        method.setMethodAccessModifier("public");
        classInfo.getMethods().add(method);

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        String json = serializer.serialize(classes);

        assertNotNull(json);
        assertTrue(json.contains("\"name\": \"testMethod\""));
        assertTrue(json.contains("\"returnType\": \"void\""));
        assertTrue(json.contains("\"accessModifier\": \"public\""));
    }

    @Test
    @DisplayName("JSON 직렬화 - 파라미터 포함")
    public void testSerializeWithParameters() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("TestClass");

        MethodInfo method = new MethodInfo();
        method.setMethodName("testMethod");
        method.setReturnType("void");
        method.getParameters().add(new Parameter("String", "arg1"));
        method.getParameters().add(new Parameter("int", "arg2"));
        classInfo.getMethods().add(method);

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        String json = serializer.serialize(classes);

        assertNotNull(json);
        assertTrue(json.contains("\"parameters\""));
        assertTrue(json.contains("\"type\": \"String\""));
        assertTrue(json.contains("\"name\": \"arg1\""));
        assertTrue(json.contains("\"type\": \"int\""));
        assertTrue(json.contains("\"name\": \"arg2\""));
    }

    @Test
    @DisplayName("JSON 직렬화 - 메서드 호출 포함")
    public void testSerializeWithMethodCalls() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("TestClass");

        MethodInfo method = new MethodInfo();
        method.setMethodName("testMethod");
        method.setReturnType("void");
        method.getMethodCalls().add(new MethodCall("service", "process", 10));
        classInfo.getMethods().add(method);

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        String json = serializer.serialize(classes);

        assertNotNull(json);
        assertTrue(json.contains("\"calls\""));
        assertTrue(json.contains("\"targetClass\": \"service\""));
        assertTrue(json.contains("\"targetMethod\": \"process\""));
        assertTrue(json.contains("\"line\": 10"));
    }

    @Test
    @DisplayName("JSON 직렬화 - 의존성 포함")
    public void testSerializeWithDependencies() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("TestClass");
        classInfo.getDependencies().add("ServiceA");
        classInfo.getDependencies().add("ServiceB");

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        String json = serializer.serialize(classes);

        assertNotNull(json);
        assertTrue(json.contains("\"dependencies\""));
        assertTrue(json.contains("\"ServiceA\""));
        assertTrue(json.contains("\"ServiceB\""));
    }

    @Test
    @DisplayName("JSON 직렬화 - 여러 클래스")
    public void testSerializeMultipleClasses() {
        ClassInfo class1 = new ClassInfo();
        class1.setClassName("Class1");

        ClassInfo class2 = new ClassInfo();
        class2.setClassName("Class2");

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(class1);
        classes.add(class2);

        String json = serializer.serialize(classes);

        assertNotNull(json);
        assertTrue(json.contains("\"totalClasses\": 2"));
        assertTrue(json.contains("\"Class1\""));
        assertTrue(json.contains("\"Class2\""));
    }

    @Test
    @DisplayName("JSON 직렬화 - 특수문자 이스케이프")
    public void testSerializeEscapeSpecialChars() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("Test\"Class");

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        String json = serializer.serialize(classes);

        assertNotNull(json);
        assertTrue(json.contains("Test\\\"Class"));
    }
}