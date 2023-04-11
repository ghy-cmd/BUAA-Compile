import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Compiler {
    public static void main(String[] args) throws IOException {
        File file = new File("testfile.txt");
        File fileout = new File("output.txt");
//        File pcoderesult = new File("pcoderesult.txt");
//        File fileout = new File("error.txt");
        lex lex = new lex();
        ArrayList<type> lexList = lex.analysis(file);
        syntax syntax = new syntax(lexList, fileout);
        syntax.analysis();
        P_SJ r = new P_SJ(syntax.codePArrayList);
        r.jieshi();
    }
}
