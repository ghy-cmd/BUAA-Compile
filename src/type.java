public class type {
    String identity;
    String content;
    int row = 0;
    String wrong = null;

    public type(String identity, String content) {
        this.identity = identity;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getIdentity() {
        return identity;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
