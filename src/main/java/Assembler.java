/*
 * Copyright (C) 2026 Sherry Lei
 *
 * This file is part of TOY Assembler.
 *
 * TOY Assembler is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, version 3 of the License.
 *
 * TOY Assembler is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * TOY Assembler. If not, see <https://www.gnu.org/licenses/>.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Assembler {
    public static final int REGS = 7;
    int ogAddr; //address that marks the first line
    static int addr; //address that updates
    String inst;
    String[] instructStorage;
    List<Symbol> buffer;
    SymbolTable totalMemory; //imitates the total memory of the machine
    int x; //length of file

    public Assembler (String file, PrintWriter p) {
        In in = new In(file);
        x = 0;
        instructStorage = new String[256];
        buffer = new ArrayList<>();
        totalMemory = new SymbolTable(256 + REGS);
        //272
        totalMemory.initialize();
        totalMemory.addRegister("0");
        ogAddr = addr;
        while (in.hasNextLine()) {
            String line = in.readLine();
            instructStorage[x] = line.trim().replace(",", "");
            x++;
        }
        for (int i = 0; i < x; i++) {
            set(instructStorage[i], p);
            addr++;
        }
        dumpToFile(p, ogAddr);
    }

    public void set (String line, PrintWriter p) {
        int op = 0;
        inst = "";
        //p.write("\n");
        String[] instA = line.split("\s+");
        int lineStart = 0;
        
        switch (instA[0]) {  
                case "H":       // halt
                    buffer.add(new Symbol(addr, "0000", 0, 0));
                    //p.write(printHex(addr) + ": 0000");
                    return;         
                case  "A":      // add      
                    lineStart = 0;
                    op = 1;       
                    break;        
                case  "S":       // subtract     
                    lineStart = 0;
                    op = 2;       
                    break;         
                case "BA":      // bitwise and 
                    lineStart = 0;
                    op = 3;       
                    break;          
                case "BX":      // bitwise xor   
                    lineStart = 0;
                    op = 4;       
                    break;          
                case "LS":       // shift left
                    lineStart = 0;  
                    op = 5;       
                    break;         
                case "RS":       // shift right   
                    lineStart = 0;
                    op = 6;       
                    break;         
                case "LA":       // load address 
                    lineStart = 0;  
                    op = 7;       
                    break;         
                case  "L":       // load   
                    lineStart = 0;
                    op = 8;       
                    break;         
                case "ST":      // store     
                    lineStart = 0;
                    op = 9;       
                    break;          
                case "LI":      // load indirect  
                    lineStart = 0;
                    op = 10;     
                    break;          
                case "SI":      // store indirect
                    lineStart = 0; 
                    op = 11;      
                    break;          
                case "BZ":      // branch if zero    
                    lineStart = 0; 
                    op = 12;      
                    break;          
                case "BP":      // branch if positive    
                    lineStart = 0;
                    op = 13;      
                    break;          
                case "JR":       // jump indirect
                    lineStart = 0;    
                    op = 14;      
                    break;         
                case "JL":       // jump and link 
                     lineStart = 0;
                     op = 15;      
                     break; 
                default:
                    if (totalMemory.search(instA[0]) == -1)  {
                        totalMemory.addMemory(addr, instA[0]); 
                    }
                    int findAddr = totalMemory.search(instA[0]);
                    switch (instA[1]) {
                        case "H":       // halt
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                buffer.add(new Symbol(findAddr, "0000", 0, 0));

                                //p.write(printHex(addr) + ": C0" + printHex(findAddr));
                                //p.write("\n");
                                //p.write(printHex(findAddr) + ": 0000");
                            } else {
                                buffer.add(new Symbol(addr, "0000", 0, 0));

                                //p.write(printHex(addr) + ": 0000");
                            }
                            return;         
                        case  "A":      // add    
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;
                            op = 1;       
                            break;        
                        case  "S":       // subtract   
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;
                            op = 2;       
                            break;         
                        case "BA":      // bitwise and 
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;
                            op = 3;       
                            break;          
                        case "BX":      // bitwise xor   
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;
                            op = 4;       
                            break;          
                        case "LS":       // shift left
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;  
                            op = 5;       
                            break;         
                        case "RS":       // shift right   
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;
                            op = 6;       
                            break;         
                        case "LA":       // load address 
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;  
                            op = 7;       
                            break;         
                        case  "L":       // load   
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;
                            op = 8;       
                            break;         
                        case "ST":      // store     
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;
                            op = 9;       
                            break;          
                        case "LI":      // load indirect  
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;
                            op = 10;     
                            break;          
                        case "SI":      // store indirect
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1; 
                            op = 11;      
                            break;          
                        case "BZ":      // branch if zero   
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1; 
                            op = 12;      
                            break;          
                        case "BP":      // branch if positive
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }    
                            lineStart = 1;
                            op = 13;      
                            break;          
                        case "JR":       // jump indirect
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;    
                            op = 14;      
                            break;         
                        case "JL":       // jump and link 
                            if (findAddr != addr) {
                                String s = "C0" + printHex(findAddr);
                                buffer.add(new Symbol(addr, s,0 ,0 ));
                                addr = findAddr;
                            }
                            lineStart = 1;
                            op = 15;      
                            break; 
                        default:
                            // for parsing lines like (N 000C)
                            if (findAddr != -1)  {
                                String format = " ";
                                format = instA[1];
                                format = format.trim();
                                int padding = 4 - format.length();
                                for (int a = 0; a < padding; a++) {
                                    format = "0" + format;
                                }
                                // used to be addr
                                buffer.add(new Symbol(findAddr , format, 0, 0));
                               // p.write(printHex(findSymbol) + ": " + format);
                                return;
                            } 
                            StdOut.println("Error: On Line " + (addr - ogAddr + 1) + ": " + instA[1] + " is not recognized as an actual opcode"); 
                            System.exit(1); 
                            break;
                    }
        }
    
        String[] bits = new String[instA.length - lineStart - 1];
        for (int i = lineStart + 1; i < instA.length; i++) {
            bits[i - lineStart - 1] = instA[i];
        }
       
        inst += printReg(op);
    
        switch (op) {
            case 1: case 2: case 3: case 4: case 5: case 6:     //RR format - two registers
                                                                //addition and logic operations                                               
                int d = totalMemory.addRegister(bits[0]); 
                if (d == -1) {
                    System.out.println(" Add " + bits[0]);
                    d = generateStoreCache(bits[0], p);
                } else if (d >= REGS) {
                    System.out.println("Load " + bits[0] + " to reg");
                    d = generateLoadCache(bits[0], d, p);
                }
                setFlagsToCurrent(bits[0], 'A');
                inst += d;
                int s = totalMemory.findRegister(bits[1]);
                if (s == -1) {
                     StdOut.println("Error: On Line " + (addr - ogAddr + 1) + ": Register " + bits[1] + " not found"); 
                     System.exit(1); 
                } else if (s >= REGS) {
                    System.out.println("Load " + bits[1] + " to reg");
                    s = generateLoadCache(bits[1], s, p);
                }
                setFlagsToCurrent(bits[1], 'U');
                inst += s;
                int t = totalMemory.findRegister(bits[2]);
                if (t == -1) {
                    StdOut.println("Error: On Line " + (addr - ogAddr + 1) + ": Register " + bits[2] + " not found"); 
                    System.exit(1);
                } else if (t >= REGS) {
                    System.out.println("Load " + bits[2] + " to reg");
                    t = generateLoadCache(bits[2], t, p);
                }
                setFlagsToCurrent(bits[2], 'U');
                inst += t;
                break;
            case 7:                                         //A format - one address
                                                            //one register, one address
                                                            //data transfers for the first time
                
                int reg = totalMemory.addRegister(bits[0]);         //register 
                if (reg == -1) {
                    System.out.println("Add " + bits[0]);
                    reg = generateStoreCache(bits[0], p);
                } else if (reg >= REGS) {
                    System.out.println("Load " + bits[0] + " to reg");
                    reg = generateLoadCache(bits[0], reg, p);
                }
                setFlagsToCurrent(bits[0], 'A');
                inst += reg;                                                 
                if (bits[1].length() == 1) {               
                    bits[1] = "0" + bits[1];               //adds padding
                    inst += bits[1];
                } else {
                    inst += printHex(Integer.parseInt(bits[1]));
                }
                break;   
            case 8: case 9:
                if (op == 8) {
                    int regd = totalMemory.addRegister(bits[0]);                   //register 
                    if (regd == -1) {
                        System.out.println(" Add " + bits[0]);
                        regd = generateStoreCache(bits[0], p);
                    } else if (regd >= REGS) {
                        System.out.println("Load " + bits[0] + " to reg");
                        regd = generateLoadCache(bits[0], regd, p);
                    }
                    setFlagsToCurrent(bits[0], 'A');
                    inst += regd;  
                } else {
                    int regD = totalMemory.findRegister(bits[0]);
                    if (regD == -1) {
                        StdOut.println("Error: On Line " + (addr - ogAddr + 1) + ": Register " + bits[0] + " not found"); 
                        System.exit(1);
                    } else if (regD >= REGS) {
                        System.out.println("Load " + bits[0] + " to reg");
                        regD = generateLoadCache(bits[0], regD, p);
                    }
                    setFlagsToCurrent(bits[0], 'U');
                    inst += regD;
                }   
                if (((op == 9) && (bits[1].equals("stdout"))) || ((op == 8) && (bits[1].equals("stdin")))) {
                    inst += "FF";
                } else {
                    int heap = totalMemory.addHeap();
                    totalMemory.addMemory(heap, bits[1]);
                    inst += printHex(heap);
                }
                break;    
            case 10: case 11:                                       // indirect load, indirect storage
                                                                    // two registers
                int reg1 = totalMemory.addRegister(bits[0]); 
                if (reg1 == -1) {
                    System.out.println("Add " + bits[0]);
                    reg1 = generateStoreCache(bits[0], p);
                } else if (reg1 >= REGS) {
                    System.out.println("Load " + bits[0] + " to reg");
                    reg1 = generateLoadCache(bits[0], reg1, p);
                }                
                inst += reg1;  
                inst += "0";
                int reg2 = totalMemory.addRegister(bits[2]);
                if (reg2 == -1) {
                    System.out.println("Add " + bits[2]);
                    reg2 = generateStoreCache(bits[2], p);
                } else if (reg2 >= REGS) {
                    System.out.println("Load " + bits[2] + " to reg");
                    reg2 = generateLoadCache(bits[2], reg2, p);
                }
                inst += reg2;
                break;
            case 12: case 13: //branches format: d, addr
                int start1 = 0;
                for (int i = 0; i < bits.length; i++) {
                    if (bits[i].equals("0")) {
                        start1 = i;
                        inst += "0";
                        break;
                    } else {
                        start1 = i;
                        int regB = totalMemory.findRegister(bits[i]);
                        if (regB == -1) {
                             StdOut.println("Error: On Line + " + (addr - ogAddr + 1) + "Register " + bits[i] + " not found"); 
                             System.exit(1); 
                        } else {
                            if (regB >= REGS) {
                                System.out.println("Load " + bits[i] + " to reg");
                                regB = generateLoadCache(bits[i], regB, p);
                            }
                            inst += regB;
                            break;
                        }       
                    }
                }
                for (int i = start1 + 1; i < bits.length; i++) {
                    int label = totalMemory.search(bits[i]);
                    if (label != -1) {
                        if (label < addr) {
                            int loopStart = label;
                            int loopEnd = addr;
                            modifyBuffer(loopStart, loopEnd - 1);
                        }
                        label = totalMemory.search(bits[i]);
                        inst += printHex(label);
                        break;
                    } else {
                        int heap = totalMemory.addHeap();
                        totalMemory.addMemory(heap, bits[1]);
                        inst += printHex(heap);
                    }
                }
                break;                                
            case 14: // jump register d, 00 
                for (int i = 0; i < bits.length; i++) {
                    if (bits[i].equals("0")) {
                        inst += "0";
                        break;
                    } else {
                        int regD = totalMemory.findRegister(bits[i]);
                        if (regD == -1) {
                            StdOut.println("Error: On Line + " + (addr - ogAddr + 1) + ": Register " + bits[i] + " not found"); 
                            System.exit(1); 
                        } else {
                             if (regD >= REGS) {
                                regD = generateLoadCache(bits[i], regD, p);
                            }
                            inst += regD;
                            break;
                        }
                    }
                }
                inst += "00";
                break;
            case 15: //jump and link d, addr -> read directly from file (change addr to symbolic name)
                int regPC = totalMemory.addRegister(bits[0]);                   //register 
                if (regPC == -1) {
                    regPC = generateStoreCache(bits[0], p);
                } else if (regPC >= REGS) {
                    regPC = generateLoadCache(bits[0], regPC, p);
                }
                setFlagsToCurrent(bits[0], 'A');
                inst += regPC;                          
                     
                int findAddr = totalMemory.search(bits[1]);
                if (findAddr != -1) {
                    inst += printHex(findAddr);
                } else { 
                    if ((fromHex82(bits[1]) >= 0) && (fromHex82(bits[1]) <= 255)) {
                        inst += Integer.parseInt(bits[1]);
                    }
                }
                break; 
            default: break;
        }
        //totalMemory.addMemory(addr, inst);
        //trackMemory();
        System.out.println("Finished " + inst + " from " + printHex(addr));
        buffer.add(new Symbol(addr, inst, 0, 0));
        //p.write(printHex(addr) + ": " + inst);
    }
    
    public void modifyBuffer (int loopStart, int loopEnd) {
        // check to see what variables are stored in the cache
        int[] retrieveCache = totalMemory.returnCache();
        Symbol[] variablesInCache = new Symbol[32];
        int k = 0;
        for (int i = 0; i < retrieveCache.length; i++) {
            if (retrieveCache[i] != 0) {
                Symbol symInCache = totalMemory.findSymbol(retrieveCache[i]);
                variablesInCache[k] = symInCache;
                Symbol symInTable = totalMemory.returnSymbol(symInCache.getSymbol());
                // deal with variables that act like constants
                if ((symInTable.getAssignedNumber() < loopStart) && (symInTable.getUsedNumber() > loopStart)) {
                    int wrongPlace = symInCache.getUsedNumber() - ogAddr;
                    int correctPlace = loopStart - ogAddr;
                    Symbol bufferInst = buffer.get(wrongPlace);
                    bufferInst.changeLoc(loopStart);
                    buffer.remove(wrongPlace);
                    for (int t = wrongPlace; t < buffer.size(); t++ ) {
                        Symbol re = buffer.get(t);
                        buffer.set(t, re.changeLoc(re.getAddress() - 1));
                    }
                    buffer.add(correctPlace, bufferInst);
                    for (int t = correctPlace + 1; t < buffer.size(); t++ ) {
                        Symbol re = buffer.get(t);
                        buffer.set(t, re.changeLoc(re.getAddress() + 1));
                    }
                    Symbol startLoopMarker = totalMemory.findSymbol(loopStart);
                    startLoopMarker.changeLoc(startLoopMarker.getAddress() + 1);
                } else if ((symInTable.getAssignedNumber() > loopStart) && (symInTable.getUsedNumber() > loopStart)) {
                            int assignNumber = symInCache.getAssignedNumber();
                            int usedNumber = symInCache.getUsedNumber();
                            Symbol assignInst = buffer.get(assignNumber - ogAddr);
                            String assignI = assignInst.getSymbol();
                            if (assignI.startsWith("F")) {
                                buffer.remove(usedNumber - ogAddr);
                                for (int p = usedNumber - ogAddr; p < buffer.size(); p++) {
                                    Symbol re = buffer.get(p);
                                    buffer.set(p, re.changeLoc(re.getAddress() - 1));
                                }
                                addr--;
                                break;
                            }
                            
                            int reg;
                            if (usedNumber != 0) {
                                Symbol storingInst = buffer.get(usedNumber - ogAddr);
                                String in = storingInst.getSymbol();
                                reg = in.charAt(1) - '0';

                            } else {
                                break;
                            }
                            String generateLoad = "8" + reg + printHex(retrieveCache[i]);
                            buffer.add(new Symbol(addr, generateLoad, 0, 0));
                            addr++;
                }
            }   
            k++;
        }
    }
    public void setFlagsToCurrent (String sym, char flag) {
        if (flag == 'A') {
            totalMemory.returnSymbol(sym).setAssignedNumber(addr);
        } else if (flag == 'U') {
            totalMemory.returnSymbol(sym).setUsedNumber(addr);
        } else if (flag == 'a') {
            totalMemory.returnSymbol(sym).setAssignedNumber(0);
        } else if (flag == 'u') {
            totalMemory.returnSymbol(sym).setUsedNumber(0);
        }
    }

    // copy over flags from ogSym to newSym
    public void copyFlags (String newSym, String ogSym, char flag) {
        if (flag == 'A') {
            int oldAddr = totalMemory.returnSymbol(ogSym).getAssignedNumber();
            totalMemory.returnSymbol(newSym).setAssignedNumber(oldAddr);
        } else if (flag == 'U') {
            int oldAddr = totalMemory.returnSymbol(ogSym).getUsedNumber();
            totalMemory.returnSymbol(newSym).setUsedNumber(oldAddr);
        }
    }

    public void setFlags (String sym, int oldAddr, char flag) {
        if (flag == 'A') {
            totalMemory.returnSymbol(sym).setAssignedNumber(oldAddr);
        } else if (flag == 'U') {
            totalMemory.returnSymbol(sym).setUsedNumber(oldAddr);
        }

    }
    public int generateStoreCache (String newSym, PrintWriter p) {
        int reg;
        int usedNumber = totalMemory.returnSymbol(newSym).getUsedNumber();
        if (usedNumber != 0) {
            Symbol storingInst = buffer.get(usedNumber - ogAddr);
            String i = storingInst.getSymbol();
            reg = i.charAt(1) - '0';
        } else {
            reg = totalMemory.findReplaceReg(); 
        }
        Symbol oldSym = totalMemory.findSymbol(reg); 
        String oldSymV = oldSym.getSymbol();
        int cacheAddress = totalMemory.addCache();

        System.out.println("Introducing new Register " + newSym + " at " + reg);
        System.out.println("Replacing old register " + oldSymV + " to " + cacheAddress);
        String inst = "";
        inst += "9";
        inst += reg;
        inst += printHex(cacheAddress);
        
        // set used flag of old Sym to the line that it gets replaced
        setFlagsToCurrent(oldSymV, 'U');

        // now save the used and assigned numbers of oldSym
        int usedTemp = oldSym.getUsedNumber();
        int assignedTemp = oldSym.getAssignedNumber();

        // copies over the assigned/used flag new Sym had with it in cache
        copyFlags(oldSymV, newSym, 'A');
        copyFlags(oldSymV, newSym, 'U');

        totalMemory.changeSymbol(newSym, oldSymV); 
        totalMemory.addMemory(cacheAddress, oldSymV);

        // sets the flag of old Sym in cache to what it used to be
        setFlags(oldSymV, assignedTemp, 'A');
        setFlags(oldSymV, usedTemp, 'U');
       
        //setFlags(newSym, 'a');
        //setFlags(newSym, 'u');
        //totalMemory.addMemory(addr, inst);
        buffer.add(new Symbol(addr, inst, 0, 0));
        //p.write(printHex(addr) + ": " + inst);
        //p.write("\n");
        addr++;

        return reg;
    }

    public int generateLoadCache (String newSym, int cacheAddr, PrintWriter p) {
        int reg = generateStoreCache(newSym, p);
        
        System.out.println("Loading from " + cacheAddr + " for " + newSym + " to Reg " + reg);
        String inst = "";
        inst += "8";
        inst += reg;
        inst += printHex(cacheAddr);

        //totalMemory.addMemory(addr, inst);
        
        buffer.add(new Symbol(addr, inst, 0, 0));
        //p.write(printHex(addr) + ": " + inst);
        //p.write("\n");
        addr++;

        //totalMemory.resetLoc(cacheAddr);
        return reg;
    }

    public void dumpToFile(PrintWriter p, int ogAddr) {
        for (int i = 0; i < buffer.size(); i++) {
            Symbol s = buffer.get(i);
            p.write(printHex(s.getAddress()) + ": " + s.getSymbol());
            p.write("\n");
        }
    }
    
    public void trackMemory() {
        int usedMemory = totalMemory.getUsedMemory();
        if (usedMemory == 272) {
            StdOut.println("Error: No more memory"); 
            System.exit(1); 
        }
         if (usedMemory > 260) {
            StdOut.println("Warning: Memory is almost all used");
         }
    }

    // return a 2-digit hex string corresponding to 16-bit integer n
    public static String printHex(int n) {
        return String.format("%02X", n & 0xFFFF);
    }
    // return a 1-digit hex string corresponding to 16-bit integer n
    public static String printReg(int n) {
        return String.format("%01X", n & 0xFFFF);
    }
    // return a 16-bit integer corresponding to the 4-digit hex string s
    public static int fromHex16(String s) {
       return Integer.parseInt(s, 16) & 0xFFFF;
    }
    // return a 8-bit integer corresponding to the 2-digit hex string s
    public static int fromHex82 (String s) {
        return Integer.parseInt(s, 4) & 0xFF;
    }
    public static void main(String[] args) {
        addr = fromHex16(args[0]);
        String filename = args[1];
        String outputfile =  args[2];
        try {
            File file = new File(outputfile);
        if (file.createNewFile()) {
            StdOut.println("File successfully created: " + file.getName());
        } else {
            StdOut.println("File already exists");
        } 
            PrintWriter writer = new PrintWriter(file);
            @SuppressWarnings("unused")
            Assembler assembler = new Assembler (filename, writer);
            writer.close(); 
        } catch (IOException e) {
            StdOut.println("error");
        }
    }
}
