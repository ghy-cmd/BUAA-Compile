import java.util.ArrayList;

public class symbol {
    String symbol;
    String type;
    String kind;
    int depth;
    int dimension;
    ArrayList<para> paraArrayList;
    int value;
    int[] value1 = new int[1000];
    int[][] value2 = new int[1000][1000];
    int place;
    int sp;
    int d1;
    int d2;


    public symbol(String symbol, String type, String kind, int depth) {
        this.symbol = symbol;
        this.type = type;
        this.kind = kind;
        this.depth = depth;
        if (type.equals("func")) {
            this.paraArrayList = new ArrayList<>();
        }
    }

    public int getDepth() {
        return depth;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getDimension() {
        return dimension;
    }

    public String getKind() {
        return kind;
    }

    public String getType() {
        return type;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
