package Analyzer;

import Analyzer.Model.FileNode;
import SourceParser.Lexer.Lexer;
import SourceParser.Model.ClassInfo;

import ProgressBar.ProgressBar;
import SourceParser.Parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class ProjectAnalyzer {
    private final FileAnalyzer fileAnalyzer;
    private final boolean showProgress;
    private final List<ParseResult> parseResults;

    public ProjectAnalyzer() {
        this(true);
    }

    public ProjectAnalyzer(boolean showProgress) {
        this.fileAnalyzer = new FileAnalyzer();
        this.showProgress = showProgress;
        this.parseResults = new ArrayList<>();
    }

    public List<ClassInfo> analyzeProject() {
        List<FileNode> javaFiles = fileAnalyzer.getAllJavaFiles();
        List<ClassInfo> classes = new ArrayList<>();
        parseResults.clear();

        if (showProgress && !javaFiles.isEmpty()) {
            ProgressBar progressBar = new ProgressBar(javaFiles.size());

            for (int i = 0; i < javaFiles.size(); i++) {
                FileNode file = javaFiles.get(i);
                try {
                    ClassInfo classInfo = parseFile(file);
                    classes.add(classInfo);
                    parseResults.add(new ParseResult(
                            file.getName(),
                            file.getPath(),
                            true
                    ));
                } catch (Exception e) {
                    parseResults.add(new ParseResult(
                            file.getName(),
                            file.getPath(),
                            e.getMessage(),
                            e
                    ));
                }
                progressBar.update(i + 1);
            }

            progressBar.finish();
        } else {
            for (FileNode file : javaFiles) {
                try {
                    ClassInfo classInfo = parseFile(file);
                    classes.add(classInfo);
                    parseResults.add(new ParseResult(
                            file.getName(),
                            file.getPath(),
                            true
                    ));
                } catch (Exception e) {
                    parseResults.add(new ParseResult(
                            file.getName(),
                            file.getPath(),
                            e.getMessage(),
                            e
                    ));
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

    public List<ParseResult> getParseResults() {
        return parseResults;
    }

    public int getSuccessCount() {
        return (int) parseResults.stream().filter(ParseResult::isSuccess).count();
    }

    public int getFailureCount() {
        return (int) parseResults.stream().filter(r -> !r.isSuccess()).count();
    }

    public List<ParseResult> getFailures() {
        return parseResults.stream()
                .filter(r -> !r.isSuccess())
                .toList();
    }
}