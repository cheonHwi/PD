import Analyzer.DependencyResolver;
import Analyzer.ParseResult;
import Analyzer.ProjectAnalyzer;
import SourceParser.Model.ClassInfo;
import Serializer.JsonSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
            File projectDir = new File(path);
            if (!projectDir.exists()) {
                System.err.println("✗ Error: Path does not exist: " + path);
                System.exit(1);
            }

            if (!projectDir.isDirectory()) {
                System.err.println("✗ Error: Path is not a directory: " + path);
                System.exit(1);
            }

            String projectName = extractProjectName(projectDir, path);

            String originalDir = System.getProperty("user.dir");
            System.setProperty("user.dir", projectDir.getAbsolutePath());

            System.out.println("📂 Analyzing: " + projectDir.getAbsolutePath());
            System.out.println("📦 Project: " + projectName);
            System.out.println();

            System.out.print("🔍 Parsing files... ");
            ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer(true);
            List<ClassInfo> classes = projectAnalyzer.analyzeProject();

            int successCount = projectAnalyzer.getSuccessCount();
            int failureCount = projectAnalyzer.getFailureCount();
            int totalCount = successCount + failureCount;

            if (classes.isEmpty() && failureCount == 0) {
                System.out.println("\n⚠ No Java files found");
                System.exit(0);
            }

            System.out.print("🔗 Resolving dependencies... ");
            DependencyResolver resolver = new DependencyResolver();
            resolver.resolveDependencies(classes);

            int totalDeps = classes.stream()
                    .mapToInt(c -> c.getDependencies().size())
                    .sum();
            System.out.println("✓");

            System.out.print("📝 Generating JSON... ");
            JsonSerializer serializer = new JsonSerializer();
            String json = serializer.serialize(classes);

            System.setProperty("user.dir", originalDir);

            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss"));
            String outputFileName = projectName + "-" + timestamp + ".json";

            serializer.saveToFile(json, outputFileName);
            System.out.println("✓");

            if (failureCount > 0) {
                String logFileName = projectName + "-" + timestamp + ".log";
                writeLogFile(logFileName, projectAnalyzer.getFailures());
            }

            long duration = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println();
            System.out.println("✨ Analysis complete!");
            System.out.println();
            System.out.println("📊 Statistics:");
            System.out.println("   Project:         " + projectName);
            System.out.println("   Total files:     " + totalCount);
            System.out.println("   ✓ Success:       " + successCount);

            if (failureCount > 0) {
                System.out.println("   ✗ Failed:        " + failureCount);
            }

            System.out.println("   Classes parsed:  " + classes.size());

            int totalMethods = classes.stream()
                    .mapToInt(c -> c.getMethods().size())
                    .sum();
            System.out.println("   Methods:         " + totalMethods);
            System.out.println("   Dependencies:    " + totalDeps);
            System.out.println("   Time:            " + duration + "s");
            System.out.println();
            System.out.println("📄 Output: " + new File(outputFileName).getAbsolutePath());

            if (failureCount > 0) {
                System.out.println();
                System.out.println("⚠️  Failed to parse " + failureCount + " file(s):");
                List<ParseResult> failures = projectAnalyzer.getFailures();

                int displayCount = Math.min(5, failures.size());
                for (int i = 0; i < displayCount; i++) {
                    ParseResult failure = failures.get(i);
                    System.out.println("   • " + failure.getFileName() + ": " + failure.getErrorMessage());
                }

                if (failures.size() > 5) {
                    System.out.println("   ... and " + (failures.size() - 5) + " more");
                }

                String logFileName = projectName + "-" + timestamp + ".log";
                System.out.println();
                System.out.println("📋 Full error log: " + new File(logFileName).getAbsolutePath());
            }

        } catch (Exception e) {
            System.err.println("\n✗ Error: " + e.getMessage());
            if (System.getenv("DEBUG") != null) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }

    private static void writeLogFile(String logFileName, List<ParseResult> failures) {
        try (FileWriter writer = new FileWriter(logFileName)) {
            writer.write("SourceParser Error Log\n");
            writer.write("======================\n\n");
            writer.write("Generated: " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n\n");
            writer.write("Failed Files: " + failures.size() + "\n\n");

            for (int i = 0; i < failures.size(); i++) {
                ParseResult failure = failures.get(i);
                writer.write((i + 1) + ". " + failure.getFileName() + "\n");
                writer.write("   Path: " + failure.getFilePath() + "\n");
                writer.write("   Error: " + failure.getErrorMessage() + "\n");

                if (failure.getException() != null) {
                    writer.write("   Stack trace:\n");
                    Exception e = failure.getException();
                    for (StackTraceElement element : e.getStackTrace()) {
                        writer.write("     " + element.toString() + "\n");
                    }
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("⚠️  Could not write log file: " + e.getMessage());
        }
    }

    private static String extractProjectName(File projectDir, String originalPath) {
        String projectName;

        if (originalPath.equals(".") || originalPath.equals("./")) {
            try {
                projectName = projectDir.getCanonicalFile().getName();
            } catch (Exception e) {
                projectName = projectDir.getAbsoluteFile().getName();
            }
        } else {
            projectName = projectDir.getName();

            if (projectName.isEmpty()) {
                try {
                    projectName = projectDir.getCanonicalFile().getName();
                } catch (Exception e) {
                    projectName = "project";
                }
            }
        }

        projectName = sanitizeFileName(projectName);

        if (projectName.isEmpty()) {
            projectName = "project";
        }

        return projectName;
    }

    private static String sanitizeFileName(String name) {
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
        System.out.println("  <projectname>-<timestamp>.log     Error log (if any failures)");
    }
}