public class SymbolTable {
    Symbol[] SymbolTable;
    int totalElem;
    public SymbolTable(int n) {
        SymbolTable = new Symbol[n];
        totalElem = n;
    }
    public int add(String item) {
        int i = search(item);
        if (i == -1) {
            for (int p = 0; p < totalElem; p++) {
                if (SymbolTable[p].getState() == 0) {
                    SymbolTable[p] = new Symbol(item, 1);
                    return p;
               }
            }
        }
        return i;
        
    }
    public int search(String item) {
        for (int i = 0; i < totalElem; i++) {
           if (SymbolTable[i].getSymbol().equals(item)) {
                return i;
           }
        }
        return -1;
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

