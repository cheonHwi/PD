package SourceParser.Model;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo {
    String methodName;
    String returnType;
    List<Parameter> parameters;
    List<MethodCall> methodCalls;  // 이 메서드가 호출하는 다른 메서드들
    int lineNumber;

    public MethodInfo() {
        this.parameters = new ArrayList<>();
        this.methodCalls = new ArrayList<>();
    }
}
