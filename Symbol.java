public class Symbol {
    private String symbol;
    private int state;
    public Symbol() {
        this("null", 0);
    }
    public Symbol (String item, int s) {
        symbol = item;
        state = s;
    }
    public String getSymbol() {
        return symbol;
    }
    public int getState() {
        return state;
    }
}