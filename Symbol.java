public class Symbol {
    private String symbol;
    private int loc; 
    private String stored;
    private int state;

    public Symbol() {
        this(0, "null", "null", 0);
    }
    public Symbol (int addr, String sym, String storage, int s) {
        symbol = sym;
        loc = addr;
        stored = storage;
        state = s;
      
    }
    public String getSymbol() {
        return symbol;
    }
    public int getAddress() {
        return loc;
    }
    public String getActualValue() {
        return stored;
    }
    public int getState() {
        return state;
    }
}