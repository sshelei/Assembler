public class SymbolTable {
    Symbol[] SymbolTable;
    int totalElem;
    int usedRegisters;
    int usedMemory;

    public SymbolTable(int n) {
        SymbolTable = new Symbol[n];
        totalElem = n;
        usedRegisters = 0;
        usedMemory = 0;
    }
    public int addRegister(String sym) {
        int i = findRegister(sym);
        if (i == -1) {
            for (int p = 0; p < totalElem; p++) {
                if (SymbolTable[p].getAddress() == -2) {
                    int reg = usedRegisters; 
                    SymbolTable[p] = new Symbol(reg, sym);
                    usedRegisters++;
                    return reg;
               }
            }
        }
        return i;
    }
    public int addMemory(int memAddr, String sym) {
        int i = search(sym);
        if (i == -1) {
            for (int p = 0; p < totalElem; p++) {
                if (SymbolTable[p].getAddress() == -2) {
                    SymbolTable[p] = new Symbol(memAddr, sym);
                    usedMemory++;
                    return p;
               }
            }
        }
        return i;
    }
    public int cleanUp (String sym) {
        for (int p = 0; p < totalElem; p++) {
            if (SymbolTable[p].getSymbol().equals(sym)) {
                SymbolTable[p].reset();
                usedRegisters--;
                return 1;
            }
        }
        return 0;
    }
    
    public int getUsedMemory() {
        return (usedRegisters + usedMemory);
    }
    
    // from symbolic name for register, find actual register
    // returns -1 if register not found
    public int findRegister (String sym) {
        for (int i = 0; i < totalElem; i++) {
            if (SymbolTable[i].getAddress() != -2)  {
                if (SymbolTable[i].getSymbol().equals(sym)) {
                    return SymbolTable[i].getAddress();
                }  
            }
        }
        return -1;
    }

    public int search(String sym) {
        for (int i = 0; i < totalElem; i++) {
            if (SymbolTable[i].getAddress() != -2) {
                if (SymbolTable[i].getSymbol().equals(sym)) {
                    return SymbolTable[i].getAddress();
                }  
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
            if (SymbolTable[i].getAddress() != -2) {
                StdOut.println(SymbolTable[i].getSymbol());
            }
        }
    }
}

