package Analyzer;

import SourceParser.Model.ClassInfo;
import SourceParser.Model.MethodCall;
import SourceParser.Model.MethodInfo;

import java.util.*;

public class DependencyResolver {
    private Map<String, ClassInfo> classMap;  // className -> ClassInfo

    public DependencyResolver() {
        this.classMap = new HashMap<>();
    }

    // 모든 클래스의 의존성 분석
    public void resolveDependencies(List<ClassInfo> classes) {
        // 1. 클래스 맵 구축 (빠른 검색을 위해)
        buildClassMap(classes);

        // 2. 각 클래스의 의존성 분석
        for (ClassInfo classInfo : classes) {
            Set<String> dependencies = new HashSet<>();

            // 메서드 호출에서 의존성 추출
            for (MethodInfo method : classInfo.getMethods()) {
                for (MethodCall call : method.getMethodCalls()) {
                    String resolvedClass = resolveClassName(call.getTargetClass(), classInfo);
                    if (resolvedClass != null && !resolvedClass.equals(classInfo.getClassName())) {
                        dependencies.add(resolvedClass);
                    }
                }
            }

            // extends에서 의존성 추가
            if (classInfo.getExtendsClass() != null) {
                dependencies.add(classInfo.getExtendsClass());
            }

            // implements에서 의존성 추가
            if (classInfo.getImplementsList() != null) {
                dependencies.addAll(classInfo.getImplementsList());
            }

            // ClassInfo에 의존성 설정
            classInfo.getDependencies().clear();
            classInfo.getDependencies().addAll(dependencies);
        }
    }

    // 클래스명 맵 구축
    private void buildClassMap(List<ClassInfo> classes) {
        classMap.clear();
        for (ClassInfo classInfo : classes) {
            classMap.put(classInfo.getClassName(), classInfo);
        }
    }

    // targetClass를 실제 클래스명으로 변환
    private String resolveClassName(String targetClass, ClassInfo context) {
        if (targetClass == null) {
            // targetClass가 null이면 같은 클래스 내 호출
            return context.getClassName();
        }

        // "this"는 현재 클래스
        if (targetClass.equals("this")) {
            return context.getClassName();
        }

        // 1. Import 문에서 찾기
        String resolvedFromImport = resolveFromImports(targetClass, context.getImports());
        if (resolvedFromImport != null) {
            return resolvedFromImport;
        }

        // 2. 같은 패키지 내에서 찾기
        String resolvedFromPackage = resolveFromSamePackage(targetClass, context.getPackageName());
        if (resolvedFromPackage != null) {
            return resolvedFromPackage;
        }

        // 3. 그냥 targetClass를 클래스명으로 사용 (카멜케이스를 파스칼케이스로)
        return capitalizeFirstLetter(targetClass);
    }

    // Import 문에서 클래스명 찾기
    private String resolveFromImports(String targetClass, List<String> imports) {
        String targetLower = targetClass.toLowerCase();

        for (String importStatement : imports) {
            // import com.example.UserRepository;
            String[] parts = importStatement.split("\\.");
            if (parts.length > 0) {
                String className = parts[parts.length - 1];

                // userRepository → UserRepository 매칭
                if (className.toLowerCase().equals(targetLower)) {
                    return className;
                }
            }
        }

        return null;
    }

    // 같은 패키지 내에서 클래스 찾기
    private String resolveFromSamePackage(String targetClass, String packageName) {
        String targetPascal = capitalizeFirstLetter(targetClass);

        // 같은 패키지의 클래스 중에서 찾기
        for (ClassInfo classInfo : classMap.values()) {
            if (classInfo.getPackageName() != null &&
                    classInfo.getPackageName().equals(packageName) &&
                    classInfo.getClassName().equals(targetPascal)) {
                return classInfo.getClassName();
            }
        }

        return null;
    }

    // 첫 글자를 대문자로 (userRepository → UserRepository)
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // 의존성 통계 출력
    public void printDependencyStatistics(List<ClassInfo> classes) {
        System.out.println("\n=== Dependency Statistics ===");

        int totalDependencies = classes.stream()
                .mapToInt(c -> c.getDependencies().size())
                .sum();

        System.out.println("Total Dependencies: " + totalDependencies);
        System.out.println("Average Dependencies per Class: " +
                (classes.isEmpty() ? 0 : totalDependencies / classes.size()));

        System.out.println("\nClasses with most dependencies:");
        classes.stream()
                .sorted((a, b) -> Integer.compare(b.getDependencies().size(), a.getDependencies().size()))
                .limit(5)
                .forEach(c -> System.out.println("  " + c.getClassName() + ": " + c.getDependencies().size() + " dependencies"));
    }
}