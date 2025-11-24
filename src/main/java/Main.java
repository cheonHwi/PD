
import Analyzer.DependencyResolver;
import Analyzer.ProjectAnalyzer;
import Serializer.JsonSerializer;
import SourceParser.Model.ClassInfo;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {

    private static final String VERSION = "1.0.0";

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        String command = args[0];

        switch (command) {
            case "-h":
            case "--help":
                printUsage();
                break;

            case "-v":
            case "--version":
                System.out.println("SourceParser v" + VERSION);
                break;

            default:
                analyzeProject(command);
                break;
        }
    }

    private static void analyzeProject(String path) {
        long startTime = System.currentTimeMillis();

        try {
            // 경로 검증
            File projectDir = new File(path);
            if (!projectDir.exists()) {
                System.err.println("✗ Error: Path does not exist: " + path);
                System.exit(1);
            }

            if (!projectDir.isDirectory()) {
                System.err.println("✗ Error: Path is not a directory: " + path);
                System.exit(1);
            }

            // 프로젝트명 추출
            String projectName = extractProjectName(projectDir, path);

            // 작업 디렉토리 변경
            String originalDir = System.getProperty("user.dir");
            System.setProperty("user.dir", projectDir.getAbsolutePath());

            System.out.println("📂 Analyzing: " + projectDir.getAbsolutePath());
            System.out.println("📦 Project: " + projectName);
            System.out.println();

            // 1. 프로젝트 분석
            System.out.print("🔍 Parsing files... ");
            ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(true);
            List<ClassInfo> classes = projectAnalyzer.analyzeProject();

            if (classes.isEmpty()) {
                System.out.println("\n⚠ No Java files found");
                System.exit(0);
            }

            // 2. 의존성 분석
            System.out.print("🔗 Resolving dependencies... ");
            DependencyResolver resolver = new DependencyResolver();
            resolver.resolveDependencies(classes);

            int totalDeps = classes.stream()
                    .mapToInt(c -> c.getDependencies().size())
                    .sum();
            System.out.println("✓");

            // 3. JSON 생성
            System.out.print("📝 Generating JSON... ");
            JsonSerializer serializer = new JsonSerializer();
            String json = serializer.serialize(classes);

            // 원래 위치로 복귀
            System.setProperty("user.dir", originalDir);

            // 파일명 생성: 프로젝트명-현재시간.json
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss"));
            String outputFileName = projectName + "-" + timestamp + ".json";

            serializer.saveToFile(json, outputFileName);
            System.out.println("✓");

            // 결과 요약
            long duration = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println();
            System.out.println("✨ Analysis complete!");
            System.out.println();
            System.out.println("📊 Statistics:");
            System.out.println("   Project:      " + projectName);
            System.out.println("   Classes:      " + classes.size());

            int totalMethods = classes.stream()
                    .mapToInt(c -> c.getMethods().size())
                    .sum();
            System.out.println("   Methods:      " + totalMethods);
            System.out.println("   Dependencies: " + totalDeps);
            System.out.println("   Time:         " + duration + "s");
            System.out.println();
            System.out.println("📄 Output: " + new File(outputFileName).getAbsolutePath());

        } catch (Exception e) {
            System.err.println("\n✗ Error: " + e.getMessage());
            if (System.getenv("DEBUG") != null) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }

    private static String extractProjectName(File projectDir, String originalPath) {
        String projectName;

        // "." 또는 "./" 입력 시
        if (originalPath.equals(".") || originalPath.equals("./")) {
            try {
                projectName = projectDir.getCanonicalFile().getName();
            } catch (Exception e) {
                projectName = projectDir.getAbsoluteFile().getName();
            }
        }
        // 상대 경로 또는 절대 경로
        else {
            projectName = projectDir.getName();

            if (projectName.isEmpty()) {
                try {
                    projectName = projectDir.getCanonicalFile().getName();
                } catch (Exception e) {
                    projectName = "project";
                }
            }
        }

        // 파일명에 사용할 수 없는 문자 제거
        projectName = sanitizeFileName(projectName);

        // 여전히 비어있으면 기본값
        if (projectName.isEmpty()) {
            projectName = "project";
        }

        return projectName;
    }

    private static String sanitizeFileName(String name) {
        // 파일명에 사용할 수 없는 문자 제거
        return name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    private static void printUsage() {
        System.out.println("SourceParser - Java Project Analyzer");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  sourceparser <path>           Analyze project at path");
        System.out.println("  sourceparser .                Analyze current directory");
        System.out.println("  sourceparser -h, --help       Show help");
        System.out.println("  sourceparser -v, --version    Show version");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  sourceparser /path/to/project");
        System.out.println("  sourceparser .");
        System.out.println();
        System.out.println("Output:");
        System.out.println("  <projectname>-<timestamp>.json    Analysis result");
    }
}