package SourceParser.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodInfo {
    String methodAccessModifier;
    String methodName;
    String returnType;
    List<Parameter> parameters;
    List<MethodCall> methodCalls;
    int lineNumber;

    public MethodInfo() {
        this.parameters = new ArrayList<>();
        this.methodCalls = new ArrayList<>();
    }

    public String getMethodAccessModifier() {
        return methodAccessModifier;
    }

    public void setMethodAccessModifier(String methodAccessModifier) {
        this.methodAccessModifier = methodAccessModifier;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public List<MethodCall> getMethodCalls() {
        return methodCalls;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        String methodSignature = "";

        if(!parameters.isEmpty()) {
            methodSignature = parameters.stream()
                    .map(Parameter::toString)
                    .collect(Collectors.joining(", "));
        }


        return String.format("%s %s %s(%s)", methodAccessModifier, returnType, methodName, methodSignature);
    }
}
