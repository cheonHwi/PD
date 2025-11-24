package Analyzer;

public class ParseResult {
    private final String fileName;
    private final String filePath;
    private final boolean success;
    private String errorMessage;
    private Exception exception;

    public ParseResult(String fileName, String filePath, boolean success) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.success = success;
    }

    public ParseResult(String fileName, String filePath, String errorMessage, Exception exception) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.success = false;
        this.errorMessage = errorMessage;
        this.exception = exception;
    }

    // Getters
    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Exception getException() {
        return exception;
    }
}