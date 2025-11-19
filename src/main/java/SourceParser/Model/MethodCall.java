package SourceParser.Model;

public class MethodCall {
    String targetClass;
    String targetMethod;
    int lineNumber;

    public MethodCall(String targetClass, String targetMethod, int lineNumber) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.lineNumber = lineNumber;
    }
}
