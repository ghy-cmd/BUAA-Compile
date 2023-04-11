import java.io.PrintWriter;
import java.util.ArrayList;

public class check {

    public static ArrayList<Integer> misE = new ArrayList<>();
    public static ArrayList<Integer> misD = new ArrayList<>();

    public static int checkA(PrintWriter output, String fs, int row) {
        int i = 0;
        int num = 0;
        StringBuilder f = new StringBuilder(fs);
        f.delete(0, 1);
        f.delete(f.length() - 1, f.length());
        while (true) {
            if (i >= f.length()) {
                break;
            }
            if (f.charAt(i) == 32 || f.charAt(i) == 33 ||
                (f.charAt(i) >= 40 && f.charAt(i) <= 126)) {
                i++;
            } else if (f.charAt(i) == '%') {
                i++;
                if (i >= f.length()) {
                    output.println(row + " a");
                    num = -1;
                    break;
                } else if (f.charAt(i) == 'd') {
                    num++;
                    i++;
                } else {
                    num = -1;
                    output.println(row + " a");
                    break;
                }
            } else {
                num = -1;
                output.println(row + " a");
                break;
            }
            if (i >= f.length()) {
                break;
            }
        }
        return num;
    }

    public static void checkE(ArrayList<symbol> symbolArrayList, int iStart, int iFinal, int rowNow,
                              String funcNow, PrintWriter output, ArrayList<type> lexList) {
        int levelS = 0;
        int levelM = 0;
        ArrayList<para> paras = new ArrayList<>();
        if (iStart + 1 == iFinal) {
            for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                symbol s = symbolArrayList.get(j);
                if (s.getSymbol().equals(funcNow) &&
                    s.getType().equals("func")) {
                    if (s.paraArrayList.size() == 0) {
                        break;
                    } else {
                        if (!misD.contains(rowNow)) {
                            output.println(rowNow + " d");
                            misD.add(rowNow);
                        }
                        break;
                    }
                }
            }
        } else {
            int dimension = 0;
            for (int i = iStart; i <= iFinal; i++) {
                String r = lexList.get(i).getContent();
                String t = lexList.get(i).getIdentity();
                if (r.equals("[")) {
                    if (levelM == 0) {
                        dimension--;
                    }
                    levelM++;
                } else if (r.equals("]")) {
                    levelM--;
                } else if (r.equals(",") && levelS == 1) {
                    para p = new para("int", dimension);
                    paras.add(p);
                    dimension = 0;
                } else if (r.equals("(")) {
                    levelS++;
                } else if (r.equals(")")) {
                    if (levelS == 1) {
                        para p = new para("int", dimension);
                        paras.add(p);
                        dimension = 0;
                    }
                    levelS--;
                } else if (r.equals("{")) {
                    dimension++;
                } else if (t.equals("IDENFR") && levelM == 0) {
                    for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                        symbol s = symbolArrayList.get(j);
                        if (s.getSymbol().equals(r)) {
                            if (s.getType().equals("func") && s.getKind().equals("int")) {
                                dimension = 0;
                            } else {
                                dimension = s.getDimension();
                            }
                            break;
                        }
                    }
                }
            }
            for (int j = symbolArrayList.size() - 1; j >= 0; j--) {
                symbol s = symbolArrayList.get(j);
                if (s.getSymbol().equals(funcNow) &&
                    s.getType().equals("func")) {
                    if (s.paraArrayList.size() == paras.size()) {
                        boolean same = true;
                        for (int i = 0; i < paras.size(); i++) {
                            if (s.paraArrayList.get(i).dimension != paras.get(i).dimension) {
                                same = false;
                                break;
                            }
                        }
                        if (same) {
                            break;
                        } else {
                            if (!misE.contains(rowNow)) {
                                output.println(rowNow + " e");
                                misE.add(rowNow);
                            }
                        }
                        break;
                    } else {
                        if (!misD.contains(rowNow)) {
                            output.println(rowNow + " d");
                            misD.add(rowNow);
                        }
                        break;
                    }
                }
            }
        }
    }
}
