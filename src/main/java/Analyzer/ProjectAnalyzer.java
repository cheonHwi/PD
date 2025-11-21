package Analyzer;

import Analyzer.Model.FileNode;
import SourceParser.Lexer.Lexer;
import SourceParser.Model.ClassInfo;
import SourceParser.Parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class ProjectAnalyzer {
    private final FileAnalyzer fileAnalyzer;

    public ProjectAnalyzer() {
        this.fileAnalyzer = new FileAnalyzer();
    }

    public List<ClassInfo> analyzeProject() {
        List<FileNode> javaFiles = fileAnalyzer.getAllJavaFiles();
        List<ClassInfo> classes = new ArrayList<>();

        for (FileNode file : javaFiles) {
            try {
                ClassInfo classInfo = parseFile(file);
                classes.add(classInfo);
            } catch (Exception e) {
                System.err.println("Failed to parse: " + file.getName());
            }
        }

        return classes;
    }

    private ClassInfo parseFile(FileNode file) throws Exception {
        String content = file.readContent();
        Lexer lexer = new Lexer(content);
        Parser parser = new Parser(lexer);
        return parser.getClassInfo();
    }
}