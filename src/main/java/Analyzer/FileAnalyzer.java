package Analyzer;

import Analyzer.Model.FileNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAnalyzer {
    public FileNode buildFileTree() {
        String currentPath = System.getProperty("user.dir");
        File rootDir = new File(currentPath);
        return buildFileTree(rootDir);
    }

    public FileNode buildFileTree(String path) {
        File rootDir = new File(path);
        return buildFileTree(rootDir);
    }

    private FileNode buildFileTree(File file) {
        FileNode node = new FileNode(
                file.getName(),
                file.getAbsolutePath(),
                file.isDirectory()
        );

        if (file.isDirectory()) {
            File[] children = file.listFiles();

            if (children != null) {
                for (File child : children) {
                    if (!child.isHidden()) {
                        FileNode childNode = buildFileTree(child);
                        node.addChild(childNode);
                    }
                }
            }
        }

        return node;
    }

    public FileNode buildJavaFileTree() {
        String currentPath = System.getProperty("user.dir");
        File rootDir = new File(currentPath);
        return buildJavaFileTree(rootDir);
    }

    private FileNode buildJavaFileTree(File file) {
        FileNode node = new FileNode(
                file.getName(),
                file.getAbsolutePath(),
                file.isDirectory()
        );

        if (file.isDirectory()) {
            File[] children = file.listFiles();

            if (children != null) {
                for (File child : children) {
                    if (child.isHidden()) continue;

                    if (child.isDirectory() || child.getName().endsWith(".java")) {
                        FileNode childNode = buildJavaFileTree(child);

                        if (!child.isDirectory() || !childNode.getChildren().isEmpty()) {
                            node.addChild(childNode);
                        }
                    }
                }
            }
        }

        return node;
    }

    public List<FileNode> getAllJavaFiles() {
        FileNode root = buildJavaFileTree();
        List<FileNode> javaFiles = new ArrayList<>();
        collectJavaFiles(root, javaFiles);
        return javaFiles;
    }

    private void collectJavaFiles(FileNode node, List<FileNode> javaFiles) {
        if (node.isJavaFile()) {
            javaFiles.add(node);
        }

        for (FileNode child : node.getChildren()) {
            collectJavaFiles(child, javaFiles);
        }
    }

    public String readJavaFile(FileNode fileNode) throws IOException {
        if (!fileNode.isJavaFile()) {
            throw new IOException("Not a Java file: " + fileNode.getPath());
        }
        return fileNode.readContent();
    }
}
