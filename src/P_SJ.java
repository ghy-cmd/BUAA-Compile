import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class P_SJ {
    ArrayList<codeP> codePArrayList;
    ArrayList<codeP> yunxingArrayList = new ArrayList<>();
    ArrayList<symbol> symbolArrayList = new ArrayList<>();
    File pcoderesult = new File("pcoderesult.txt");
    PrintWriter pcodeOut = new PrintWriter(pcoderesult);
    int depth = 0;
    boolean func = false;
    Scanner s = new Scanner(System.in);

    public P_SJ(ArrayList<codeP> codePArrayList) throws FileNotFoundException {
        this.codePArrayList = codePArrayList;
    }

    public void jieshi() throws IOException {
        int d = 0;
        int dp = 0;
        int fill = 0;
        for (int i = 0; i < codePArrayList.size(); i++) {
            int temp = yunxingArrayList.size();//栈顶指针
            codeP now = codePArrayList.get(i);//当前指令
            if (now.type.equals(codeP.add) || now.type.equals(codeP.sub) ||
                now.type.equals(codeP.div) || now.type.equals(codeP.mul) ||
                now.type.equals(codeP.mod)) {
                int num1 = 0;
                int num2 = 0;
                codeP a1 = yunxingArrayList.get(temp - 1);
                codeP a2 = yunxingArrayList.get(temp - 2);
                if (a1.type.equals(codeP.num)) {
                    num1 = a1.number;
                }
                if (a2.type.equals(codeP.num)) {
                    num2 = a2.number;
                }
                yunxingArrayList.remove(temp - 1);
                temp = yunxingArrayList.size();
                yunxingArrayList.remove(temp - 1);
                codeP a = new codeP(codeP.num);
                a.depth = depth;
                switch (now.type) {
                    case codeP.add:
                        a.setNumber(num1 + num2);
                        break;
                    case codeP.sub:
                        a.setNumber(num2 - num1);
                        break;
                    case codeP.mul:
                        a.setNumber(num2 * num1);
                        break;
                    case codeP.div:
                        a.setNumber(num2 / num1);
                        break;
                    case codeP.mod:
                        a.setNumber(num2 % num1);
                        break;
                }
                yunxingArrayList.add(a);
            } else if (now.type.equals(codeP.subS)) {
                int num = 0;
                codeP a1 = yunxingArrayList.get(temp - 1);
                if (a1.type.equals(codeP.num)) {
                    num = a1.number;
                }
                yunxingArrayList.remove(temp - 1);
                codeP a = new codeP(codeP.num);
                a.setNumber(-num);
                a.depth = depth;
                yunxingArrayList.add(a);
            } else if (now.type.equals(codeP.num)) {
                now.depth = depth;
                yunxingArrayList.add(now);
            } else if (now.type.equals(codeP.constInit)) {
                symbol s = new symbol(now.value, "const", "int", depth);
                s.dimension = now.dimension;
                if (now.dimension == 1) {
                    s.d1 = yunxingArrayList.get(temp - 1).number;
                    yunxingArrayList.remove(temp - 1);
                } else if (now.dimension == 2) {
                    s.d2 = yunxingArrayList.get(temp - 1).number;
                    yunxingArrayList.remove(temp - 1);
                    temp = yunxingArrayList.size();
                    s.d1 = yunxingArrayList.get(temp - 1).number;
                    yunxingArrayList.remove(temp - 1);
                }
                symbolArrayList.add(s);
                yunxingArrayList.add(now);
            } else if (now.type.equals(codeP.varInit)) {
                symbol s = new symbol(now.value, "var", "int", depth);
                s.dimension = now.dimension;
                if (now.dimension == 1) {
                    s.d1 = yunxingArrayList.get(temp - 1).number;
                    yunxingArrayList.remove(temp - 1);
                } else if (now.dimension == 2) {
                    s.d2 = yunxingArrayList.get(temp - 1).number;
                    yunxingArrayList.remove(temp - 1);
                    temp = yunxingArrayList.size();
                    s.d1 = yunxingArrayList.get(temp - 1).number;
                    yunxingArrayList.remove(temp - 1);
                }
                symbolArrayList.add(s);
                if (now.inited) {
                    yunxingArrayList.add(now);
                }
            } else if (now.type.equals(codeP.lvalChange)) {
                yunxingArrayList.add(now);
            } else if (now.type.equals(codeP.lvalCheck)) {
                for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                    symbol s = symbolArrayList.get(j);
                    if (func) {
                        if (s.getSymbol().equals(now.value) && !s.getType().equals("func")) {
                            if (now.dimension == 0 && s.dimension == 0) {
                                codeP a = new codeP(codeP.num);
                                a.depth = depth;
                                a.number = s.value;//默认不会出现未定义变量调用
                                yunxingArrayList.add(a);
                            } else if (now.dimension == 1 && s.dimension == 1) {
                                codeP a = new codeP(codeP.num);
                                a.depth = depth;
                                a.number = s.value1[yunxingArrayList.get(temp - 1).number];
                                yunxingArrayList.remove(temp - 1);
                                yunxingArrayList.add(a);
                            } else if (now.dimension == 2 && s.dimension == 2) {
                                codeP a = new codeP(codeP.num);
                                a.depth = depth;
                                a.number = s.value2[yunxingArrayList.get(
                                    temp - 2).number][yunxingArrayList.get(temp - 1).number];
                                yunxingArrayList.remove(temp - 1);
                                temp = yunxingArrayList.size();
                                yunxingArrayList.remove(temp - 1);
                                yunxingArrayList.add(a);
                            } else {
                                now.depth = depth;
                                yunxingArrayList.add(now);
                            }
                            break;
                        }
                    } else if (s.getSymbol().equals(now.value) && !s.getType().equals("func")) {
                        if (now.dimension == 0 && s.dimension == 0) {
                            codeP a = new codeP(codeP.num);
                            a.depth = depth;
                            a.number = s.value;//默认不会出现未定义变量调用
                            yunxingArrayList.add(a);
                        } else if (now.dimension == 1 && s.dimension == 1) {
                            codeP a = new codeP(codeP.num);
                            a.depth = depth;
                            a.number = s.value1[yunxingArrayList.get(temp - 1).number];
                            yunxingArrayList.remove(temp - 1);
                            yunxingArrayList.add(a);
                        } else if (now.dimension == 2 && s.dimension == 2) {
                            codeP a = new codeP(codeP.num);
                            a.depth = depth;
                            a.number = s.value2[yunxingArrayList.get(
                                temp - 2).number][yunxingArrayList.get(temp - 1).number];
                            yunxingArrayList.remove(temp - 1);
                            temp = yunxingArrayList.size();
                            yunxingArrayList.remove(temp - 1);
                            yunxingArrayList.add(a);
                        } else {
                            now.depth = depth;
                            yunxingArrayList.add(now);
                        }
                        break;
                    }
                }
            } else if (now.type.equals(codeP.printf)) {
                int j = 0;
                int d1 = now.number;
                StringBuilder f = new StringBuilder(now.value);
                f.delete(0, 1);
                f.delete(f.length() - 1, f.length());
                do {
                    if (j >= f.length()) {
                        break;
                    }
                    if (f.charAt(j) == 32 || f.charAt(j) == 33 ||
                        (f.charAt(j) >= 40 && f.charAt(j) <= 126 && f.charAt(j) != 92)) {
                        j++;
                    } else if (f.charAt(j) == '%') {
                        j++;
                        if (f.charAt(j) == 'd') {
                            f.delete(j - 1, j + 1);
                            codeP a = yunxingArrayList.get(temp - d1);
                            String s = String.valueOf(a.number);
                            f.insert(j - 1, s);
                            j = j - 1 + s.length();
                            yunxingArrayList.remove(temp - d1);
                            temp = yunxingArrayList.size();
                            d1--;
                        }
                    } else if (f.charAt(j) == '\\') {
                        j++;
                        if (f.charAt(j) == 'n') {
                            StringBuilder tt = new StringBuilder(f);
                            StringBuilder huanhang2 = f.delete(0, j + 1);
                            StringBuilder huanhang1 = tt.delete(j - 1, tt.length());
                            pcodeOut.println(huanhang1);
                            j = 0;
                        }
                    } else {
                        j++;
                    }
                } while (j < f.length());
                pcodeOut.print(f);
            } else if (now.type.equals(codeP.let)) {
                for (int k = yunxingArrayList.size() - 1; k >= 0; k--) {
                    if (yunxingArrayList.get(k).type.equals(codeP.constInit)) {
                        for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                            if (symbolArrayList.get(j).getSymbol()
                                .equals(yunxingArrayList.get(k).value) &&
                                symbolArrayList.get(j).getType().equals("const")) {
                                if (symbolArrayList.get(j).dimension == 0) {
                                    symbolArrayList.get(j).value =
                                        yunxingArrayList.get(temp - 1).number;
                                    yunxingArrayList.remove(temp - 1);
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                } else if (symbolArrayList.get(j).dimension == 1) {
                                    int m = symbolArrayList.get(j).d1;
                                    for (int n = 0; n < m; n++) {
                                        temp = yunxingArrayList.size();
                                        symbolArrayList.get(j).value1[m - 1 - n] =
                                            yunxingArrayList.get(temp - 1).number;
                                        yunxingArrayList.remove(temp - 1);
                                    }
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                } else if (symbolArrayList.get(j).dimension == 2) {
                                    int m = symbolArrayList.get(j).d1;
                                    int n = symbolArrayList.get(j).d2;
                                    for (int g = m - 1; g >= 0; g--) {
                                        for (int y = n - 1; y >= 0; y--) {
                                            temp = yunxingArrayList.size();
                                            symbolArrayList.get(j).value2[g][y] =
                                                yunxingArrayList.get(temp - 1).number;
                                            yunxingArrayList.remove(temp - 1);
                                        }
                                    }
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                }
                                break;
                            }
                        }
                        break;
                    } else if (yunxingArrayList.get(k).type.equals(codeP.varInit)) {
                        for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                            if (symbolArrayList.get(j).getSymbol()
                                .equals(yunxingArrayList.get(k).value)) {
                                if (symbolArrayList.get(j).dimension == 0) {
                                    symbolArrayList.get(j).value =
                                        yunxingArrayList.get(temp - 1).number;
                                    yunxingArrayList.remove(temp - 1);
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                } else if (symbolArrayList.get(j).dimension == 1) {
                                    int m = symbolArrayList.get(j).d1;
                                    for (int n = 0; n < m; n++) {
                                        temp = yunxingArrayList.size();
                                        symbolArrayList.get(j).value1[m - 1 - n] =
                                            yunxingArrayList.get(temp - 1).number;
                                        yunxingArrayList.remove(temp - 1);
                                    }
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                } else if (symbolArrayList.get(j).dimension == 2) {
                                    int m = symbolArrayList.get(j).d1;
                                    int n = symbolArrayList.get(j).d2;
                                    for (int g = m - 1; g >= 0; g--) {
                                        for (int y = n - 1; y >= 0; y--) {
                                            temp = yunxingArrayList.size();
                                            symbolArrayList.get(j).value2[g][y] =
                                                yunxingArrayList.get(temp - 1).number;
                                            yunxingArrayList.remove(temp - 1);
                                        }
                                    }
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                }
                                break;
                            }
                        }
                        break;
                    } else if (yunxingArrayList.get(k).type.equals(codeP.lvalChange)) {
                        for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                            if (symbolArrayList.get(j).getSymbol()
                                .equals(yunxingArrayList.get(k).value)) {
                                if (symbolArrayList.get(j).dimension == 0) {
                                    symbolArrayList.get(j).value =
                                        yunxingArrayList.get(temp - 1).number;
                                    yunxingArrayList.remove(temp - 1);
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                } else if (symbolArrayList.get(j).dimension == 1) {
                                    symbolArrayList.get(j).value1[yunxingArrayList.get(
                                        k - 1).number] = yunxingArrayList.get(temp - 1).number;
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                } else if (symbolArrayList.get(j).dimension == 2) {
                                    symbolArrayList.get(j).value2[yunxingArrayList.get(
                                        k - 2).number][yunxingArrayList.get(k - 1).number] =
                                        yunxingArrayList.get(temp - 1).number;
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                    temp = yunxingArrayList.size();
                                    yunxingArrayList.remove(temp - 1);
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            } else if (now.type.equals(codeP.getInt)) {
                int num = s.nextInt();
                codeP a = new codeP(codeP.num);
                a.number = num;
                yunxingArrayList.add(a);
            } else if (now.type.equals(codeP.funcInt)) {
                if (depth == 0) {
                    symbol s = new symbol(now.value, "func", "int", depth);
                    s.setPlace(i);
                    symbolArrayList.add(s);
                    d = 0;
                    do {
                        i++;
                        if (codePArrayList.get(i).type.equals(codeP.para)) {
                            d++;
                        }
                    } while (!codePArrayList.get(i).type.equals(codeP.funcEnd));
                    s.setDimension(d);
                } else {
                    symbol s = new symbol(now.value, "func", "int", depth);
                    s.setPlace(i);
                    s.sp = dp;
                    s.setDimension(d);
                    symbolArrayList.add(s);
                }
            } else if (now.type.equals(codeP.funcVoid)) {
                if (depth == 0) {
                    symbol s = new symbol(now.value, "func", "void", depth);
                    s.setPlace(i);
                    symbolArrayList.add(s);
                    d = 0;
                    do {
                        i++;
                        if (codePArrayList.get(i).type.equals(codeP.para)) {
                            d++;
                        }
                    } while (!codePArrayList.get(i).type.equals(codeP.funcEnd));
                    s.setDimension(d);
                } else {
                    symbol s = new symbol(now.value, "func", "void", depth);
                    s.setPlace(i);
                    s.sp = dp;
                    s.setDimension(d);
                    symbolArrayList.add(s);
                }
            } else if (now.type.equals(codeP.call)) {
                for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                    if (symbolArrayList.get(j).getType().equals("func") &&
                        symbolArrayList.get(j).getSymbol().equals(now.value)) {
                        dp = i;
                        i = symbolArrayList.get(j).place - 1;
                        d = symbolArrayList.get(j).dimension;
                        depth++;
                        func = true;
                        break;
                    }
                }
            } else if (now.type.equals(codeP.funcEnd)) {
                if (depth != 0) {
                    for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                        if (symbolArrayList.get(j).getType().equals("func")) {
                            i = symbolArrayList.get(j).sp;
                            break;
                        }
                    }
                    symbolArrayList.removeIf(item -> item.getDepth() == depth);
                    yunxingArrayList.removeIf(
                        item -> (
                            (item.type.equals(codeP.num) || item.type.equals(codeP.lvalCheck)) &&
                                item.getDepth() == depth));
                    depth--;
                    func = false;
                    dp = 0;
                }
            } else if (now.type.equals(codeP.para)) {
                if (d != 0) {
                    int m = 0;
                    int k = yunxingArrayList.size() - 1;
                    int k1 = 0;
                    for (; k >= 0; k--) {
                        if (yunxingArrayList.get(k).type.equals(codeP.num)) {
                            m++;
                            k1 = k;
                        } else if (yunxingArrayList.get(k).type.equals(codeP.lvalCheck)) {
                            m++;
                            k1 = k;
                            k = k - yunxingArrayList.get(k).dimension;
                        }
                        if (m == d) {
                            symbol s = new symbol(now.value, "para", "int", depth);
                            if (k1 == k) {
                                if (now.dimension == 0) {
                                    s.value = yunxingArrayList.get(k).number;
                                    s.dimension = 0;
                                    yunxingArrayList.remove(k);
                                    symbolArrayList.add(s);
                                } else if (now.dimension == 1) {
                                    for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                                        if (yunxingArrayList.get(k).value.equals(
                                            symbolArrayList.get(j).symbol) &&
                                            !symbolArrayList.get(j).type.equals("func") &&
                                            symbolArrayList.get(j).getDepth() != depth) {
                                            s.value1 =
                                                symbolArrayList.get(j).value1;
                                            s.dimension = 1;
                                            yunxingArrayList.remove(k);
                                            symbolArrayList.add(s);
                                            break;
                                        }
                                    }
                                } else if (now.dimension == 2) {
                                    for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                                        if (yunxingArrayList.get(k).value.equals(
                                            symbolArrayList.get(j).symbol) &&
                                            !symbolArrayList.get(j).type.equals("func") &&
                                            symbolArrayList.get(j).getDepth() != depth) {
                                            s.value2 =
                                                symbolArrayList.get(j).value2;
                                            s.dimension = 2;
                                            yunxingArrayList.remove(k);
                                            symbolArrayList.add(s);
                                            break;
                                        }
                                    }
                                }
                            } else if (k1 == k + 1) {
                                if (now.dimension == 0) {
                                    for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                                        if (yunxingArrayList.get(k + 1).value.equals(
                                            symbolArrayList.get(j).symbol) &&
                                            !symbolArrayList.get(j).type.equals("func") &&
                                            symbolArrayList.get(j).getDepth() != depth) {
                                            s.value =
                                                symbolArrayList.get(j).value1[yunxingArrayList.get(
                                                    k).number];
                                            s.dimension = 0;
                                            yunxingArrayList.remove(k);
                                            yunxingArrayList.remove(k);
                                            symbolArrayList.add(s);
                                            break;
                                        }
                                    }
                                } else if (now.dimension == 1) {
                                    for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                                        if (yunxingArrayList.get(k + 1).value.equals(
                                            symbolArrayList.get(j).symbol) &&
                                            !symbolArrayList.get(j).type.equals("func") &&
                                            symbolArrayList.get(j).getDepth() != depth) {
                                            s.value1 =
                                                symbolArrayList.get(j).value2[yunxingArrayList.get(
                                                    k).number];
                                            s.dimension = 1;
                                            yunxingArrayList.remove(k);
                                            yunxingArrayList.remove(k);
                                            symbolArrayList.add(s);
                                            break;
                                        }
                                    }
                                }
                            } else if (k1 == k + 2) {
                                for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                                    if (yunxingArrayList.get(k + 2).value.equals(
                                        symbolArrayList.get(j).symbol) &&
                                        !symbolArrayList.get(j).type.equals("func") &&
                                        symbolArrayList.get(j).getDepth() != depth) {
                                        s.value =
                                            symbolArrayList.get(j).value2[yunxingArrayList.get(
                                                k).number][yunxingArrayList.get(k + 1).number];
                                        s.dimension = 0;
                                        yunxingArrayList.remove(k);
                                        yunxingArrayList.remove(k);
                                        yunxingArrayList.remove(k);
                                        symbolArrayList.add(s);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                    d--;
                }
            } else if (now.type.equals(codeP.funcMain)) {
                depth++;
            } else if (now.type.equals(codeP.equal) || now.type.equals(codeP.equalN) ||
                now.type.equals(codeP.dayu) || now.type.equals(codeP.dayue) ||
                now.type.equals(codeP.xiaoyu) || now.type.equals(codeP.xiaoyue)) {
                codeP a1 = yunxingArrayList.get(temp - 2);
                codeP a2 = yunxingArrayList.get(temp - 1);
                codeP a = new codeP(codeP.Cond);
                a.depth = depth;
                if (a1.type.equals(codeP.num) && a2.type.equals(codeP.num)) {
                    switch (now.type) {
                        case codeP.equal:
                            a.TON = (a1.number == a2.number);
                            break;
                        case codeP.equalN:
                            a.TON = (a1.number != a2.number);
                            break;
                        case codeP.dayu:
                            a.TON = (a1.number > a2.number);
                            break;
                        case codeP.dayue:
                            a.TON = (a1.number >= a2.number);
                            break;
                        case codeP.xiaoyu:
                            a.TON = (a1.number < a2.number);
                            break;
                        case codeP.xiaoyue:
                            a.TON = (a1.number <= a2.number);
                            break;
                    }
                } else {
                    int b1;
                    int b2;
                    if (a1.type.equals(codeP.num)) {
                        b1 = a1.number;
                    } else {
                        b1 = a1.TON ? 1 : 0;
                    }
                    if (a2.type.equals(codeP.num)) {
                        b2 = a2.number;
                    } else {
                        b2 = a2.TON ? 1 : 0;
                    }
                    switch (now.type) {
                        case codeP.equal:
                            a.TON = (b1 == b2);
                            break;
                        case codeP.equalN:
                            a.TON = (b1 != b2);
                            break;
                        case codeP.dayu:
                            a.TON = (b1 > b2);
                            break;
                        case codeP.dayue:
                            a.TON = (b1 >= b2);
                            break;
                        case codeP.xiaoyu:
                            a.TON = (b1 < b2);
                            break;
                        case codeP.xiaoyue:
                            a.TON = (b1 <= b2);
                            break;
                    }
                }
                yunxingArrayList.remove(temp - 1);
                temp = yunxingArrayList.size();
                yunxingArrayList.remove(temp - 1);
                yunxingArrayList.add(a);
            } else if (now.type.equals(codeP.and) || now.type.equals(codeP.or)) {
                codeP a1 = yunxingArrayList.get(temp - 2);
                codeP a2 = yunxingArrayList.get(temp - 1);
                codeP a = new codeP(codeP.Cond);
                a.depth = depth;
                if (a1.type.equals(codeP.Cond) && a2.type.equals(codeP.Cond)) {
                    if (now.type.equals(codeP.and)) {
                        a.TON = (a1.TON && a2.TON);
                    } else {
                        a.TON = (a1.TON || a2.TON);
                    }
                } else {
                    Boolean b1;
                    Boolean b2;
                    if (a1.type.equals(codeP.num)) {
                        b1 = a1.number != 0;
                    } else {
                        b1 = a1.TON;
                    }
                    if (a2.type.equals(codeP.num)) {
                        b2 = a2.number != 0;
                    } else {
                        b2 = a2.TON;
                    }
                    if (now.type.equals(codeP.and)) {
                        a.TON = (b1 && b2);
                    } else {
                        a.TON = (b1 || b2);
                    }
                }
                yunxingArrayList.remove(temp - 1);
                temp = yunxingArrayList.size();
                yunxingArrayList.remove(temp - 1);
                yunxingArrayList.add(a);
            } else if (now.type.equals(codeP.If)) {
                if (yunxingArrayList.get(temp - 1).type.equals(codeP.Cond)) {
                    if (!yunxingArrayList.get(temp - 1).TON) {
                        do {
                            i++;
                            if (codePArrayList.get(i).type.equals(codeP.Else) &&
                                codePArrayList.get(i).number == now.number) {
                                break;
                            } else if (codePArrayList.get(i).type.equals(codeP.IfEnd) &&
                                codePArrayList.get(i).number == now.number) {
                                yunxingArrayList.remove(temp - 1);
                                break;
                            }
                        } while (true);
                    }
                } else if (yunxingArrayList.get(temp - 1).type.equals(codeP.num)) {
                    codeP a = new codeP(codeP.Cond);
                    a.depth = depth;
                    a.TON = !(yunxingArrayList.get(temp - 1).number == 0);
                    yunxingArrayList.remove(temp - 1);
                    yunxingArrayList.add(a);
                    if (!yunxingArrayList.get(temp - 1).TON) {
                        do {
                            i++;
                            if (codePArrayList.get(i).type.equals(codeP.Else) &&
                                codePArrayList.get(i).number == now.number) {
                                break;
                            } else if (codePArrayList.get(i).type.equals(codeP.IfEnd) &&
                                codePArrayList.get(i).number == now.number) {
                                yunxingArrayList.remove(temp - 1);
                                break;
                            }
                        } while (true);
                    }
                }
            } else if (now.type.equals(codeP.Else)) {
                if (yunxingArrayList.get(temp - 1).type.equals(codeP.Cond)) {
                    if (yunxingArrayList.get(temp - 1).TON) {
                        do {
                            i++;
                            if (codePArrayList.get(i).type.equals(codeP.ElseEnd) &&
                                codePArrayList.get(i).number == now.number) {
                                yunxingArrayList.remove(temp - 1);
                                break;
                            }
                        } while (true);
                    }
                } else if (yunxingArrayList.get(temp - 1).type.equals(codeP.num)) {
                    if (yunxingArrayList.get(temp - 1).number != 0) {
                        do {
                            i++;
                            if (codePArrayList.get(i).type.equals(codeP.ElseEnd) &&
                                codePArrayList.get(i).number == now.number) {
                                yunxingArrayList.remove(temp - 1);
                                break;
                            }
                        } while (true);
                    }
                }
            } else if (now.type.equals(codeP.IfEnd)) {
                yunxingArrayList.remove(temp - 1);
            } else if (now.type.equals(codeP.ElseEnd)) {
                yunxingArrayList.remove(temp - 1);
            } else if (now.type.equals(codeP.not)) {
                codeP a = yunxingArrayList.get(temp - 1);
                if (a.type.equals(codeP.num)) {
                    codeP b = new codeP(codeP.num);
                    if (a.number == 0) {
                        b.number = 1;
                    } else {
                        b.number = 0;
                    }
                    yunxingArrayList.remove(temp - 1);
                    yunxingArrayList.add(b);
                } else if (a.type.equals(codeP.Cond)) {
                    a.TON = (!a.TON);
                }
            } else if (now.type.equals(codeP.WhileFlag)) {
                for (int j = i + 1; j < codePArrayList.size(); j++) {
                    if (codePArrayList.get(j).type.equals(codeP.WhileEnd) &&
                        codePArrayList.get(j).number == now.number) {
                        codePArrayList.get(j).place = i;
                        break;
                    }
                }
            } else if (now.type.equals(codeP.While)) {
                if (yunxingArrayList.get(temp - 1).type.equals(codeP.Cond)) {
                    if (!yunxingArrayList.get(temp - 1).TON) {
                        do {
                            i++;
                            if (codePArrayList.get(i).type.equals(codeP.WhileEnd) &&
                                codePArrayList.get(i).number == now.number) {
                                yunxingArrayList.remove(temp - 1);
                                break;
                            }
                        } while (true);
                    }
                } else if (yunxingArrayList.get(temp - 1).type.equals(codeP.num)) {
                    codeP a = new codeP(codeP.Cond);
                    a.depth = depth;
                    a.TON = !(yunxingArrayList.get(temp - 1).number == 0);
                    yunxingArrayList.remove(temp - 1);
                    yunxingArrayList.add(a);
                    if (!yunxingArrayList.get(temp - 1).TON) {
                        do {
                            i++;
                            if (codePArrayList.get(i).type.equals(codeP.WhileEnd) &&
                                codePArrayList.get(i).number == now.number) {
                                yunxingArrayList.remove(temp - 1);
                                break;
                            }
                        } while (true);
                    }
                }
            } else if (now.type.equals(codeP.WhileEnd)) {
                yunxingArrayList.remove(temp - 1);
                i = now.place;
            } else if (now.type.equals(codeP.Break)) {
                do {
                    i++;
                    if (codePArrayList.get(i).type.equals(codeP.WhileEnd) &&
                        codePArrayList.get(i).number ==
                            now.number) {
                        temp = yunxingArrayList.size();
                        yunxingArrayList.remove(temp - 1);
                        break;
                    } else if (codePArrayList.get(i).type.equals(codeP.BlockL)) {
                        depth++;
                    } else if (codePArrayList.get(i).type.equals(codeP.BlockR)) {
                        symbolArrayList.removeIf(item -> item.getDepth() == depth);
                        yunxingArrayList.removeIf(
                            item -> ((item.type.equals(codeP.num) ||
                                item.type.equals(codeP.lvalCheck)) &&
                                item.getDepth() == depth));
                        depth--;
                    } else if (codePArrayList.get(i).type.equals(codeP.WhileEnd)) {
                        yunxingArrayList.removeIf(
                            item -> (item.type.equals(codeP.Cond) && item.depth == depth));
                    } else if (codePArrayList.get(i).type.equals(codeP.IfEnd)) {
                        yunxingArrayList.removeIf(
                            item -> (item.type.equals(codeP.Cond) && item.depth == depth));
                    } else if (codePArrayList.get(i).type.equals(codeP.ElseEnd)) {
                        yunxingArrayList.removeIf(
                            item -> (item.type.equals(codeP.Cond) && item.depth == depth));
                    }
                } while (true);
            } else if (now.type.equals(codeP.Continue)) {
                do {
                    i++;
                    if (codePArrayList.get(i).type.equals(codeP.WhileEnd) &&
                        codePArrayList.get(i).number ==
                            now.number) {
                        temp = yunxingArrayList.size();
                        yunxingArrayList.remove(temp - 1);
                        i = codePArrayList.get(i).place;
                        break;
                    } else if (codePArrayList.get(i).type.equals(codeP.BlockL)) {
                        depth++;
                    } else if (codePArrayList.get(i).type.equals(codeP.BlockR)) {
                        symbolArrayList.removeIf(item -> item.getDepth() == depth);
                        yunxingArrayList.removeIf(
                            item -> ((item.type.equals(codeP.num) ||
                                item.type.equals(codeP.lvalCheck)) &&
                                item.getDepth() == depth));
                        depth--;
                    } else if (codePArrayList.get(i).type.equals(codeP.WhileEnd)) {
                        yunxingArrayList.removeIf(
                            item -> (item.type.equals(codeP.Cond) && item.depth == depth));
                    } else if (codePArrayList.get(i).type.equals(codeP.IfEnd)) {
                        yunxingArrayList.removeIf(
                            item -> (item.type.equals(codeP.Cond) && item.depth == depth));
                    } else if (codePArrayList.get(i).type.equals(codeP.ElseEnd)) {
                        yunxingArrayList.removeIf(
                            item -> (item.type.equals(codeP.Cond) && item.depth == depth));
                    }
                } while (true);
            } else if (now.type.equals(codeP.ret)) {
                do {
                    i++;
                    if (i >= codePArrayList.size()) {
                        i--;
                        break;
                    } else if (codePArrayList.get(i).type.equals(codeP.funcEnd)) {
                        if (depth != 0) {
                            for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                                if (symbolArrayList.get(j).getType().equals("func")) {
                                    i = symbolArrayList.get(j).sp;
                                    if (symbolArrayList.get(j).getKind().equals("int")) {
                                        temp = yunxingArrayList.size();
                                        yunxingArrayList.get(temp - 1).depth = depth - 1;
                                    }
                                    break;
                                }
                            }
                            symbolArrayList.removeIf(item -> item.getDepth() == depth);
                            yunxingArrayList.removeIf(
                                item -> ((item.type.equals(codeP.num) ||
                                    item.type.equals(codeP.lvalCheck)) &&
                                    item.getDepth() >= depth));
                            depth--;
                            func = false;
                            dp = 0;
                        }
                        break;
                    } else if (codePArrayList.get(i).type.equals(codeP.BlockL)) {
                        depth++;
                    } else if (codePArrayList.get(i).type.equals(codeP.BlockR)) {
                        symbolArrayList.removeIf(item -> item.getDepth() == depth);
                        depth--;
                    } else if (codePArrayList.get(i).type.equals(codeP.WhileEnd)) {
                        yunxingArrayList.removeIf(
                            item -> (item.type.equals(codeP.Cond) && item.depth == depth));
                    } else if (codePArrayList.get(i).type.equals(codeP.IfEnd)) {
                        yunxingArrayList.removeIf(
                            item -> (item.type.equals(codeP.Cond) && item.depth == depth));
                    } else if (codePArrayList.get(i).type.equals(codeP.ElseEnd)) {
                        yunxingArrayList.removeIf(
                            item -> (item.type.equals(codeP.Cond) && item.depth == depth));
                    }
                } while (true);
            } else if (now.type.equals(codeP.BlockL)) {
                depth++;
            } else if (now.type.equals(codeP.BlockR)) {
                symbolArrayList.removeIf(item -> item.getDepth() == depth);
                yunxingArrayList.removeIf(
                    item -> ((item.type.equals(codeP.num) || item.type.equals(codeP.lvalCheck)) &&
                        item.getDepth() == depth));
                depth--;
            } else if (now.type.equals(codeP.orBegin)) {
                if (yunxingArrayList.get(temp - 1).type.equals(codeP.Cond)) {
                    if (yunxingArrayList.get(temp - 1).TON) {
                        do {
                            i++;
                        } while (!codePArrayList.get(i).type.equals(codeP.or));
                    }
                } else if (yunxingArrayList.get(temp - 1).type.equals(codeP.num)) {
                    codeP a = new codeP(codeP.Cond);
                    a.depth = depth;
                    a.TON = !(yunxingArrayList.get(temp - 1).number == 0);
                    yunxingArrayList.remove(temp - 1);
                    yunxingArrayList.add(a);
                    if (yunxingArrayList.get(temp - 1).TON) {
                        do {
                            i++;
                        } while (!codePArrayList.get(i).type.equals(codeP.or));
                    }
                }
            } else if (now.type.equals(codeP.andBegin)) {
                if (yunxingArrayList.get(temp - 1).type.equals(codeP.Cond)) {
                    if (!yunxingArrayList.get(temp - 1).TON) {
                        do {
                            i++;
                        } while (!codePArrayList.get(i).type.equals(codeP.and));
                    }
                } else if (yunxingArrayList.get(temp - 1).type.equals(codeP.num)) {
                    codeP a = new codeP(codeP.Cond);
                    a.depth = depth;
                    a.TON = !(yunxingArrayList.get(temp - 1).number == 0);
                    yunxingArrayList.remove(temp - 1);
                    yunxingArrayList.add(a);
                    if (!yunxingArrayList.get(temp - 1).TON) {
                        do {
                            i++;
                        } while (!codePArrayList.get(i).type.equals(codeP.and));
                    }
                }
            }
        }
        pcodeOut.close();
    }
}
