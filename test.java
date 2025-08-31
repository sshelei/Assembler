import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class test {
    
    public static void main(String[] args) {
        List<Symbol> buffer = new ArrayList<>();
        //Symbol a = new Symbol(40, "7101");
        //Symbol b = new Symbol(41, "7A00");
        //Symbol c = new Symbol(42, "7B01");
       // Symbol d = new Symbol(43, "894C");
        //Symbol e = new Symbol(44, "C94B");
        //Symbol f = new Symbol(44, "91DF");
        //Symbol g = new Symbol(45, "9AFF");
        buffer.add(new Symbol(40, "7101", 0,0));
        //buffer.add(b);
        //buffer.add(c);
        //buffer.add(d);
        //buffer.add(e);
        //buffer.add(g);
        for (int i = 0; i < buffer.size(); i++) {
            Symbol s = buffer.get(i);
            System.out.println(s.getAddress() + " : " + s.getSymbol());
        }
        System.out.print("\n");
        buffer.add(0, new Symbol(44, "91DF",0,0));
        for (int t = 5; t < buffer.size(); t++ ) {
            Symbol re = buffer.get(t);
            buffer.set(t, re.changeLoc(re.getAddress() + 1));
        }
        for (int i = 0; i < buffer.size(); i++) {
            Symbol s = buffer.get(i);
            System.out.println(s.getAddress() + " : " + s.getSymbol());
        }
        BitSet bits = new BitSet(8);
        bits.set(0);
        bits.set(1);
        bits.set(7);
        for (int i = 0; i < 8; i++) {
            System.out.println(bits.get(i) + " ");
        }
        System.out.println("\n");
        BitSet flags = new BitSet(3);
        flags.set(0);
        flags.set(1);
        flags.set(2);
        for (int i = 0; i < 3; i++) {
            System.out.println(flags.get(i) + " ");
        }
    }
    
}
