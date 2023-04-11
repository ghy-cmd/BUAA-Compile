import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class syntax {
    ArrayList<type> lexList;
    PrintWriter output;
    int i = 0;
    int depth = 1;
    ArrayList<symbol> symbolArrayList = new ArrayList<>();
    int j;
    int j1;
    int func = -1;
    Boolean outSyntax = false;
    Boolean mistake = false;
    Boolean flag = false;
    Boolean whileFlag = false;
    Boolean bug = false;
    int lRow = 0;
    boolean funcR = false;
    int num = 0;
    boolean middle = false;
    boolean middleP = true;
    boolean codeMiddle = false;
    ArrayList<codeP> codePArrayList = new ArrayList<>();
    int ifId = 0;
    int whileId = 0;

    public syntax(ArrayList<type> lexList, File fileoutput) throws FileNotFoundException {
        this.lexList = lexList;
        this.output = new PrintWriter(fileoutput);
    }

    public void operate() {
        if (outSyntax) {
            output.println(getIdentity() + " " + getContent());
        }
//        output.println(getIdentity() + " " + getContent() + " " + getRow());
        i++;
    }

    public String getIdentity() {
        return lexList.get(i).identity;
    }

    public String getContent() {
        return lexList.get(i).content;
    }

    public int getRow() {
        return lexList.get(i).getRow();
    }

    public Boolean checkRepeat(String symbol) {
        for (symbol item : symbolArrayList) {
            if (item.getSymbol().equals(symbol) && item.getDepth() == depth) {
                if (mistake) {
                    output.println(getRow() + " b");
                }
                return true;
            }
        }
        return false;
    }

    public String checkC(String ident) {
        for (int i = symbolArrayList.size() - 1; i >= 0; i--) {
            if (symbolArrayList.get(i).getSymbol().equals(ident)) {
                return symbolArrayList.get(i).getType();
            }
        }
        if (mistake) {
            output.println(getRow() + " c");
        }
        return null;
    }

    public Boolean checkI() {
        if (!getContent().equals(";")) {
            if (mistake) {
                output.println(lexList.get(i - 1).getRow() + " i");
            }
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkJ() {
        if (!getContent().equals(")")) {
            if (mistake) {
                output.println(lexList.get(i - 1).getRow() + " j");
            }
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkK() {
        if (!getContent().equals("]")) {
            if (mistake) {
                output.println(lexList.get(i - 1).getRow() + " k");
            }
            return false;
        } else {
            return true;
        }
    }

    public void checkL(int num, int p, int pRow) {
        if (num != -1) {
            if (mistake) {
                if (num != p) {
                    output.println(pRow + " l");
                }
            }
        }
    }

    public void analysis() {
        CompUnit();
        if (bug) {
            output.println();
            for (symbol item : symbolArrayList) {
                output.println(
                    item.getSymbol() + " " + item.getType() + " " + item.getKind() + " " +
                        item.getDepth() + " " +
                        item.getDimension());
                if (item.getType().equals("func")) {
                    for (para y : item.paraArrayList) {
                        output.println("FUNC: " + y.kind + " " + y.dimension);
                    }
                }
            }
        }
        output.close();
    }

    public void CompUnit() {
        while (getIdentity().equals("CONSTTK") ||
            (getIdentity().equals("INTTK") &&
                lexList.get(i + 1).identity.equals("IDENFR") &&
                !lexList.get(i + 2).content.equals("("))) {
            Decl();
        }
        while (getIdentity().equals("VOIDTK") ||
            (getIdentity().equals("INTTK") &&
                !lexList.get(i + 1).identity.equals("MAINTK") &&
                lexList.get(i + 2).content.equals("("))) {
            FuncDef();
        }
        if (getIdentity().equals("INTTK") &&
            lexList.get(i + 1).identity.equals("MAINTK") &&
            lexList.get(i + 2).content.equals("(") && lexList.get(i + 3).content.equals(")")) {
            MainFuncDef();
        }
        if (outSyntax) {
            output.println("<CompUnit>");
        }
    }

    public void Decl() {
        if (getIdentity().equals("CONSTTK")) {
            ConstDecl();
        } else {
            VarDecl();
        }
    }

    public void MainFuncDef() {
        flag = true;
        type = "int";
        operate();
        operate();
        operate();
        operate();
        depth++;
        if (middleP) {
            codeP a = new codeP(codeP.funcMain);
            codePArrayList.add(a);
            if (codeMiddle) {
                output.println(a.type);
            }
        }
        Block();
        if (mistake) {
            if (type.equals("int") && flag) {
                output.println(lexList.get(i - 1).getRow() + " g");
            }
        }
        flag = false;
        if (outSyntax) {
            output.println("<MainFuncDef>");
        }
    }

    public void ConstDecl() {
        operate();
        if (getIdentity().equals("INTTK")) {
            operate();
            ConstDef();
            while (getContent().equals(",")) {
                operate();
                ConstDef();
            }
            if (checkI()) {
                operate();
            }
        }
        if (outSyntax) {
            output.println("<ConstDecl>");
        }
    }

    public void VarDecl() {
        operate();
        VarDef();
        while (getContent().equals(",")) {
            operate();
            VarDef();
        }
        if (checkI()) {
            operate();
        }
        if (outSyntax) {
            output.println("<VarDecl>");
        }
    }

    public void ConstDef() {
        String l = null;
        if (getIdentity().equals("IDENFR")) {
            symbol s = new symbol(getContent(), "const", "int", depth);
            l = getContent();
            if (!checkRepeat(getContent())) {
                symbolArrayList.add(s);
            }
            operate();
            j = 0;
            while (!getContent().equals("=")) {
                if (getContent().equals("[")) {
                    operate();
                    ConstExp();
                    if (checkK()) {
                        operate();
                    }
                }
                j++;
            }
            if (middleP) {
                codeP a = new codeP(codeP.constInit);
                a.setValue(l);
                a.inited = true;
                a.dimension = j;
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type + " " + a.value);
                }
            }
            s.setDimension(j);
            operate();
            String r = ConstInitVal();
            if (middle) {
                output.println("const int " + l + "=" + r);
            }

            if (middleP) {
                codeP b = new codeP(codeP.let);
                codePArrayList.add(b);
                if (codeMiddle) {
                    output.println("=");
                }
            }
        }
        if (outSyntax) {
            output.println("<ConstDef>");
        }

    }

    public void VarDef() {
        String l = null;
        symbol s = new symbol(getContent(), "var", "int", depth);
        codeP a = new codeP(codeP.varInit);
        if (getIdentity().equals("IDENFR")) {
            l = getContent();
            if (!checkRepeat(getContent())) {
                symbolArrayList.add(s);
            }
            a.setValue(getContent());
            operate();
        }
        j = 0;
        while (getContent().equals("[")) {
            operate();
            ConstExp();
            if (checkK()) {
                operate();
            }
            j++;
        }
        s.setDimension(j);
        a.dimension = j;
        if (middleP) {
            codePArrayList.add(a);
            if (codeMiddle) {
                output.println("varInit " + a.value);
            }
        }
        if (getContent().equals("=")) {
            operate();
            String r = InitVal();
            if (middle) {
                output.println("var int " + l + "=" + r);
            }
            a.inited = true;
            if (middleP) {
                codeP b = new codeP(codeP.let);
                codePArrayList.add(b);
                if (codeMiddle) {
                    output.println("=");
                }
            }
        } else {
            if (middle) {
                output.println("var int " + l);
            }
        }
        if (outSyntax) {
            output.println("<VarDef>");
        }
    }

    String type = "";

    public void FuncDef() {
        FuncType();
        symbol s = new symbol(getContent(), "func", type, depth);
        if (middleP) {
            if (type.equals("int")) {
                codeP a = new codeP(codeP.funcInt);
                a.setValue(getContent());
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type + " " + a.value);
                }
            } else {
                codeP a = new codeP(codeP.funcVoid);
                a.setValue(getContent());
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type + " " + a.value);
                }
            }
        }
        if (getIdentity().equals("IDENFR")) {
            if (!checkRepeat(getContent())) {
                symbolArrayList.add(s);
                func = symbolArrayList.size() - 1;
            }
            if (middle) {
                output.println(type + " " + getContent() + "()");
            }
            operate();
        }
        j = 0;
        depth++;
        if (getContent().equals("(")) {
            operate();
            if (!getContent().equals(")")) {
                if (getContent().equals("int")) {
                    FuncFParams();
                }
            }
            if (checkJ()) {
                operate();
            }
        }
        s.setDimension(j);
        Block();
        if (mistake) {
            if (type.equals("int") && flag) {
                output.println(lexList.get(i - 1).getRow() + " g");
            }
        }
        flag = false;
        symbolArrayList.removeIf(item -> item.getDepth() == depth);
        depth--;
        if (outSyntax) {
            output.println("<FuncDef>");
        }
        func = -1;
        if (middleP) {
            codeP a = new codeP(codeP.funcEnd);
            codePArrayList.add(a);
            if (codeMiddle) {
                output.println(a.type);
            }
        }
    }

    public void FuncType() {
        type = getContent();
        if (type.equals("int")) {
            flag = true;
        }
        operate();
        if (outSyntax) {
            output.println("<FuncType>");
        }
    }

    public String ConstInitVal() {
        String l = null;
        if (getContent().equals("{")) {
            operate();
            if (!getContent().equals("}")) {
                ConstInitVal();
                while (getContent().equals(",")) {
                    operate();
                    ConstInitVal();
                }
            }
            operate();
        } else {
            l = ConstExp();
        }
        if (outSyntax) {
            output.println("<ConstInitVal>");
        }
        return l;
    }

    public String InitVal() {
        String l = null;
        if (getContent().equals("{")) {
            operate();
            InitVal();
            while (getContent().equals(",")) {
                operate();
                InitVal();
            }
            if (getContent().equals("}")) {
                operate();
            }
        } else {
            l = Exp();
        }
        if (outSyntax) {
            output.println("<InitVal>");
        }
        return l;
    }

    public String ConstExp() {
        String l = AddExp();
        if (outSyntax) {
            output.println("<ConstExp>");
        }
        return l;
    }

    public String AddExp() {
        String l = MulExp();
        while (getContent().equals("+") || getContent().equals("-")) {
            String m = getContent();
            if (outSyntax) {
                output.println("<AddExp>");
            }
            operate();
            String r = MulExp();
            num++;
            if (middle) {
                output.println("t" + num + "=" + l + m + r);
            }
            l = "t" + num;
            if (m.equals("+")) {
                if (middleP) {
                    codeP a = new codeP(codeP.add);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println("+");
                    }
                }
            } else if (m.equals("-")) {
                if (middleP) {
                    codeP a = new codeP(codeP.sub);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println("-");
                    }
                }
            }
        }
        if (outSyntax) {
            output.println("<AddExp>");
        }
        return l;
    }

    public String MulExp() {
        String l = UnaryExp();
        while (getContent().equals("*") || getContent().equals("/") ||
            getContent().equals("%")) {
            String m = getContent();
            if (outSyntax) {
                output.println("<MulExp>");
            }
            operate();
            String r = UnaryExp();
            num++;
            if (middle) {
                output.println("t" + num + "=" + l + m + r);
            }
            l = "t" + num;
            switch (m) {
                case "*": {
                    if (middleP) {
                        codeP a = new codeP(codeP.mul);
                        codePArrayList.add(a);
                        if (codeMiddle) {
                            output.println("*");
                        }
                    }
                    break;
                }
                case "/": {
                    if (middleP) {
                        codeP a = new codeP(codeP.div);
                        codePArrayList.add(a);
                        if (codeMiddle) {
                            output.println("/");
                        }
                    }
                    break;
                }
                case "%": {
                    if (middleP) {
                        codeP a = new codeP(codeP.mod);
                        codePArrayList.add(a);
                        if (codeMiddle) {
                            output.println("%");
                        }
                    }
                    break;
                }
            }
        }
        if (outSyntax) {
            output.println("<MulExp>");
        }
        return l;
    }

    public String UnaryExp() {
        String l = null;
        if (getIdentity().equals("IDENFR") && lexList.get(i + 1).content.equals("(")) {
            String m = getContent();

            checkC(getContent());
            int rowNow = getRow();
            String funcNow = getContent();
            operate();
            int iStart = i;
            int iFinal = i;
            operate();
            if (!getContent().equals(")")) {
                FuncRParams();
            }
            if (middle) {
                output.println("call " + m);
            }
            if (middleP) {
                codeP a = new codeP(codeP.call);
                a.setValue(m);
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type + " " + a.value);
                }
            }
            if (checkJ()) {
                iFinal = i;
                if (mistake) {
                    check.checkE(symbolArrayList, iStart, iFinal, rowNow, funcNow, output, lexList);
                }
                operate();
            }
            l = "RET";
        } else if (getContent().equals("(") || getIdentity().equals("INTCON") ||
            getIdentity().equals("IDENFR")) {
            l = PrimaryExp();
        } else if (getContent().equals("+") || getContent().equals("-") ||
            getContent().equals("!")) {
            String m = UnaryOp();
            String r = UnaryExp();
            l = " ";
            num++;
            if (middle) {
                output.println("t" + num + "=" + l + m + r);
            }
            l = "t" + num;
            if (m.equals("+")) {
                if (middleP) {
                    codeP a = new codeP(codeP.addS);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            } else if (m.equals("-")) {
                if (middleP) {
                    codeP a = new codeP(codeP.subS);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            } else if (m.equals("!")) {
                if (middleP) {
                    codeP a = new codeP(codeP.not);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            }

        }
        if (outSyntax) {
            output.println("<UnaryExp>");
        }
        return l;
    }

    public void FuncRParams() {
        String l = Exp();
        if (middle) {
            output.println("push " + l);
        }
        while (getContent().equals(",")) {
            operate();
            l = Exp();
            if (middle) {
                output.println("push " + l);
            }
        }
        if (outSyntax) {
            output.println("<FuncRParams>");
        }
    }

    public void FuncFParams() {
        FuncFParam();
        j++;
        while (getContent().equals(",")) {
            operate();
            FuncFParam();
            j++;
        }
        if (outSyntax) {
            output.println("<FuncFParams>");
        }
    }

    public String Exp() {
        String l = AddExp();
        if (outSyntax) {
            output.println("<Exp>");
        }
        return l;
    }

    public void FuncFParam() {
        StringBuilder l;
        if (getIdentity().equals("INTTK")) {
            operate();
        }
        symbol s = new symbol(getContent(), "para", "int", depth);
        l = new StringBuilder(getContent());
        String m = getContent();
        if (getIdentity().equals("IDENFR")) {
            if (!checkRepeat(getContent())) {
                symbolArrayList.add(s);
            }
            operate();
        }
        j1 = 0;
        if (getContent().equals("[")) {
            j1++;
            operate();
            if (checkK()) {
                operate();
            }
            l.append("[]");
            while (getContent().equals("[")) {
                middleP = false;
                operate();
                l.append("[").append(ConstExp()).append("]");
                if (checkK()) {
                    operate();
                }
                middleP = true;
                j1++;
            }
        }
        s.setDimension(j1);
        if (middleP) {
            codeP a = new codeP(codeP.para);
            a.setValue(m);
            a.dimension = j1;
            codePArrayList.add(a);
            if (codeMiddle) {
                output.println(a.type + " " + a.value);
            }
        }
        para p = new para("int", j1);
        if (func != -1) {
            symbolArrayList.get(func).paraArrayList.add(p);
            if (middle) {
                output.println("para int " + l.toString());
            }
        }
        if (outSyntax) {
            output.println("<FuncFParam>");
        }
    }

    public String PrimaryExp() {
        String l = null;
        if (getIdentity().equals("INTCON")) {
            l = Number();
        } else if (getContent().equals("(")) {
            operate();
            l = Exp();
            if (checkJ()) {
                operate();
            }
        } else if (getIdentity().equals("IDENFR")) {
            l = LVal(2);
        }
        if (outSyntax) {
            output.println("<PrimaryExp>");
        }
        return l;
    }

    String result = null;

    public String LVal(int from) {
        String l = null;
        if (getIdentity().equals("IDENFR")) {
            result = checkC(getContent());
            l = getContent();
            lRow = getRow();
            operate();
        }
        int j = 0;
        while (getContent().equals("[")) {
            j++;
            operate();
            Exp();
            if (checkK()) {
                operate();
            }
        }
        if (middleP) {
            if (from == 1) {
                codeP a = new codeP(codeP.lvalChange);
                a.value = l;
                a.dimension = j;
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.value);
                }
            } else if (from == 2) {
                codeP a = new codeP(codeP.lvalCheck);
                a.value = l;
                a.dimension = j;
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.value);
                }
            }
        }
        if (outSyntax) {
            output.println("<LVal>");
        }
        return l;
    }

    public String Number() {
        String m = getContent();
        operate();
        if (outSyntax) {
            output.println("<Number>");
        }
        int num = 0;
        for (int ghy = 0; ghy < m.length(); ghy++) {
            num = num * 10 + m.charAt(ghy) - 48;
        }
        if (middleP) {
            codeP a = new codeP(codeP.num);
            a.setValue(m);
            a.setNumber(num);
            codePArrayList.add(a);
            if (codeMiddle) {
                output.println(a.value);
            }
        }
        return m;
    }

    public void Block() {
        operate();
        while (!getContent().equals("}")) {
            BlockItem();
        }
        operate();
        if (outSyntax) {
            output.println("<Block>");
        }
    }

    public void BlockItem() {
        if (getIdentity().equals("CONSTTK") ||
            (getIdentity().equals("INTTK") &&
                lexList.get(i + 1).identity.equals("IDENFR") &&
                !lexList.get(i + 2).content.equals("("))) {
            Decl();
        } else {
            Stmt();
        }
    }

    public void Stmt() {
        int nowi = i;
        String l = null;
//        for (int j = i; ; j++) {
//            if (lexList.get(j).content.equals(";")) {
//                break;
//            } else {
//                if (lexList.get(j).content.equals("=")) {
//                    judge = true;
//                }
//            }
//        }
        if (getIdentity().equals("IDENFR")) {
            if (true) {
                outSyntax = false;
                middleP = false;
                LVal(1);
                if (!getContent().equals("=")) {
                    outSyntax = false;
                    middleP = true;
                    i = nowi;
                    if (!getContent().equals(";")) {
                        Exp();
                    }
                    if (checkI()) {
                        operate();
                    }
                } else {
                    outSyntax = false;
                    middleP = true;
                    i = nowi;
                    LVal(1);
                    operate();
                    if (getContent().equals("getint")) {
                        operate();
                        operate();
                        if (checkJ()) {
                            operate();
                        }
                        if (middleP) {
                            codeP a = new codeP(codeP.getInt);
                            codePArrayList.add(a);
                            if (codeMiddle) {
                                output.println(a.type);
                            }
                        }
                    } else {
                        Exp();
                    }
                    if (middleP) {
                        codeP a = new codeP(codeP.let);
                        codePArrayList.add(a);
                        if (codeMiddle) {
                            output.println("=");
                        }
                    }
                    if (checkI()) {
                        operate();
                    }
                }
            } else if (mistake) {
                mistake = false;
                LVal(1);
                if (!getContent().equals("=")) {
                    mistake = true;
                    i = nowi;
                    if (!getContent().equals(";")) {
                        Exp();
                    }
                    if (checkI()) {
                        operate();
                    }
                } else {
                    mistake = true;
                    i = nowi;
                    LVal(1);
                    if (mistake) {
                        if (result != null && result.equals("const")) {
                            output.println(lRow + " h");
                        }
                        result = "null";
                    }
                    operate();
                    if (getContent().equals("getint")) {
                        operate();
                        operate();
                        if (checkJ()) {
                            operate();
                        }
                    } else {
                        Exp();
                    }
                    if (checkI()) {
                        operate();
                    }
                }
            }
        } else if (getContent().equals("{")) {
            depth++;
            if (middleP) {
                codeP a = new codeP(codeP.BlockL);
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
            Block();
            symbolArrayList.removeIf(item -> item.getDepth() == depth);
            depth--;
            if (middleP) {
                codeP a = new codeP(codeP.BlockR);
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
        } else if (getContent().equals("if")) {
            ifId++;
            operate();
            operate();
            Cond();
            if (middleP) {
                codeP a = new codeP(codeP.If);
                a.number = ifId;
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
            if (checkJ()) {
                operate();
            }
            Stmt();
            if (getContent().equals("else")) {
                if (middleP) {
                    codeP a = new codeP(codeP.Else);
                    a.number = ifId;
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
                operate();
                Stmt();
                if (middleP) {
                    codeP a = new codeP(codeP.ElseEnd);
                    a.number = ifId;
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            } else {
                if (middleP) {
                    codeP a = new codeP(codeP.IfEnd);
                    a.number = ifId;
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            }
            ifId--;
        } else if (getContent().equals("while")) {
            whileId++;
            whileFlag = true;
            operate();
            operate();
            if (middleP) {
                codeP a = new codeP(codeP.WhileFlag);
                a.number = whileId;
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
            Cond();
            if (middleP) {
                codeP a = new codeP(codeP.While);
                a.number = whileId;
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
            if (checkJ()) {
                operate();
            }
            Stmt();
            if (middleP) {
                codeP a = new codeP(codeP.WhileEnd);
                a.number = whileId;
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
            whileId--;
            whileFlag = false;
        } else if (getContent().equals("break") ||
            getContent().equals("continue")) {
            if (mistake) {
                if (!whileFlag) {
                    output.println(getRow() + " m");
                }
            }
            if (getContent().equals("break")) {
                if (middleP) {
                    codeP a = new codeP(codeP.Break);
                    a.number = whileId;
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            } else {
                if (middleP) {
                    codeP a = new codeP(codeP.Continue);
                    a.number = whileId;
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            }
            operate();
            if (checkI()) {
                operate();
            }
        } else if (getContent().equals("return")) {
            operate();
            if (!getContent().equals(";")) {
                l = Exp();
                if (middle) {
                    output.println("ret " + l);
                }
                if (mistake) {
                    if (type.equals("void")) {
                        output.println(getRow() + " f");
                    }
                }
            }
            if (middleP) {
                codeP a = new codeP(codeP.ret);
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
            if (checkI()) {
                operate();
            }
            if (getContent().equals("}")) {
                flag = false;
            }
        } else if (getContent().equals("printf")) {
            int pRow = getRow();
            operate();
            operate();
            String m = getContent();
            int t = getRow();
            operate();
            j = 0;
            while (getContent().equals(",")) {
                operate();
                Exp();
                j++;
            }
            int num = check.checkA(output, m, t);
            if (middleP) {
                codeP a = new codeP(codeP.printf);
                a.value = m;
                a.setNumber(j);
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
            checkL(num, j, pRow);

            if (checkJ()) {
                operate();
            }
            if (checkI()) {
                operate();
            }
        } else {
            if (!getContent().equals(";")) {
                Exp();
            }
            if (checkI()) {
                operate();
            }
        }
        if (outSyntax) {
            output.println("<Stmt>");
        }
    }

    public void Cond() {
        LOrExp();
        if (outSyntax) {
            output.println("<Cond>");
        }
    }

    public void LOrExp() {
        LAndExp();
        while (getContent().equals("||")) {
            if (outSyntax) {
                output.println("<LOrExp>");
            }
            operate();
            if (middleP) {
                codeP a = new codeP(codeP.orBegin);
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
            LAndExp();
            if (middleP) {
                codeP a = new codeP(codeP.or);
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
        }
        if (outSyntax) {
            output.println("<LOrExp>");
        }
    }

    public void LAndExp() {
        EqExp();
        while (getContent().equals("&&")) {
            if (outSyntax) {
                output.println("<LAndExp>");
            }
            operate();
            if (middleP) {
                codeP a = new codeP(codeP.andBegin);
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
            EqExp();
            if (middleP) {
                codeP a = new codeP(codeP.and);
                codePArrayList.add(a);
                if (codeMiddle) {
                    output.println(a.type);
                }
            }
        }
        if (outSyntax) {
            output.println("<LAndExp>");
        }
    }

    public void EqExp() {
        RelExp();
        while (getContent().equals("==") || getContent().equals("!=")) {
            if (outSyntax) {
                output.println("<EqExp>");
            }
            String r = getContent();
            operate();
            RelExp();
            if (r.equals("==")) {
                if (middleP) {
                    codeP a = new codeP(codeP.equal);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            } else {
                if (middleP) {
                    codeP a = new codeP(codeP.equalN);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            }
        }
        if (outSyntax) {
            output.println("<EqExp>");
        }
    }

    public void RelExp() {
        String l = AddExp();
        while (getContent().equals("<") || getContent().equals(">") || getContent().equals("<=") ||
            getContent().equals(">=")) {
            if (outSyntax) {
                output.println("<RelExp>");
            }
            String r = getContent();
            operate();
            AddExp();
            if (r.equals("<")) {
                if (middleP) {
                    codeP a = new codeP(codeP.xiaoyu);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            } else if (r.equals(">")) {
                if (middleP) {
                    codeP a = new codeP(codeP.dayu);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            } else if (r.equals("<=")) {
                if (middleP) {
                    codeP a = new codeP(codeP.xiaoyue);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            } else if (r.equals(">=")) {
                if (middleP) {
                    codeP a = new codeP(codeP.dayue);
                    codePArrayList.add(a);
                    if (codeMiddle) {
                        output.println(a.type);
                    }
                }
            }
        }
        if (outSyntax) {
            output.println("<RelExp>");
        }
    }

    public String UnaryOp() {
        String m = getContent();
        operate();
        if (outSyntax) {
            output.println("<UnaryOp>");
        }
        return m;
    }
}
