import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
//one symbol table for both registers and memory
//too many registers (overflow: register space is not sufficent)
    //if registers run out, how to delete not-used registers or use memory instead
//quit everything if registers/memory overflow
public class Assembler {
    int ogAddr; //address that marks the first line
    int addr; //address that updates
    String inst;
    String[] instructStorage;
    SymbolTable registers; //imitates registers of the machine
    SymbolTable TOYmemory; //imitates the memory of the machine
    int x; //length of file

    public Assembler (String file, int addr, PrintWriter p) {
        In in = new In(file);
        x = 0;
        instructStorage = new String[256];
        registers = new SymbolTable(16);
        TOYmemory = new SymbolTable(256);
        registers.initialize();
        TOYmemory.initialize();
        registers.addRegister("0",  "0");
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

        int findSymbol = TOYmemory.find(instA[0]);
        if (findSymbol != -1) {
            String format = TOYmemory.getStored(instA[0]);
            format = format.trim();
            int padding = 4 - format.length();
            for (int a = 0; a < padding; a++) {
                format += "0";
            }
            p.write(printHex(addr) + ": " + format);
            return;
        }

        for (int i = 0; i < instA.length; i++) {
            if (op != 0) {
                break;
            }
            switch (instA[i]) {  
                case "H": 
                    p.write(printHex(addr) + ": 0000");
                    return;         //halt
                case  "A":     
                    lineStart = i;
                    op = 1;       
                    break;          // add  
                case  "S":     
                    lineStart = i;
                    op = 2;       
                    break;          // subtract
                case "BA":   
                      lineStart = i;
                      op = 3;       
                      break;          // bitwise and
                case "BX":    
                    lineStart = i;
                    op = 4;       
                    break;          // bitwise xor
                case "LS":   
                    lineStart = i;  
                    op = 5;       
                    break;          // shift left
                case "RS":     
                    lineStart = i;
                    op = 6;       
                    break;          // shift right
                case "LA":   
                    lineStart = i;  
                    op = 7;       
                    break;          // load address
                case  "L":    
                    lineStart = i;
                     op = 8;       
                     break;          // load
                case "ST":     
                    lineStart = i;
                    op = 9;       
                    break;          // store
                case "LI":     
                    lineStart = i;
                    op = 10;     
                    break;          // load indirect
                case "SI":    
                    lineStart = i; 
                    op = 11;      
                    break;          // store indirect
                case "BZ":    
                    lineStart = i; 
                    op = 12;      
                    break;          // branch if zero
                case "BP":     
                    lineStart = i;
                    op = 13;      
                    break;          // branch if positive
                case "JR": 
                    lineStart = i;    
                    op = 14;      
                    break;          // jump indirect
                case "JL":    
                     lineStart = i;
                     op = 15;      
                     break;          // jump and link 
                case "loop" :
                case "done" :
                    break;
                default:
                     StdOut.println(instA[i] + " is not recognized as an actual opcode"); 
                     
                     StdOut.println("File not written successfully");
                     return;
            }
       }

       String[] bits = new String[instA.length - lineStart - 1];
       for (int i = lineStart + 1; i < instA.length; i++) {
            bits[i - lineStart - 1] = instA[i];
       }
       
       inst += printReg(op);
    
        switch (op) {
            case 1: case 2: case 3: case 4: case 5: case 6:                    //RR format - two registers                            //d, s, t - total of three
                int regN = registers.addRegister(bits[0], "operation"); //addition and logic operations
                inst += regN;
                int s = registers.search(bits[1]);
                inst += s;
                int t = registers.search(bits[2]);
                inst += t;
            break;
            case 7:                                        //A format - one address
                                                                   //one register, one address
                                                               //data transfers for the first time
                int reg = registers.addRegister(bits[0], bits[1]);                   //register 
                inst += reg;                          
                                                              
                                                         //addr
                    if (bits[1].length() == 1) {               
                        bits[1] = "0" + bits[1];               //adds padding
                        inst += bits[1];
                    } else {
                        inst += printHex(Integer.parseInt(bits[1]));
                    }
                
                break;   
            case 8: case 9:
                 
                int regd = registers.addRegister(bits[0], bits[1]);                   //register 
                inst += regd;   
                if (((op == 9) && (bits[1].equals("stdout"))) || ((op == 8) && (bits[1].equals("stdin")))) {
                    inst += "FF";
                } else {
                    int addrT = ogAddr;;
                    String stored = "";
                    for (int i = 0; i < x; i++) {
                        if (instructStorage[i].startsWith(bits[1])) {
                            addrT += i;
                            String[] temp = instructStorage[i].split(" ");
                            for (int q = 2; q < temp.length; q++) {
                                if (!(temp[q].equals(""))) {
                                    String value = "";
                                    int padding = 4 - temp[q].trim().length();
                                    for (int a = 0; a < padding; a++) {
                                        value += "0";
                                    }
                                    stored = value + temp[q].trim();
                                }
                        }
                            }
                            
                    }
                            inst += printHex(addrT);
                            TOYmemory.addMemory(addrT, bits[1], stored);   //add it to the mainMemory MST with associated address
                    

                }
                break;    
            case 10: case 11:                                       //indirect storage
                                                 //one register, one address
                int reg1 = registers.find(bits[0]);                   //register 
                inst += reg1;  
                int addr1 = TOYmemory.find(bits[1]);
                inst += addr1;
                break;
            case 12: case 13: //branches format: d, addr
                for (int i = 0; i < bits.length; i++) {
                    if (bits[i].equals("0")) {
                        inst += "0";
                        break;
                    } else {
                    if (registers.find(bits[i]) != -1) {
                        int regB = registers.find(bits[i]);
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
                    if (registers.find(bits[i]) != -1) {
                        int regD = registers.find(bits[i]);
                        inst += regD;
                     break;
                } 
            }
            }
                inst += "00";
                break;
            case 15: //jump and link d, addr -> read directly from file
                     int regPC = registers.addRegister(bits[0], "pc");                   //register 
                     inst += regPC;                          
                           
                    if ((fromHex82(bits[1]) >= 0) && (fromHex82(bits[1]) <= 255)) {
                            inst += printHex(Integer.parseInt(bits[1]));
                    }
                
                
                break; 
            default: break;
        }
        p.write(printHex(addr) + ": " + inst);
    }

    // return a 4-digit hex string corresponding to 16-bit integer n
    public static String toHex(int n) {
        return String.format("%04X", n & 0xFFFF);
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
    // return a 8-bit integer corresponding to the 4-digit hex string s
    public static int fromHex8(String s) {
        return Integer.parseInt(s, 16) & 0xFF;
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