import SourceParser.Lexer.Lexer;
import SourceParser.Model.MethodInfo;
import SourceParser.Parser.Parser;

public class Main {
    public static void main(String[] args) {
        String code = "public List<CustomType<String, double>> main(int[] numList) {}";

        Lexer lexer = new Lexer(code);

        Parser parser = new Parser(lexer);
        MethodInfo methodInfo = parser.getMethodInfo();

        System.out.println(methodInfo);
    }
}
