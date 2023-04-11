import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;

public class lex {
    public ArrayList<type> analysis(File file) throws IOException {
        StringBuilder result = new StringBuilder();
//        PrintWriter output = new PrintWriter(fileoutput);
        FileInputStream in = new FileInputStream(file);
        PushbackInputStream reader = new PushbackInputStream(in);
        ArrayList<type> lexList = new ArrayList<>();
        int c;
        int row = 1;
        while ((c = reader.read()) != -1) {
            result.setLength(0);
            while (c == 13 || c == 10 || c == 32 || c == 9) {
                if (c == 10) {
                    row++;
                }
                c = reader.read();
            }
            if ((c >= 97 && c <= 122) || (c >= 65 && c <= 90) || c == '_') {
//                output.println((char) c);
                while ((c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122) ||
                    c == '_') {
                    result.append((char) c);
                    c = reader.read();
                }
                reader.unread(c);
                String s = "";
                s = reserver(result);
                if (s.isEmpty()) {
//                    output.println("IDENFR " + result);
                    type a = new type("IDENFR", result.toString());
                    a.setRow(row);
                    lexList.add(a);
                } else {
//                    output.println(s + " " + result);
                    type a = new type(s, result.toString());
                    a.setRow(row);
                    lexList.add(a);
                }
            } else if (c >= 48 && c <= 57) {
                while (c >= 48 && c <= 57) {
                    result.append((char) c);
                    c = reader.read();
                }
                reader.unread(c);
//                transNum(result);//字符串转换成整数
//                output.println("INTCON " + result);
                type a = new type("INTCON", result.toString());
                a.setRow(row);
                lexList.add(a);
            } else if (c == '+') {
//                output.println("PLUS +");
                type a = new type("PLUS", "+");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '-') {
//                output.println("MINU -");
                type a = new type("MINU", "-");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '*') {
//                output.println("MULT *");
                type a = new type("MULT", "*");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '(') {
//                output.println("LPARENT (");
                type a = new type("LPARENT", "(");
                a.setRow(row);
                lexList.add(a);
            } else if (c == ')') {
//                output.println("RPARENT )");
                type a = new type("RPARENT", ")");
                a.setRow(row);
                lexList.add(a);
            } else if (c == ',') {
//                output.println("COMMA ,");
                type a = new type("COMMA", ",");
                a.setRow(row);
                lexList.add(a);
            } else if (c == ';') {
//                output.println("SEMICN ;");
                type a = new type("SEMICN", ";");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '[') {
//                output.println("LBRACK [");
                type a = new type("LBRACK", "[");
                a.setRow(row);
                lexList.add(a);
            } else if (c == ']') {
//                output.println("RBRACK ]");
                type a = new type("RBRACK", "]");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '{') {
//                output.println("LBRACE {");
                type a = new type("LBRACE", "{");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '}') {
//                output.println("RBRACE }");
                type a = new type("RBRACE", "}");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '%') {
//                output.println("MOD %");
                type a = new type("MOD", "%");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '&') {
                c = reader.read();
//                output.println("AND &&");
                type a = new type("AND", "&&");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '|') {
                c = reader.read();
//                output.println("OR ||");
                type a = new type("OR", "||");
                a.setRow(row);
                lexList.add(a);
            } else if (c == '!') {
                c = reader.read();
                if (c == '=') {
//                    output.println("NEQ !=");
                    type a = new type("NEQ", "!=");
                    a.setRow(row);
                    lexList.add(a);
                } else {
                    reader.unread(c);
//                    output.println("NOT !");
                    type a = new type("NOT", "!");
                    a.setRow(row);
                    lexList.add(a);
                }
            } else if (c == '<') {
                c = reader.read();
                if (c == '=') {
//                    output.println("LEQ <=");
                    type a = new type("LEQ", "<=");
                    a.setRow(row);
                    lexList.add(a);
                } else {
                    reader.unread(c);
//                    output.println("LSS <");
                    type a = new type("LSS", "<");
                    a.setRow(row);
                    lexList.add(a);
                }
            } else if (c == '>') {
                c = reader.read();
                if (c == '=') {
//                    output.println("GEQ >=");
                    type a = new type("GEQ", ">=");
                    a.setRow(row);
                    lexList.add(a);
                } else {
                    reader.unread(c);
//                    output.println("GRE >");
                    type a = new type("GRE", ">");
                    a.setRow(row);
                    lexList.add(a);
                }
            } else if (c == '=') {
                c = reader.read();
                if (c == '=') {
//                    output.println("EQL ==");
                    type a = new type("EQL", "==");
                    a.setRow(row);
                    lexList.add(a);
                } else {
                    reader.unread(c);
//                    output.println("ASSIGN =");
                    type a = new type("ASSIGN", "=");
                    a.setRow(row);
                    lexList.add(a);
                }
            } else if (c == '/') {
                c = reader.read();
                if (c == '*') {
                    do {
                        do {
                            c = reader.read();
                            if (c == '\n') {
                                row++;
                            }
                        } while (c != '*');
                        c = reader.read();
                        if (c != '/') {
                            reader.unread(c);
                        }
                    } while (c != '/');
                } else if (c == '/') {
                    do {
                        c = reader.read();
                        if (c == '\n') {
                            row++;
                        }
                    } while (c != '\n');
                } else {
                    reader.unread(c);
//                    output.println("DIV /");
                    type a = new type("DIV", "/");
                    a.setRow(row);
                    lexList.add(a);
                }
            } else if (c == '"') {
                do {
                    result.append((char) c);
                    c = reader.read();
                } while (c != '"');
                result.append((char) c);
//                output.println("STRCON " + result);
                type a = new type("STRCON", result.toString());
                a.setRow(row);
                lexList.add(a);
            }
        }
        in.close();
//        output.close();
        return lexList;
    }

    public String reserver(StringBuilder stringBuilder) {
        String s = stringBuilder.toString();
        if (s.equals("main")) {
            return "MAINTK";
        } else if (s.equals("const")) {
            return "CONSTTK";
        } else if (s.equals("int")) {
            return "INTTK";
        } else if (s.equals("break")) {
            return "BREAKTK";
        } else if (s.equals("continue")) {
            return "CONTINUETK";
        } else if (s.equals("if")) {
            return "IFTK";
        } else if (s.equals("else")) {
            return "ELSETK";
        } else if (s.equals("while")) {
            return "WHILETK";
        } else if (s.equals("getint")) {
            return "GETINTTK";
        } else if (s.equals("printf")) {
            return "PRINTFTK";
        } else if (s.equals("return")) {
            return "RETURNTK";
        } else if (s.equals("void")) {
            return "VOIDTK";
        } else {
            return "";
        }
    }
}
