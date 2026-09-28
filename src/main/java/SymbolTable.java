import java.util.LinkedList;
import java.util.Queue;

public class SymbolTable {
    public static final int REGS = 7;
    Symbol[] SymbolTable;
    int totalElem;
    int usedRegisters;
    int usedMemory;
    int cache;
    int[] cacheMem;
    int heap;
    int[] heapMem;
    Queue<Integer> queue;

    public SymbolTable(int n) {
        SymbolTable = new Symbol[n];
        cacheMem = new int[32];
        heapMem = new int[64];
        totalElem = n;
        usedRegisters = 0;
        usedMemory = 0;
        cache = 222;
        heap = 221;
        queue = new LinkedList<>();
        for (int i = 1; i < REGS; i++) {
            queue.add(i);
        }
    }

    public int addRegister (String sym) {
        int i = findRegister(sym);
        if (i == -1) {
            for (int p = 0; p < totalElem; p++) {
                if (SymbolTable[p].getAddress() == -2) {
                    if (usedRegisters < REGS) {
                        int reg = usedRegisters;  
                        SymbolTable[p] = new Symbol(reg, sym, 0, 0);
                        usedRegisters++;
                        return reg;
                    } else {
                        return -1;
                    }
               }
            }
        }
        return i;
    }
    public int addMemory (int memAddr, String sym) {
        int i = search(sym);
        if (i == -1) {
            for (int p = 0; p < totalElem; p++) {
                if (SymbolTable[p].getAddress() == -2) {
                    SymbolTable[p] = new Symbol(memAddr, sym, 0 ,0);
                    usedMemory++;
                    return p;
               }
            }
        }
        return i;
    }
    public int findReplaceReg () {
        if (queue.size() == 0) {
            for (int i = 1; i < REGS; i++) {
                queue.add(i);
            }
        }
        int reg = queue.poll();
        return reg;
        
    }

    public int addCache() {
        cache++;
        for (int i = 0; i < cacheMem.length; i++) {
            if (cacheMem[i] == 0) {
                cacheMem[i] = cache;
                break;
            }
        }
        return cache;
    }

    public int[] returnCache() {
        return cacheMem;
    }

    public int addHeap() {
        for (int i = 0; i < heapMem.length; i++) {
            if (heapMem[i] != 0) {
                int tmp = heapMem[i];
                heapMem[i] = 0;
                return tmp;
            }
        }
        heap--;
        return heap;
    }


    public void changeSymbol(String newSym, String oldSym) {
        for (int i = 0; i < totalElem; i++) {
            if (SymbolTable[i].getSymbol().equals(oldSym)) {
                SymbolTable[i].changeSym(newSym);
            }
        }
    }

    public void changeSym (int loc, String newLoc) {
        for (int i = 0; i < totalElem; i++) {
            if (SymbolTable[i].getAddress() == loc) {
                String oldSym = SymbolTable[i].getSymbol();
                String newSym = oldSym.substring(0,2) + newLoc;
                SymbolTable[i].changeSym(newSym);
                break;
            }  
        }
    }

    public void changeLoc (String sym, int loc) {
        for (int i = 0; i < totalElem; i++) {
            if (SymbolTable[i].getSymbol().equals(sym)) {
                SymbolTable[i].changeLoc(loc);
                break;
            }
        }
    }

    public int getUsedMemory() {
        return (usedRegisters + usedMemory);
    }
    
    public int getUsedRegisters() {
        return usedRegisters;
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

    public Symbol findSymbol (int loc) {
        for (int i = 0; i < totalElem; i++) {
            if (SymbolTable[i].getAddress() == loc) {
                return SymbolTable[i];
            }  
            
        }
        return new Symbol();
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
    
    public Symbol returnSymbol (String sym) {
        for (int i = 0; i < totalElem; i++) {
            if (SymbolTable[i].getSymbol().equals(sym)) {
                return SymbolTable[i];
            }  
        }
        return new Symbol();

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

