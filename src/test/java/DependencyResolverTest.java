
import Analyzer.DependencyResolver;
import SourceParser.Model.ClassInfo;
import SourceParser.Model.MethodCall;
import SourceParser.Model.MethodInfo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class DependencyResolverTest {

    private DependencyResolver resolver;

    @BeforeEach
    public void setUp() {
        resolver = new DependencyResolver();
    }

    @Test
    @DisplayName("의존성 분석 - Import에서 해결")
    public void testResolveFromImport() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("UserService");
        classInfo.setPackageName("com.example.service");
        classInfo.getImports().add("com.example.repository.UserRepository");

        MethodInfo method = new MethodInfo();
        method.setMethodName("getUsers");
        MethodCall call = new MethodCall("userRepository", "findAll", 10);
        method.getMethodCalls().add(call);
        classInfo.getMethods().add(method);

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        resolver.resolveDependencies(classes);

        assertTrue(classInfo.getDependencies().contains("UserRepository"));
    }

    @Test
    @DisplayName("의존성 분석 - extends 의존성")
    public void testResolveExtendsDependency() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("UserService");
        classInfo.setExtendsClass("BaseService");

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        resolver.resolveDependencies(classes);

        assertTrue(classInfo.getDependencies().contains("BaseService"));
    }

    @Test
    @DisplayName("의존성 분석 - implements 의존성")
    public void testResolveImplementsDependency() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("UserService");
        classInfo.getImplementsList().add("Service");
        classInfo.getImplementsList().add("Serializable");

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        resolver.resolveDependencies(classes);

        assertTrue(classInfo.getDependencies().contains("Service"));
        assertTrue(classInfo.getDependencies().contains("Serializable"));
    }

    @Test
    @DisplayName("의존성 분석 - this 호출 제외")
    public void testResolveExcludeThis() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("UserService");

        MethodInfo method = new MethodInfo();
        method.setMethodName("process");
        MethodCall call = new MethodCall("this", "validate", 10);
        method.getMethodCalls().add(call);
        classInfo.getMethods().add(method);

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        resolver.resolveDependencies(classes);

        assertFalse(classInfo.getDependencies().contains("UserService"));
    }

    @Test
    @DisplayName("의존성 분석 - 같은 클래스 호출 제외")
    public void testResolveExcludeSameClass() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("UserService");

        MethodInfo method = new MethodInfo();
        method.setMethodName("process");
        MethodCall call = new MethodCall(null, "validate", 10);
        method.getMethodCalls().add(call);
        classInfo.getMethods().add(method);

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        resolver.resolveDependencies(classes);

        assertFalse(classInfo.getDependencies().contains("UserService"));
    }

    @Test
    @DisplayName("의존성 분석 - 여러 의존성 종합")
    public void testResolveMultipleDependencies() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("UserService");
        classInfo.setExtendsClass("BaseService");
        classInfo.getImplementsList().add("Service");
        classInfo.getImports().add("com.example.repository.UserRepository");

        MethodInfo method = new MethodInfo();
        method.setMethodName("process");
        MethodCall call1 = new MethodCall("userRepository", "findAll", 10);
        MethodCall call2 = new MethodCall("this", "validate", 11);
        method.getMethodCalls().add(call1);
        method.getMethodCalls().add(call2);
        classInfo.getMethods().add(method);

        List<ClassInfo> classes = new ArrayList<>();
        classes.add(classInfo);

        resolver.resolveDependencies(classes);

        assertTrue(classInfo.getDependencies().contains("BaseService"));
        assertTrue(classInfo.getDependencies().contains("Service"));
        assertTrue(classInfo.getDependencies().contains("UserRepository"));
        assertFalse(classInfo.getDependencies().contains("UserService"));
    }
}