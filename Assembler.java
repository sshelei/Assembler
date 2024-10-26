import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
public class Assembler {
    int addr;
    String inst;
    SymbolTable reg;
    public Assembler (String file, int addr, PrintWriter p) {
        In in = new In(file);
        reg = new SymbolTable(16);
        reg.initialize();
        reg.add("0");
        while (in.hasNextLine()) {
            String line = in.readLine();
            set(line, file, addr, p);
            addr++;
        }
    }
    public void set (String line, String file, int addr, PrintWriter p) {
        if (line.startsWith(" ")) {
            line = line.trim();
        } else {
            String memVariable = line.substring(0,1);
        }
        String[] instA = line.split(" ", 2);
        String opS = instA[0];
        inst = "";
        int op = 0;
        if (opS.equals("H")) {                  // halt
            p.write(printHex(addr) + ": 0000");
            return;
        }
        switch (opS) {  
            case  "A":     op = 1 ;      break;          // add  
            case  "S":     op = 2;       break;          // subtract
            case "BA":     op = 3;       break;          // bitwise and
            case "BX":     op = 4;       break;          // bitwise xor
            case "LS":     op = 5;       break;          // shift left
            case "RS":     op = 6;       break;          // shift right
            case "LA":     op = 7;       break;          // load address
            case  "L":     op = 8;       break;          // load
            case "ST":     op = 9;       break;          // store
            case "LI":     op = 10;      break;          // load indirect
            case "SI":     op = 11;      break;          // store indirect
            case "BZ":     op = 12;      break;          // branch if zero
            case "BP":     op = 13;      break;          // branch if positive
            case "JR":     op = 14;      break;          // jump indirect
            case "JI":     op = 15;      break;          // jump and link          
        }
        inst += op;
        String instS = instA[1];
        String[] bits;
        switch (op) {
            case 1: case 2: case 3: case 4: case 5: case 6: case 10: case 11: //RR format - two registers
                bits = instS.split(", ");                               //d, s, t - total of three
                for (int i = 0; i < bits.length; i++) {
                        int regN = reg.add(bits[i]);  
                        inst += regN;
                }
            break;
            case 7: case 8: case 9: case 12: case 13: case 15: //A format - one address
                bits = instS.split(", ");                //one register, one address
                int regN = reg.add(bits[0]);                   //d 
                inst += regN;                          
                                                               //addr
                if (op == 7) {                                 //just for load address
                    if (bits[1].length() == 1) {
                        bits[1] = "0" + bits[1];               //adds padding
                        inst += bits[1];
                    } else {
                        inst += fromHex8(bits[1]);
                    }
                } else {                                       //for memory addresses
                    int i = findMem(file, bits[0], addr);
                    inst += printHex(i);
                }                                           
                break;
            case 14:
            break;
        }
        p.write(printHex(addr) + ": " + inst);
        p.write("\n");
    }

    public static int findMem (String s, String variable, int addr) {
        In in = new In(s);
        while (in.hasNextLine()) {
            String line = in.readLine();
            if (line.startsWith(variable)) {
                return addr;
            }
        }
        return -1;
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
   
    public static void main(String[] args) {
        int addr = fromHex16(args[0]);
        String filename = args[1];
        String outputfile = args[2];
        try {
            File file = new File(outputfile);
        if (file.createNewFile()) {
            StdOut.println("File created: " + file.getName());
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