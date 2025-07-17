public class Symbol {
    private String symbol; // symbolic name
    private int loc;  // actual in TOY

    public Symbol() {
        this(-2, "null");
    }

    public Symbol (int addr, String sym) {
        symbol = sym;
        loc = addr; 
    }

    public String getSymbol() {
        return symbol;
    }

    public int getAddress() {
        return loc;
    }

    public void reset() {
        loc = -2;
    }
}