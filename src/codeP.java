
public class codeP {
    String type;
    String value;
    Boolean inited = false;

    public static final String constInit = "constInit";
    public static final String varInit = "varInit";
    public static final String let = "let";
    public static final String mul = "mul";
    public static final String add = "add";
    public static final String sub = "sub";
    public static final String num = "num";
    public static final String div = "div";
    public static final String mod = "mod";
    public static final String lvalChange = "lvalChange";
    public static final String lvalCheck = "lvalCheck";
    public static final String printf = "printf";
    public static final String getInt = "getInt";
    public static final String funcInt = "funcInt";
    public static final String funcVoid = "funcVoid";
    public static final String call = "call";
    public static final String funcEnd = "funcEnd";
    public static final String para = "para";
    public static final String ret = "return";
    public static final String funcMain = "funcMain";
    public static final String addS = "addS";
    public static final String subS = "subS";
    public static final String and = "and";
    public static final String or = "or";
    public static final String equal = "equal";
    public static final String equalN = "equalN";
    public static final String dayu = ">";
    public static final String xiaoyu = "<";
    public static final String dayue = ">=";
    public static final String xiaoyue = "<=";
    public static final String If = "if";
    public static final String Else = "else";
    public static final String IfEnd = "Ifend";
    public static final String Cond = "cond";
    public static final String ElseEnd = "Elseend";
    public static final String not = "not";
    public static final String WhileFlag = "whileFlag";
    public static final String While = "while";
    public static final String WhileEnd = "whileEnd";
    public static final String Break = "break";
    public static final String Continue = "continue";
    public static final String BlockL = "blockleft";
    public static final String BlockR = "blockright";
    public static final String andBegin="andBegin";
    public static final String orBegin="orBegin";

    int number;
    int dimension = 0;
    Boolean TON;
    int place;
    int depth;


    public codeP(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepth() {
        return depth;
    }
}
