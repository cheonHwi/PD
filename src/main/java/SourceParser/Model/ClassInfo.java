package SourceParser.Model;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    String accessModifier;
    List<String> modifiers;
    String className;
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
}
