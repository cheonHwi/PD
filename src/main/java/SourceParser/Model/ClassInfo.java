package SourceParser.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClassInfo {
    String accessModifier;
    List<String> modifiers;
    String className;
    String classType;
    String packageName;
    List<String> imports;
    String extendsClass;
    List<String> implementsList;
    List<MethodInfo> methods;

    public ClassInfo() {
        this.imports = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.modifiers = new ArrayList<>();
        this.implementsList = new ArrayList<>();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(String accessModifier) {
        this.accessModifier = accessModifier;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    public List<String> getImports() {
        return imports;
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

    public List<MethodInfo> getMethods() {
        return methods;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ClassInfo {\n");
        appendPackage(sb);
        appendClassDeclaration(sb);
        appendExtends(sb);
        appendImplements(sb);
        appendImports(sb);
        appendMethods(sb);
        sb.append("}");

        return sb.toString();
    }

    private void appendPackage(StringBuilder sb) {
        if (packageName != null && !packageName.isEmpty()) {
            sb.append("  package: ").append(packageName).append("\n");
        }
    }

    private void appendClassDeclaration(StringBuilder sb) {
        sb.append("  class: ").append(buildClassDeclaration()).append("\n");
    }

    private void appendExtends(StringBuilder sb) {
        if (extendsClass != null && !extendsClass.isEmpty()) {
            sb.append("  extends: ").append(extendsClass).append("\n");
        }
    }

    private void appendImplements(StringBuilder sb) {
        if (!implementsList.isEmpty()) {
            sb.append("  implements: ")
                    .append(String.join(", ", implementsList))
                    .append("\n");
        }
    }

    private void appendImports(StringBuilder sb) {
        if (imports.isEmpty()) return;

        sb.append("  imports: [\n");
        imports.forEach(imp -> sb.append("    ").append(imp).append("\n"));
        sb.append("  ]\n");
    }

    private void appendMethods(StringBuilder sb) {
        if (methods.isEmpty()) return;

        sb.append("  methods: [\n");
        methods.forEach(method ->
                sb.append("    ").append(buildMethodSignature(method)).append("\n")
        );
        sb.append("  ]\n");
    }

    private String buildClassDeclaration() {
        StringBuilder sb = new StringBuilder();

        if (accessModifier != null && !accessModifier.equals("default")) {
            sb.append(accessModifier).append(" ");
        }

        sb.append(classType).append(" ");

        if (!modifiers.isEmpty()) {
            sb.append(String.join(" ", modifiers)).append(" ");
        }

        sb.append(className);

        return sb.toString();
    }

    private String buildMethodSignature(MethodInfo method) {
        StringBuilder sb = new StringBuilder();

        if (method.getMethodAccessModifier() != null) {
            sb.append(method.getMethodAccessModifier()).append(" ");
        }

        sb.append(method.getReturnType())
                .append(" ")
                .append(method.getMethodName())
                .append("(");

        if (!method.getParameters().isEmpty()) {
            String params = method.getParameters().stream()
                    .map(p -> p.getType() + " " + p.getName())
                    .collect(Collectors.joining(", "));
            sb.append(params);
        }

        sb.append(")");

        return sb.toString();
    }
}