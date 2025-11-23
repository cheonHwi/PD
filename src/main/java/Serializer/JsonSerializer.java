package Serializer;

import SourceParser.Model.ClassInfo;
import SourceParser.Model.MethodCall;
import SourceParser.Model.MethodInfo;
import SourceParser.Model.Parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class JsonSerializer {

    public String serialize(List<ClassInfo> classes) {
        StringBuilder json = new StringBuilder();

        json.append("{\n");
        json.append("  \"projectName\": \"").append(getProjectName()).append("\",\n");
        json.append("  \"analyzedAt\": \"").append(getCurrentTimestamp()).append("\",\n");
        json.append("  \"totalClasses\": ").append(classes.size()).append(",\n");
        json.append("  \"classes\": [\n");

        for (int i = 0; i < classes.size(); i++) {
            json.append(serializeClass(classes.get(i), 2));
            if (i < classes.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ]\n");
        json.append("}");

        return json.toString();
    }

    private String serializeClass(ClassInfo classInfo, int indentLevel) {
        StringBuilder json = new StringBuilder();
        String indent = "  ".repeat(indentLevel);

        json.append(indent).append("{\n");

        // Package
        if (classInfo.getPackageName() != null && !classInfo.getPackageName().isEmpty()) {
            json.append(indent).append("  \"package\": \"").append(escape(classInfo.getPackageName())).append("\",\n");
        }

        // Class name
        json.append(indent).append("  \"className\": \"").append(escape(classInfo.getClassName())).append("\"");

        // Access modifier
        if (classInfo.getAccessModifier() != null) {
            json.append(",\n").append(indent).append("  \"accessModifier\": \"").append(escape(classInfo.getAccessModifier())).append("\"");
        }

        // Modifiers
        if (classInfo.getModifiers() != null && !classInfo.getModifiers().isEmpty()) {
            json.append(",\n").append(indent).append("  \"modifiers\": ");
            json.append(serializeStringList(classInfo.getModifiers()));
        }

        // Extends
        if (classInfo.getExtendsClass() != null && !classInfo.getExtendsClass().isEmpty()) {
            json.append(",\n").append(indent).append("  \"extends\": \"").append(escape(classInfo.getExtendsClass())).append("\"");
        }

        // Implements
        if (classInfo.getImplementsList() != null && !classInfo.getImplementsList().isEmpty()) {
            json.append(",\n").append(indent).append("  \"implements\": ");
            json.append(serializeStringList(classInfo.getImplementsList()));
        }

        // Imports
        if (classInfo.getImports() != null && !classInfo.getImports().isEmpty()) {
            json.append(",\n").append(indent).append("  \"imports\": ");
            json.append(serializeStringList(classInfo.getImports()));
        }

        // Methods
        if (classInfo.getMethods() != null && !classInfo.getMethods().isEmpty()) {
            json.append(",\n").append(indent).append("  \"methods\": [\n");
            List<MethodInfo> methods = classInfo.getMethods();
            for (int i = 0; i < methods.size(); i++) {
                json.append(serializeMethod(methods.get(i), indentLevel + 2));
                if (i < methods.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append(indent).append("  ]");
        }

        // Dependencies
        if (classInfo.getDependencies() != null && !classInfo.getDependencies().isEmpty()) {
            json.append(",\n").append(indent).append("  \"dependencies\": ");
            json.append(serializeStringList(classInfo.getDependencies()));
        }

        json.append("\n").append(indent).append("}");

        return json.toString();
    }

    // MethodInfo를 JSON으로 변환
    private String serializeMethod(MethodInfo method, int indentLevel) {
        StringBuilder json = new StringBuilder();
        String indent = "  ".repeat(indentLevel);

        json.append(indent).append("{\n");
        json.append(indent).append("  \"name\": \"").append(escape(method.getMethodName())).append("\",\n");

        if (method.getMethodAccessModifier() != null) {
            json.append(indent).append("  \"accessModifier\": \"").append(escape(method.getMethodAccessModifier())).append("\",\n");
        }

        json.append(indent).append("  \"returnType\": \"").append(escape(method.getReturnType())).append("\",\n");
        json.append(indent).append("  \"lineNumber\": ").append(method.getLineNumber());

        // Parameters
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            json.append(",\n").append(indent).append("  \"parameters\": [\n");
            List<Parameter> params = method.getParameters();
            for (int i = 0; i < params.size(); i++) {
                json.append(serializeParameter(params.get(i), indentLevel + 2));
                if (i < params.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append(indent).append("  ]");
        }

        // Method calls
        if (method.getMethodCalls() != null && !method.getMethodCalls().isEmpty()) {
            json.append(",\n").append(indent).append("  \"calls\": [\n");
            List<MethodCall> calls = method.getMethodCalls();
            for (int i = 0; i < calls.size(); i++) {
                json.append(serializeMethodCall(calls.get(i), indentLevel + 2));
                if (i < calls.size() - 1) {
                    json.append(",");
                }
                json.append("\n");
            }
            json.append(indent).append("  ]");
        }

        json.append("\n").append(indent).append("}");

        return json.toString();
    }

    // Parameter를 JSON으로 변환
    private String serializeParameter(Parameter param, int indentLevel) {
        String indent = "  ".repeat(indentLevel);
        return indent + "{\"type\": \"" + escape(param.getType()) + "\", \"name\": \"" + escape(param.getName()) + "\"}";
    }

    // MethodCall을 JSON으로 변환
    private String serializeMethodCall(MethodCall call, int indentLevel) {
        StringBuilder json = new StringBuilder();
        String indent = "  ".repeat(indentLevel);

        json.append(indent).append("{\n");

        if (call.getTargetClass() != null) {
            json.append(indent).append("  \"targetClass\": \"").append(escape(call.getTargetClass())).append("\",\n");
        } else {
            json.append(indent).append("  \"targetClass\": null,\n");
        }

        json.append(indent).append("  \"targetMethod\": \"").append(escape(call.getTargetMethod())).append("\",\n");
        json.append(indent).append("  \"line\": ").append(call.getLineNumber()).append("\n");
        json.append(indent).append("}");

        return json.toString();
    }

    // String 리스트를 JSON 배열로 변환
    private String serializeStringList(List<String> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < list.size(); i++) {
            json.append("\"").append(escape(list.get(i))).append("\"");
            if (i < list.size() - 1) {
                json.append(", ");
            }
        }
        json.append("]");
        return json.toString();
    }

    // JSON 파일로 저장
    public void saveToFile(String json, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.writeString(path, json);
        System.out.println("JSON saved to: " + path.toAbsolutePath());
    }

    // 현재 디렉토리 이름을 프로젝트명으로 사용
    private String getProjectName() {
        String currentDir = System.getProperty("user.dir");
        Path path = Paths.get(currentDir);
        return path.getFileName().toString();
    }

    // 현재 시간 타임스탬프
    private String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return now.format(formatter);
    }

    // JSON 특수문자 이스케이프
    private String escape(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}