package SourceParser.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassInfo {
    private String filePath;
    private String packageName;
    private String className;
    private String classType;
    private String accessModifier;
    private List<String> modifiers;
    private String extendsClass;
    private List<String> implementsList;
    private List<String> imports;
    private List<MethodInfo> methods;
    private List<String> dependencies;

    public ClassInfo() {
        this.modifiers = new ArrayList<>();
        this.implementsList = new ArrayList<>();
        this.imports = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.dependencies = new ArrayList<>();
    }

    // Getters and Setters
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(String accessModifier) {
        this.accessModifier = accessModifier;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    public String getExtendsClass() {
        return extendsClass;
    }

    public void setExtendsClass(String extendsClass) {
        this.extendsClass = extendsClass;
    }

    public List<String> getImplementsList() {
        return implementsList;
    }

    public void setImplementsList(List<String> implementsList) {
        this.implementsList = implementsList;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");
        appendPackage(sb);
        appendClassDeclaration(sb);
        appendExtends(sb);
        appendImplements(sb);
        appendImports(sb);
        appendMethods(sb);
        appendDependencies(sb);
        sb.append("}");

        return sb.toString();
    }

    private void appendPackage(StringBuilder sb) {
        if (packageName != null && !packageName.isEmpty()) {
            sb.append("  \"package\": \"").append(packageName).append("\",\n");
        }
    }

    private void appendClassDeclaration(StringBuilder sb) {
        sb.append("  \"className\": \"").append(className).append("\"");

        if (accessModifier != null && !accessModifier.equals("default")) {
            sb.append(",\n  \"accessModifier\": \"").append(accessModifier).append("\"");
        }

        if (!modifiers.isEmpty()) {
            sb.append(",\n  \"modifiers\": [");
            sb.append(modifiers.stream()
                    .map(m -> "\"" + m + "\"")
                    .collect(Collectors.joining(", ")));
            sb.append("]");
        }
    }

    private void appendExtends(StringBuilder sb) {
        if (extendsClass != null && !extendsClass.isEmpty()) {
            sb.append(",\n  \"extends\": \"").append(extendsClass).append("\"");
        }
    }

    private void appendImplements(StringBuilder sb) {
        if (!implementsList.isEmpty()) {
            sb.append(",\n  \"implements\": [");
            sb.append(implementsList.stream()
                    .map(i -> "\"" + i + "\"")
                    .collect(Collectors.joining(", ")));
            sb.append("]");
        }
    }

    private void appendImports(StringBuilder sb) {
        if (imports.isEmpty()) return;

        sb.append(",\n  \"imports\": [\n");
        sb.append(imports.stream()
                .map(imp -> "    \"" + imp + "\"")
                .collect(Collectors.joining(",\n")));
        sb.append("\n  ]");
    }

    private void appendMethods(StringBuilder sb) {
        if (methods.isEmpty()) return;

        sb.append(",\n  \"methods\": [\n");
        sb.append(methods.stream()
                .map(this::buildMethodJson)
                .collect(Collectors.joining(",\n")));
        sb.append("\n  ]");
    }

    private void appendDependencies(StringBuilder sb) {
        if (dependencies.isEmpty()) return;

        sb.append(",\n  \"dependencies\": [");
        sb.append(dependencies.stream()
                .map(d -> "\"" + d + "\"")
                .collect(Collectors.joining(", ")));
        sb.append("]");
    }

    private String buildMethodJson(MethodInfo method) {
        StringBuilder sb = new StringBuilder();
        sb.append("    {\n");
        sb.append("      \"name\": \"").append(method.getMethodName()).append("\",\n");

        if (method.getMethodAccessModifier() != null) {
            sb.append("      \"accessModifier\": \"").append(method.getMethodAccessModifier()).append("\",\n");
        }

        sb.append("      \"returnType\": \"").append(method.getReturnType()).append("\"");

        if (!method.getParameters().isEmpty()) {
            sb.append(",\n      \"parameters\": [");
            sb.append(method.getParameters().stream()
                    .map(p -> "{\"type\": \"" + p.getType() + "\", \"name\": \"" + p.getName() + "\"}")
                    .collect(Collectors.joining(", ")));
            sb.append("]");
        }

        if (!method.getMethodCalls().isEmpty()) {
            sb.append(",\n      \"calls\": [\n");
            sb.append(method.getMethodCalls().stream()
                    .map(call -> "        {\"targetClass\": " +
                            (call.getTargetClass() != null ? "\"" + call.getTargetClass() + "\"" : "null") +
                            ", \"targetMethod\": \"" + call.getTargetMethod() + "\", \"line\": " + call.getLineNumber() + "}")
                    .collect(Collectors.joining(",\n")));
            sb.append("\n      ]");
        }

        sb.append("\n    }");
        return sb.toString();
    }
}