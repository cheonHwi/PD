package SourceParser.Model;

import java.util.ArrayList;
import java.util.List;

public class ClassInfo {
    String className;
    String packageName;
    List<MethodInfo> methods;
    List<String> dependencies;  // 이 클래스가 의존하는 다른 클래스들

    public ClassInfo() {
        this.methods = new ArrayList<>();
        this.dependencies = new ArrayList<>();
    }
}
