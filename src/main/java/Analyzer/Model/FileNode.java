package Analyzer.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileNode {
    private String name;
    private String path;
    private boolean isDirectory;
    private List<FileNode> children;

    public FileNode(String name, String path, boolean isDirectory) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.children = new ArrayList<>();
    }

    public void addChild(FileNode child) {
        this.children.add(child);
    }

    public String readContent() throws IOException {
        if (isDirectory) {
            throw new IOException("Cannot read content of directory: " + path);
        }

        Path filePath = Paths.get(path);
        return Files.readString(filePath);
    }

    public boolean isJavaFile() {
        return !isDirectory && name.endsWith(".java");
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public List<FileNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return name + ": " + path;
    }

}
