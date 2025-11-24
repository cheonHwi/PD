package Analyzer;

import Analyzer.Model.FileNode;
import ProgressBar.ProgressBar;
import SourceParser.Lexer.Lexer;
import SourceParser.Model.ClassInfo;
import SourceParser.Parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class ProjectAnalyzer {
    private final FileAnalyzer fileAnalyzer;
    private final boolean showProgress;

    public ProjectAnalyzer() {
        this(true);
    }

    public ProjectAnalyzer(boolean showProgress) {
        this.fileAnalyzer = new FileAnalyzer();
        this.showProgress = showProgress;
    }

    public List<ClassInfo> analyzeProject() {
        List<FileNode> javaFiles = fileAnalyzer.getAllJavaFiles();
        List<ClassInfo> classes = new ArrayList<>();

        if (showProgress && !javaFiles.isEmpty()) {
            ProgressBar progressBar = new ProgressBar(javaFiles.size());

            for (int i = 0; i < javaFiles.size(); i++) {
                FileNode file = javaFiles.get(i);
                try {
                    ClassInfo classInfo = parseFile(file);
                    classes.add(classInfo);
                } catch (Exception ignored) {
                }
                progressBar.update(i + 1);
            }

            progressBar.finish();
        } else {
            for (FileNode file : javaFiles) {
                try {
                    ClassInfo classInfo = parseFile(file);
                    classes.add(classInfo);
                } catch (Exception ignored) {
                }
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