import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Assembler {
    int ogAddr; //address that marks the first line
    int addr; //address that updates
    String inst;
    String[] instructStorage;
    SymbolTable totalMemory; //imitates the total memory of the machine
    int x; //length of file

    public Assembler (String file, int addr, PrintWriter p) {
        In in = new In(file);
        x = 0;
        instructStorage = new String[256];
        totalMemory = new SymbolTable(272);
        totalMemory.initialize();
        totalMemory.addRegister("0");
        ogAddr = addr;
        while (in.hasNextLine()) {
            String line = in.readLine();
            instructStorage[x] = line.trim().replace(",", "");
            x++;
        }
        for (int i = 0; i < x; i++) {
            set(instructStorage[i], addr, p);
            addr++;
        }
    }

    public void set (String line, int addr, PrintWriter p) {
        int op = 0;
        inst = "";
        p.write("\n");
        String[] instA = line.split(" ");
        int lineStart = 0;
        
        // for parsing lines like (N 000C)
        int findSymbol = totalMemory.search(instA[0]);
        if (findSymbol != -1) {
            String format = " ";
            for (int i = 1; i < instA.length; i++) {
                if (instA[i] != "") {
                    format = instA[i];
                }
            }
            format = format.trim();
            int padding = 4 - format.length();
            for (int a = 0; a < padding; a++) {
                format = "0" + format;
            }
            p.write(printHex(addr) + ": " + format);
            return;
        }

        for (int i = 0; i < instA.length; i++) {
            if (op != 0) {
                break;
            }
            switch (instA[i]) {  
                case "H":       // halt
                    p.write(printHex(addr) + ": 0000");
                    return;         
                case  "A":      // add      
                    lineStart = i;
                    op = 1;       
                    break;        
                case  "S":       // subtract     
                    lineStart = i;
                    op = 2;       
                    break;         
                case "BA":      // bitwise and 
                    lineStart = i;
                    op = 3;       
                    break;          
                case "BX":      // bitwise xor   
                    lineStart = i;
                    op = 4;       
                    break;          
                case "LS":       // shift left
                    lineStart = i;  
                    op = 5;       
                    break;         
                case "RS":       // shift right   
                    lineStart = i;
                    op = 6;       
                    break;         
                case "LA":       // load address 
                    lineStart = i;  
                    op = 7;       
                    break;         
                case  "L":       // load   
                    lineStart = i;
                    op = 8;       
                    break;         
                case "ST":      // store     
                    lineStart = i;
                    op = 9;       
                    break;          
                case "LI":      // load indirect  
                    lineStart = i;
                    op = 10;     
                    break;          
                case "SI":      // store indirect
                    lineStart = i; 
                    op = 11;      
                    break;          
                case "BZ":      // branch if zero    
                    lineStart = i; 
                    op = 12;      
                    break;          
                case "BP":      // branch if positive    
                    lineStart = i;
                    op = 13;      
                    break;          
                case "JR":       // jump indirect
                    lineStart = i;    
                    op = 14;      
                    break;         
                case "JL":       // jump and link 
                     lineStart = i;
                     op = 15;      
                     break;         
                case "loop" :
                case "done" :
                     break;
                default:
                     StdOut.println("Error: On Line " + (addr - ogAddr + 1) + ": " + instA[i] + " is not recognized as an actual opcode"); 
                     System.exit(1); 
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
                inst += d;
                int s = totalMemory.findRegister(bits[1]);
                if (s == -1) {
                     StdOut.println("Error: On Line " + (addr - ogAddr + 1) + ": Register " + bits[1] + " not found"); 
                     System.exit(1); 
                }
                inst += s;
                int t = totalMemory.findRegister(bits[2]);
                if (t == -1) {
                    StdOut.println("Error: On Line " + (addr - ogAddr + 1) + ": Register " + bits[2] + " not found"); 
                    System.exit(1);
                }
                inst += t;
                break;
            case 7:                                         //A format - one address
                                                            //one register, one address
                                                            //data transfers for the first time
                
                int reg = totalMemory.addRegister(bits[0]);         //register 
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
                    inst += regd;  
                } else {
                    int regD = totalMemory.findRegister(bits[0]);
                    if (regD == -1) {
                        StdOut.println("Error: On Line " + (addr - ogAddr + 1) + ": Register " + bits[0] + " not found"); 
                        System.exit(1);
                    }
                    inst += regD;
                }   
                if (((op == 9) && (bits[1].equals("stdout"))) || ((op == 8) && (bits[1].equals("stdin")))) {
                    inst += "FF";
                } else {
                    int addrT = ogAddr;
                    for (int i = 0; i < x; i++) {
                        if (instructStorage[i].startsWith(bits[1])) {
                            addrT += i;
                        }     
                    }
                    inst += printHex(addrT);
                    totalMemory.addMemory(addrT, bits[1]);   //add it to the mainMemory MST with associated address
                }
                break;    
            case 10: case 11:                                       // indirect load, indirect storage
                                                                    // two registers
                int reg1 = totalMemory.addRegister(bits[0]);                  
                inst += reg1;  
                int reg2 = totalMemory.addRegister(bits[2]);
                inst += reg2;
                break;
            case 12: case 13: //branches format: d, addr
                for (int i = 0; i < bits.length; i++) {
                    if (bits[i].equals("0")) {
                        inst += "0";
                        break;
                    } else {
                        int regB = totalMemory.findRegister(bits[i]);
                        if (regB == -1) {
                             StdOut.println("Error: On Line + " + (addr - ogAddr + 1) + "Register " + bits[i] + " not found"); 
                             System.exit(1); 
                        } else {
                            inst += regB;
                            break;
                        }       
                    }
                }
                if (instA[0].equals("loop")) { //start
                    int end = ogAddr; //find end of the loop
                    for (int i = 0; i < x; i++) {
                        if (instructStorage[i].startsWith(bits[1])) {
                            end += i;
                        }
                    }
                    inst += printHex(end); 
                } else {   //end
                    int start = ogAddr; //find start of loop
                    for (int i = 0; i < x; i++) {
                        if (instructStorage[i].startsWith(bits[1])) {
                            start += i;
                        }
                    }
                    inst += printHex(start); 
                }
                break;                                
            case 14: //jump register d, 00 
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
                            inst += regD;
                            break;
                        }
                    }
                }
                inst += "00";
                break;
            case 15: //jump and link d, addr -> read directly from file (change addr to symbolic name)
                int regPC = totalMemory.addRegister(bits[0]);                   //register 
                inst += regPC;                          
                     
                int findAddr = totalMemory.search(bits[1]);
                if (findAddr != -1) {
                    inst += printHex(findAddr);
                } else { 
                    if ((fromHex82(bits[1]) >= 0) && (fromHex82(bits[1]) <= 255)) {
                        inst += printHex(Integer.parseInt(bits[1]));
                    }
                }
                break; 
            default: break;
        }
        if (bits[0] != "0") {
            cleanUpRegister(bits[0], addr - ogAddr, line.indexOf(bits[0]));
        }
        if ((op == 1) || (op == 2) || (op == 3) || (op == 4) || (op == 5) || (op == 6) || (op == 10) || (op == 11)) {
            if (!((op == 10) || (op == 11))) {
                if (bits[1] != "0") {
                    cleanUpRegister(bits[1], addr - ogAddr, line.indexOf(bits[1]));
                }
            }
            if (bits[2] != "0") {
                cleanUpRegister(bits[2], addr - ogAddr, line.indexOf(bits[2]));
            }
        }   
        totalMemory.addMemory(addr, inst);
        trackMemory();
        p.write(printHex(addr) + ": " + inst);
    }
    
    public void cleanUpRegister(String sym, int currentIndex, int cursorPos) {
        for (; currentIndex < instructStorage.length; currentIndex++) {
            String line = instructStorage[currentIndex];
            if (line == null) {
                totalMemory.cleanUp(sym);
                return;
            }
            int index = line.indexOf(sym, cursorPos + 1);
            if (index != -1) {
                if ((line.charAt(index - 1) == ' ') && (index + sym.length() == line.length())) {
                    return;
                }
                if ((line.charAt(index - 1) == ' ') && (line.charAt(index + sym.length()) == ' ')) {
                    return;
                }
            }
            cursorPos = -1;
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
        int addr = fromHex16(args[0]);
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
            Assembler assembler = new Assembler (filename, addr, writer);
            writer.close(); 
        } catch (IOException e) {
            StdOut.println("error");
        }
    }
}