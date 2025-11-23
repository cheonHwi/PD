package SourceParser.Model;

public class MethodCall {
    private String targetClass;   // 호출 대상 클래스/객체 (예: userService, this, null)
    private String targetMethod;  // 호출 대상 메서드 (예: getUsers)
    private int lineNumber;       // 호출 위치

    public MethodCall(String targetClass, String targetMethod, int lineNumber) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.lineNumber = lineNumber;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        if (targetClass == null) {
            return targetMethod + "() at line " + lineNumber;
        }
        return targetClass + "." + targetMethod + "() at line " + lineNumber;
    }
}