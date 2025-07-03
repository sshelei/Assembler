import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class AssemblerTest {
    int addr;
    String inst;
    String[] instructStorage;
    int x;
    public AssemblerTest (String file, int addr, PrintWriter p) {
        In in = new In(file);
        x = 0;
        instructStorage = new String[256];
        while (in.hasNextLine()) {
            String line = in.readLine();
            instructStorage[x] = line.trim().replace(",", "");
            x++;
        }
        for (int i = 0; i < x; i++) {
            System.out.println(instructStorage[i]);
        }
    }
    public static int fromHex16(String s) {
        return Integer.parseInt(s, 16) & 0xFFFF;
    }
    public static void main(String[] args) {
        int addr = fromHex16(/*args[0] */ "10");
        String filename = /* args[1]*/  "trial.ass";
        String outputfile = /* args[2] */  "trial.toy";
        try {
            File file = new File(outputfile);
        if (file.createNewFile()) {
            StdOut.println("File created: " + file.getName());
        } else {
            StdOut.println("File already exists");
        } 
            PrintWriter writer = new PrintWriter(file);
            @SuppressWarnings("unused")
            AssemblerTest assemblerTest = new AssemblerTest (filename, addr, writer);
            writer.close(); 
        } catch (IOException e) {
            StdOut.println("error");
        }
    }
}
    
    

