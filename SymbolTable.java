public class SymbolTable {
    Symbol[] SymbolTable;
    int totalElem;
    public SymbolTable(int n) {
        SymbolTable = new Symbol[n];
        totalElem = n;
    }
    public int addRegister(String sym, String stored) {
        int i = search(sym);
        if (i == -1) {
            for (int p = 0; p < totalElem; p++) {
                if (SymbolTable[p].getState() == 0) {
                    SymbolTable[p] = new Symbol(p, sym, stored, 1);
                    return p;
               }
            }
        }
        return i;
    }
    public int addMemory(int loc, String sym, String stored) {
        int i = search(sym);
        if (i == -1) {
            for (int p = 0; p < totalElem; p++) {
                if (SymbolTable[p].getState() == 0) {
                    SymbolTable[p] = new Symbol(loc, sym, stored, 1);
                    return p;
               }
            }
        }
        return i;
    }
    public int search(String sym) {
        for (int i = 0; i < totalElem; i++) {
            if (SymbolTable[i].getState() == 1) {
                if (SymbolTable[i].getSymbol().equals(sym)) {
                    return i;
                }  
            }
        }
        return -1;
    }
    public int find(String sym) {
        int i = search(sym);
        if (i == -1) {
            return -1;
        } else {
            if (SymbolTable[i].getSymbol().equals(sym)) {
                return SymbolTable[i].getAddress();
            } else {
                return -1;
            }
    }
    }
    public String getStored (String sym) {
        int i = search(sym);
        if (i == -1) {
            return "not found";
        } else {
            if (SymbolTable[i].getSymbol().equals(sym)) {
                return SymbolTable[i].getActualValue();
            } else {
                return "not found";
            }
    }
    }
    public void initialize() {
        for (int i = 0; i < totalElem; i++) {
            SymbolTable[i] = new Symbol();
        }
    }
    public void printST() {
        for (int i = 0; i < totalElem; i++) {
            if (SymbolTable[i].getState() == 1) {
                StdOut.println(SymbolTable[i].getSymbol());
            }
        }
    }

   }

