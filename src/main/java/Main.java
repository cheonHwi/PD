import Analyzer.DependencyResolver;
import Analyzer.ProjectAnalyzer;
import Serializer.JsonSerializer;
import SourceParser.Model.ClassInfo;

import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
            ProjectAnalyzer projectAnalyzer = new ProjectAnalyzer();
            List<ClassInfo> classes = projectAnalyzer.analyzeProject();

            System.out.println("Analyzed " + classes.size() + " classes");

            DependencyResolver resolver = new DependencyResolver();
            resolver.resolveDependencies(classes);

            System.out.println("Dependencies resolved\n");

            resolver.printDependencyStatistics(classes);

            System.out.println("\n=== Class Dependencies ===");
            for (ClassInfo classInfo : classes) {
                System.out.println(classInfo.getClassName() + " depends on:");
                for (String dep : classInfo.getDependencies()) {
                    System.out.println("  - " + dep);
                }
                System.out.println();
            }

            JsonSerializer serializer = new JsonSerializer();
            String json = serializer.serialize(classes);

            try {
                serializer.saveToFile(json, "project-analysis.json");
            } catch (Exception e) {
                System.err.println("Failed to save JSON: " + e.getMessage());
            }
        }
}
